package com.projectsync.iamuser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneResponse implements Serializable {

    int id;

    String phoneNumber;
}
