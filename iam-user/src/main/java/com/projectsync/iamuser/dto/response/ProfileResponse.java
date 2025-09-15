package com.projectsync.iamuser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse implements Serializable {

    int id;

    String ProfileType;

    String permissions;
}
