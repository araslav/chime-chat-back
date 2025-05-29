package com.petpace.chat.aws.chime.mapper;

import com.petpace.chat.aws.chime.dto.MeetingInfoDto;
import com.petpace.chat.aws.chime.dto.UserRequestDto;
import com.petpace.chat.aws.chime.entity.User;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.chimesdkmeetings.model.Meeting;

@Component
public class MeetingInfoMapper {

    public MeetingInfoDto mapToMeetingInfoDto(UserRequestDto userRequestDto, Meeting meeting) {
        MeetingInfoDto meetingInfo = new MeetingInfoDto();
        meetingInfo.setMeetingId(meeting.meetingId());
        meetingInfo.setAudioHostUrl(meeting.mediaPlacement().audioHostUrl());
        meetingInfo.setMediaRegion(meeting.mediaRegion());
        meetingInfo.setSignalingUrl(meeting.mediaPlacement().signalingUrl());
        meetingInfo.setTurnControlUrl(meeting.mediaPlacement().turnControlUrl());
        meetingInfo.setDoctorInfo(new User(userRequestDto.id(), userRequestDto.name(),
                userRequestDto.name(), userRequestDto.role()));
        return meetingInfo;
    }
}
