package com.projectsync.iamuser.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomError {

    private Instant timestamp;

    private String message;

    private String path;

    private String errorCode;
}
