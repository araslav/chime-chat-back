package com.petpace.chat.aws.chime.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorMeetingInfo {
    private Long doctorId;
    private String meetingId;
    private boolean busy = false;
    private String mediaRegion;
    private String externalUserId;
    private String joinToken;
    private String audioHostUrl;
    private String signalingUrl;
    private String turnControlUrl;
}