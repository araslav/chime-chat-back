package com.petpace.chat.aws.chime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chimesdkidentity.ChimeSdkIdentityClient;
import software.amazon.awssdk.services.chimesdkmessaging.ChimeSdkMessagingClient;

@Configuration
public class ChimeMessagingConfig {
    @Bean
    public ChimeSdkMessagingClient chimeSdkMessagingClient() {
        return ChimeSdkMessagingClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create()) // или ENV переменные
                .build();
    }

    @Bean
    public ChimeSdkIdentityClient chimeIdentityClient() {
        return ChimeSdkIdentityClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }
}