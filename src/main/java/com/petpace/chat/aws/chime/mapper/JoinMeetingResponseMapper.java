package com.petpace.chat.aws.chime.mapper;

import com.petpace.chat.aws.chime.dto.JoinMeetingResponse;
import com.petpace.chat.aws.chime.dto.MeetingInfoDto;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.chimesdkmeetings.model.Attendee;

@Component
public class JoinMeetingResponseMapper {
    public JoinMeetingResponse mapToJoinMeetingResponse(MeetingInfoDto meetingInfoDto, Attendee attendee) {
        JoinMeetingResponse joinMeetingResponse = new JoinMeetingResponse();
        joinMeetingResponse.setMeetingId(meetingInfoDto.getMeetingId());
        joinMeetingResponse.setExternalUserId(attendee.externalUserId());
        joinMeetingResponse.setJoinToken(attendee.joinToken());
        joinMeetingResponse.setAttendeeId(attendee.attendeeId());
        joinMeetingResponse.setMediaRegion(meetingInfoDto.getMediaRegion());
        joinMeetingResponse.setAudioHostUrl(meetingInfoDto.getAudioHostUrl());
        joinMeetingResponse.setSignalingUrl(meetingInfoDto.getSignalingUrl());
        joinMeetingResponse.setTurnControlUrl(meetingInfoDto.getTurnControlUrl());
        return joinMeetingResponse;
    }
}
