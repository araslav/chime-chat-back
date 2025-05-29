package com.petpace.chat.aws.chime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.chimesdkidentity.ChimeSdkIdentityClient;
import software.amazon.awssdk.services.chimesdkidentity.model.CreateAppInstanceUserRequest;
import software.amazon.awssdk.services.chimesdkidentity.model.CreateAppInstanceUserResponse;
import software.amazon.awssdk.services.chimesdkmessaging.ChimeSdkMessagingClient;
import software.amazon.awssdk.services.chimesdkmessaging.model.ChannelMembershipType;
import software.amazon.awssdk.services.chimesdkmessaging.model.ChannelMode;
import software.amazon.awssdk.services.chimesdkmessaging.model.ChannelPrivacy;
import software.amazon.awssdk.services.chimesdkmessaging.model.CreateChannelMembershipRequest;
import software.amazon.awssdk.services.chimesdkmessaging.model.CreateChannelRequest;
import software.amazon.awssdk.services.chimesdkmessaging.model.CreateChannelResponse;

@RequiredArgsConstructor
@Service
public class ChimeMessagingService {

    private final ChimeSdkIdentityClient identityClient;
    private final ChimeSdkMessagingClient messagingClient;

    @Value("${spring.chime.messaging.app-instance-arn}")
    private String appInstanceArn;

    public String createUser(String userId, String name) {
        CreateAppInstanceUserRequest request = CreateAppInstanceUserRequest.builder()
                .appInstanceArn(appInstanceArn)
                .appInstanceUserId(userId)
                .name(name)
                .build();

        CreateAppInstanceUserResponse response = identityClient.createAppInstanceUser(request);
        return response.appInstanceUserArn();
    }

    public String createChannel(String name, String createdByArn) {
        CreateChannelRequest request = CreateChannelRequest.builder()
                .appInstanceArn(appInstanceArn)
                .name(name)
                .mode(ChannelMode.RESTRICTED)
                .privacy(ChannelPrivacy.PRIVATE)
                .chimeBearer(createdByArn)
                .build();

        CreateChannelResponse response = messagingClient.createChannel(request);
        return response.channelArn();
    }

    public void addUserToChannel(String channelArn, String userArn, String adminArn) {
        CreateChannelMembershipRequest request = CreateChannelMembershipRequest.builder()
                .channelArn(channelArn)
                .memberArn(userArn)
                .type(ChannelMembershipType.DEFAULT)
                .chimeBearer(adminArn)
                .build();

        messagingClient.createChannelMembership(request);
    }
}
