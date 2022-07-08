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
package com.forgerock.securebanking.common.cors.filter;

import com.forgerock.securebanking.common.cors.configuration.CorsConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.forgerock.securebanking.common.cors.CorsConstants.*;
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpMethod.OPTIONS;

/**
 * This filter is an object that performs filtering tasks to validate the multiple domains allowed for non-origin domain requests to comply with CORS policy<br>
 * <br>
 * <p>
 * {@link CorsConfigurationProperties}: Class to load the properties to validate the allowed domains and set the CORS header values
 * </p>
 * Perform filtering in the <code>doFilter</code> method.
 */
@Component
@Slf4j
public class CorsFilter implements Filter {

    private final CorsConfigurationProperties filterConfigurationProperties;

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("### Initiating {} ###", this.getClass().getSimpleName());
    }

    public CorsFilter(CorsConfigurationProperties filterConfigurationProperties) {
        this.filterConfigurationProperties = filterConfigurationProperties;
    }

    private static boolean isCorsRequest(HttpServletRequest request) {
        return !StringUtils.isEmpty(request.getHeader(HEADER_ORIGIN));
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        String methodName = "doFilter(servletRequest, servletResponse, chain)";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (isCorsRequest(request)) {
            log.debug("{}: {} CORS HTTP Request method: {}", this.getClass().getSimpleName(), methodName, request.getMethod());

            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String originHeader = request.getHeader(HEADER_ORIGIN);
            URI originUri = URI.create(originHeader);

            List<String> allowedOrigins = filterConfigurationProperties.getAllowedOrigins();

            Optional originsFound = allowedOrigins.stream().filter(o -> originUri.getHost().endsWith(o)).findFirst();

            if (!originsFound.isPresent()) {
                log.warn("{}: {} Origin header host [{}] does not match the allowed origins [{}]", this.getClass().getSimpleName(), methodName, originUri.getHost(), allowedOrigins);
                response.setStatus(SC_UNAUTHORIZED);
                return;
            }

            response.addHeader(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, originHeader);
            response.addHeader(HEADER_ACCESS_CONTROL_ALLOW_METHODS, filterConfigurationProperties.getAllowedMethods());
            response.addHeader(HEADER_ACCESS_CONTROL_MAX_AGE, filterConfigurationProperties.getMaxAge());
            response.addHeader(HEADER_ACCESS_CONTROL_ALLOW_HEADERS, filterConfigurationProperties.getAllowedHeaders());
            response.addHeader(HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, String.valueOf(filterConfigurationProperties.isAllowedCredentials()));

            // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
            if (request.getMethod().equals(OPTIONS.name())) {
                response.setStatus(SC_ACCEPTED);
                return;
            }
        }

        // pass the request along the filter chain
        chain.doFilter(request, servletResponse);
    }
}
