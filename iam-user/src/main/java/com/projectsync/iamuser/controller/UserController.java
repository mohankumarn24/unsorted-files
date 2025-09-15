package com.projectsync.iamuser.controller;

import com.projectsync.iamuser.constants.AppConstants;
import com.projectsync.iamuser.dto.request.UserRequest;
import com.projectsync.iamuser.dto.response.UserResponse;
import com.projectsync.iamuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest){

        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") String userId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("header1", "value1");
        return new ResponseEntity<>(userService.getUserById(userId), headers, HttpStatus.OK);
    }

    @GetMapping("/getUserByEmail/{emailId}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable("emailId") String emailId) {

        return new ResponseEntity<>(userService.getUserByEmail(emailId), HttpStatus.OK);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/getUsersPagination")
    public ResponseEntity<List<UserResponse>> getAllUsersPagination(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_NUM, required = false) String pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGINATION_DEFAULT_PAGE_SIZE, required = false) String pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.PAGINATION_DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.PAGINATION_DEFAULT_SORT_DIR, required = false) String sortDir) throws Exception {

        return new ResponseEntity<>(userService.getAllUsersPagination(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserResponse> updateUserById(
            @Valid @RequestBody UserRequest userRequest,
            @PathVariable("userId") String userId) {

        return new ResponseEntity<>(userService.updateUserById(userRequest, userId), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable("userId") Integer userId) {

        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);
    }
}
