package com.infera.extension.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
/** Configures outbound HTTP access to the Gemini API. */
public class WebClientConfig {

    /**
     * Supplies a WebClient builder that delegates DNS lookups to the operating
     * system, avoiding Netty resolver timeouts on some VPNs and corporate DNS.
     */
    @Bean
    public WebClient.Builder webClient() {
        // Use the operating system's DNS resolver. The Netty async resolver can
        // time out on some VPN/corporate DNS configurations even when Windows
        // itself can resolve the Gemini host.
        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
