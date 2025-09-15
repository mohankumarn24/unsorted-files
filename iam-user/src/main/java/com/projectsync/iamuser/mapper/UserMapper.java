package com.projectsync.iamuser.mapper;

import com.projectsync.iamuser.dto.request.PhoneRequest;
import com.projectsync.iamuser.dto.response.PhoneResponse;
import com.projectsync.iamuser.dto.response.ProfileResponse;
import com.projectsync.iamuser.dto.request.UserRequest;
import com.projectsync.iamuser.dto.response.UserResponse;
import com.projectsync.iamuser.entity.Phone;
import com.projectsync.iamuser.entity.Profile;
import com.projectsync.iamuser.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {

    }

    public static User mapUserToEntity(UserRequest userRequest) {

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setAddress(userRequest.getAddress());
        user.setEmail(userRequest.getEmail());

        String phoneNumber = Optional.ofNullable(userRequest.getPhoneRequest().getPhoneNumber())
                .orElse("9999999999");
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        phone.setUser(user);
        user.setPhone(phone);

        List<Profile> profiles = userRequest.getProfileRequests().stream()
                        .map(profileRequest -> {
                            Profile profile = new Profile();
                            profile.setProfileType(profileRequest.getProfileType());
                            profile.setPermissions(profileRequest.getPermissions());
                            profile.setUser(user);
                            return profile;
                        })
                        .collect(Collectors.toList());
        user.setProfiles(profiles);

        return user;
    }

    public static UserResponse mapUserToDto(User user) {

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setAddress(user.getAddress());
        userResponse.setEmail(user.getEmail());

        userResponse.setPhoneResponse(new PhoneResponse(user.getPhone().getId(), user.getPhone().getPhoneNumber()));

        List<ProfileResponse> profileResponses = user.getProfiles().stream()
                .map(profile -> new ProfileResponse(profile.getId(), profile.getProfileType(), profile.getPermissions()))
                .collect(Collectors.toList());
        userResponse.setProfiles(profileResponses);

        return userResponse;
    }
}
