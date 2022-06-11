package io.github.sonarnext.wave.runner.config;

import org.apache.commons.compress.utils.Lists;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

public class RestClientConfiguration {

    public static RestTemplate restTemplate() {

        return new RestTemplateBuilder()
                .build();
    }

}
