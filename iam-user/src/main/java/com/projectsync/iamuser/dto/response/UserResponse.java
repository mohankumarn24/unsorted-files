package com.projectsync.iamuser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {

    int id;

    String firstName;

    String lastName;

    String address;

    String email;

    PhoneResponse phoneResponse;

    List<ProfileResponse> profiles;
}
