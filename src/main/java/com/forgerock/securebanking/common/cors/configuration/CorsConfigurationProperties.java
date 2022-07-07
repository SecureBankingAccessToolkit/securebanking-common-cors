/**
 * Copyright © 2020-2021 ForgeRock AS (obst@forgerock.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.forgerock.securebanking.common.cors.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 */
@Configuration
@ConfigurationProperties(prefix = "common.cors")
@Data
public class CorsConfigurationProperties {
    /* yaml configuration descriptor for common cors library
    common: # root key for all common 'securebanking-common-*' libraries
      cors: # library name
        allowed_origins: # the values could be a subdomain
            - localhost
            - dev.forgerock.financial
            - forgerock.financial
        allowed_headers: accept-api-version, x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN, Id-Token
        allowed_methods: GET, PUT, POST, DELETE, OPTIONS, PATCH
        allowed_credentials: true
        max_age: 3600
     */
    // CorsFilter
    @Value("${allowed_origins:localhost}")
    private List<String> allowedOrigins;
    private String allowedHeaders;
    private String allowedMethods;
    @Value("${allowed_credentials:true}")
    private boolean allowedCredentials;
    @Value("${max_age:3600}")
    private String maxAge;
}
