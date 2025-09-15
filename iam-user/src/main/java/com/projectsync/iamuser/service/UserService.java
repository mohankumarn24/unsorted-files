package com.projectsync.iamuser.service;

import com.projectsync.iamuser.Exception.UserNotFoundException;
import com.projectsync.iamuser.config.PaginationProperties;
import com.projectsync.iamuser.dto.request.UserRequest;
import com.projectsync.iamuser.dto.response.UserResponse;
import com.projectsync.iamuser.entity.User;
import com.projectsync.iamuser.mapper.UserMapper;
import com.projectsync.iamuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${pagination.enabled}")
    String isPaginationEnabled;

    private final PaginationProperties paginationProperties;

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest userRequest) {

        User savedUser = userRepository.save(UserMapper.mapUserToEntity(userRequest));
        return UserMapper.mapUserToDto(savedUser);
    }

    @Cacheable(value = "cacheStore", key = "#userId")
    public UserResponse getUserById(String userId) {

        User savedUser = userRepository
                .findById(Integer.parseInt(userId))
                // .orElse(new User());
                .orElseThrow(() ->
                        new UserNotFoundException(new StringBuilder("User not found for id: ").append(userId).toString()));
        return UserMapper.mapUserToDto(savedUser);
    }

    public List<UserResponse> getAllUsers() {

        List<User> savedUsers = userRepository.findAll();
        List<UserResponse> userResponseList = savedUsers.stream()
                .map(user -> UserMapper.mapUserToDto(user))
                .collect(Collectors.toList());
        return userResponseList;
    }

    public List<UserResponse> getAllUsersPagination(String pageNo, String pageSize, String sortBy, String sortDir) throws Exception {

        if (isPaginationEnabled.equalsIgnoreCase("false")) {
            throw new Exception("Pagination not enabled");
        }

        // read properties file
        System.out.println("Default pagination pageNo: " + paginationProperties.getPageNo());
        System.out.println("Default pagination pageSize: " + paginationProperties.getPageSize());
        System.out.println("Default pagination sortBy: " + paginationProperties.getSortBy());
        System.out.println("Default pagination sortDir: " + paginationProperties.getSortDir());

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNo), Integer.parseInt(pageSize), sort);
        Page<User> usersPage = userRepository.findAll(pageable);
        if (usersPage.hasContent()) {
            return usersPage.getContent().stream()
                    .map(UserMapper::mapUserToDto)
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("No users for pagination");
        }
    }

    @CachePut(value = "cacheStore", key = "#userId")
    public UserResponse updateUserById(UserRequest userRequest, String userId) {

        User savedUser = userRepository
                .findById(Integer.parseInt(userId))
                .orElseThrow(() ->
                        new UserNotFoundException(new StringBuilder("user not found for id: ").append(userId).toString()));

        savedUser.setFirstName(userRequest.getFirstName());
        savedUser.setLastName(userRequest.getLastName());
        savedUser.setAddress(userRequest.getAddress());
        // todo: profile update

        return UserMapper.mapUserToDto(userRepository.save(savedUser));
    }


    @CacheEvict(value = "cacheStore", allEntries = true)
    public String deleteUserById(int userId) {

        userRepository.deleteById(userId);
        return "user deleted successfully";
    }

    // @Cacheable(value = "getUserByEmail-cache", key = "#emailId")
    public UserResponse getUserByEmail(String emailId) {

        User savedUser = userRepository.findByEmail(emailId).orElseThrow(() -> new UserNotFoundException("User not found for id: " + emailId));
        return UserMapper.mapUserToDto(savedUser);
    }
}


/* steps for L1 cache
1. Add below annotation in IamUserApplication
    @EnableCaching
2. Add below annotation for getUserById method and getUserByEmail method: will cache response
    @Cacheable(value = "getUserById-cache", key = "#userId")
    @Cacheable(value = "getUserByEmail-cache", key = "#emailId")
3. Add below annotation (optional) to cache response
    @CachePut(value = "getUserById-cache", key = "#userId")
4. Add below annotation to evict
    @CacheEvict(value = {"getUserById-cache", "getUserByEmail-cache"}, allEntries = true)
    // @CacheEvict(value = "getUserById-cache", key = "#userId")

Notes for L1 Cache:
 - https://www.youtube.com/watch?v=C9guc_x-QIw
 - https://stackoverflow.com/questions/25379051/spring-cache-evict-multiple-caches
 - https://docs.spring.io/spring-framework/docs/4.0.x/spring-framework-reference/html/cache.html


@CachePut always lets the method execute. It is generally used if you want your cache to be updated with the result of the method execution.
Example: When you want to update a stale data which is cached, instead of blowing the cache completely.

@Cacheable will be executed only once for the given cachekey and subsequent requests won't execute the method, until the cache expires or gets flushed.

steps for L2 cache:
1. Same steps as L1 cache +
2. Add 3 dependencies
3. Cnfigure EH-Cache 3 in AppConfig
4. Implement serializable interface for UserResponse
*/


/*
Notes for Pagination: getAllUsersPagination()
 http://localhost:8080/employees?pageSize=5&pageNo=1&sortBy=firstName
 https://reflectoring.io/spring-boot-paging/
 select id, first_name, last_name, address, email from userdatabase.users order by id asc limit 5,3
 select id, first_name, last_name, address, email from userdatabase.users order by id asc limit offset, records
 offset --> start index; records -> number of records to return
 return 3 records from index 5
 */