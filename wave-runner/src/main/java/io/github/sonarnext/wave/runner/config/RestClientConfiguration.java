package io.github.sonarnext.wave.runner.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestClientConfiguration {

    public static RestTemplate restTemplate() {

        return new RestTemplateBuilder()
                .build();
    }

}
