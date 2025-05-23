package com.petpace.chat.aws.chime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;

@Configuration
public class AwsChimeConfig {
    @Bean
    public ChimeSdkMeetingsClient chimeSdkMeetingsClient() {
        return ChimeSdkMeetingsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
