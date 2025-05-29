package com.petpace.chat.aws.chime.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class JoinMeetingResponse {
    private String meetingId;
    private String mediaRegion;
    private String attendeeId;
    private String externalUserId;
    private String joinToken;
    private String audioHostUrl;
    private String signalingUrl;
    private String turnControlUrl;
}