package com.banking.aiassistant.config;

import java.util.Map;

import org.springframework.ai.mcp.customizer.McpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.common.McpTransportContext;

@Configuration
public class McpClientConfig {

    @Bean
    public McpClientCustomizer<McpClient.SyncSpec> mcpSyncClientCustomizer() {

        return (name, spec) -> spec.transportContextProvider(() -> {

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtAuth) {

                String token = jwtAuth.getToken().getTokenValue();

                return McpTransportContext.create(
                        Map.of("authorization", "Bearer " + token));
            }

            return McpTransportContext.create(Map.of());
        });
    }
    
    @Bean
    public McpClientCustomizer<HttpClientStreamableHttpTransport.Builder>
            mcpHttpTransportCustomizer() {

        return (name, builder) -> builder.httpRequestCustomizer(
                (requestBuilder, method, endpoint, body, context) -> {

                    Object authorization = context.get("authorization");

                    if (authorization != null
                            && !authorization.toString().isBlank()) {

                        requestBuilder.header(
                                "Authorization",
                                authorization.toString());
                    }
                });
    }
}