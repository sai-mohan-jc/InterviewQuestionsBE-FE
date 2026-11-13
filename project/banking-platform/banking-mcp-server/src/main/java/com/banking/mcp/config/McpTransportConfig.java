
package com.banking.mcp.config;

import java.util.Map;

import org.springframework.ai.mcp.server.webmvc.transport.WebMvcStreamableServerTransportProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.modelcontextprotocol.common.McpTransportContext;

@Configuration
public class McpTransportConfig {

    @Bean
    public WebMvcStreamableServerTransportProvider transport() {

        return WebMvcStreamableServerTransportProvider.builder()
                .contextExtractor(serverRequest -> {

                    String authorization = serverRequest.headers()
                            .firstHeader("Authorization");

                    return McpTransportContext.create(
                            Map.of("authorization",
                                    authorization == null ? "" : authorization));

                })
                .build();
    }
}
