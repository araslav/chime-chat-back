package com.petpace.chat.aws.chime.dto;

import com.petpace.chat.aws.chime.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingInfoDto {
    private String meetingId;
//    private Long doctorId;
    private boolean isBusy = false;
    private String mediaRegion;
    private String externalUserId;
    private String joinToken;
    private String audioHostUrl;
    private String signalingUrl;
    private String turnControlUrl;
    private User doctorInfo;
}