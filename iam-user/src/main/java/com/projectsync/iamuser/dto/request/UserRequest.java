package com.projectsync.iamuser.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    int id;

    @NotBlank(message = "User first name should not be null or empty")
    String firstName;

    @NotBlank(message = "User last name should not be null or empty")
    String lastName;

    @NotBlank(message = "User address name should not be null or empty")
    String address;

    @NotBlank(message = "User email should not be null or empty")
    @Email(message = "Email address should be valid")
    private String email;

    PhoneRequest phoneRequest;

    List<ProfileRequest> profileRequests;
}
