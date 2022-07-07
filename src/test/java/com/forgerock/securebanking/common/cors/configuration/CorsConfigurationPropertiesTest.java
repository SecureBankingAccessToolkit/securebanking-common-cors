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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CorsConfigurationProperties}
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CorsConfigurationProperties.class, initializers = ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties(value = CorsConfigurationProperties.class)
public class CorsConfigurationPropertiesTest {
    private static final String EXPECTED_ALLOWED_DOMAIN = "domain4test.com";

    @Autowired
    private CorsConfigurationProperties corsConfigurationProperties;

    @Test
    public void haveProperties() {
        assertThat(corsConfigurationProperties.getAllowedOrigins()).anyMatch(o -> o.endsWith(EXPECTED_ALLOWED_DOMAIN));
        assertThat(corsConfigurationProperties.getAllowedHeaders()).contains("accept-api-version");
        assertThat(corsConfigurationProperties.getAllowedMethods()).containsIgnoringCase("post");
        assertThat(corsConfigurationProperties.isAllowedCredentials()).isTrue();
        assertThat(corsConfigurationProperties.getMaxAge()).isEqualTo("3600");
    }
}
