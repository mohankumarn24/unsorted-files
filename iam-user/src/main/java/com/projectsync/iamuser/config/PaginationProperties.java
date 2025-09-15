package com.projectsync.iamuser.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "pagination.default")
// @Configuration
@Data
@NoArgsConstructor
public class PaginationProperties {

    String pageNo;

    String pageSize;

    String sortBy;

    String sortDir;
}
