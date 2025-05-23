package com.petpace.chat.aws.chime.service;

import com.petpace.chat.aws.chime.dto.DoctorMeetingInfo;
import com.petpace.chat.aws.chime.dto.JoinMeetingResponse;
import com.petpace.chat.aws.chime.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;

@RequiredArgsConstructor
@Service
public class AwsChimeService {
    private final ChimeSdkMeetingsClient chimeClient;
    private final MeetingPoolService meetingPoolService;
    private final EmitterService emitterService;

    public DoctorMeetingInfo createMeeting(UserRequestDto doctorRequestDto) {
        String joinToken = String.valueOf(doctorRequestDto.hashCode());

        // TODO create bean
        CreateMeetingRequest meetingRequest = CreateMeetingRequest.builder()
                .clientRequestToken(joinToken)
                .externalMeetingId("meeting-" + doctorRequestDto.id())
                .mediaRegion("us-east-1")
                .build();

        CreateMeetingResponse meetingResponse = chimeClient.createMeeting(meetingRequest);

        DoctorMeetingInfo doctorMeetingInfo = mapToDoctorMeetingInfo(doctorRequestDto, meetingResponse.meeting());
        doctorMeetingInfo.setJoinToken(joinToken);

        meetingPoolService.addMeeting(doctorRequestDto.id(), doctorMeetingInfo);
        return doctorMeetingInfo;
    }

    public JoinMeetingResponse joinMeeting(UserRequestDto userRequestDto) {

        DoctorMeetingInfo doctorMeetingInfo = meetingPoolService.findFreeDoctor().get();

        CreateAttendeeRequest attendeeRequest = CreateAttendeeRequest.builder()
                .meetingId(String.valueOf(doctorMeetingInfo.getMeetingId()))
                .externalUserId(userRequestDto.name() + "-" + userRequestDto.id())
                .build();

        CreateAttendeeResponse attendeeResponse = chimeClient.createAttendee(attendeeRequest);
        Attendee attendee = attendeeResponse.attendee();

        JoinMeetingResponse joinMeetingResponse = mapToJoinMeetingResponse(attendee, doctorMeetingInfo);

        emitterService.notifyClient(doctorMeetingInfo);
        return joinMeetingResponse;
    }

    private DoctorMeetingInfo mapToDoctorMeetingInfo(UserRequestDto userRequestDto, Meeting meeting) {
        DoctorMeetingInfo doctorMeetingInfo = new DoctorMeetingInfo();
        doctorMeetingInfo.setMeetingId(meeting.meetingId());
        doctorMeetingInfo.setDoctorId(userRequestDto.id());
        doctorMeetingInfo.setAudioHostUrl(meeting.mediaPlacement().audioHostUrl());
        doctorMeetingInfo.setMediaRegion(meeting.mediaRegion());
        doctorMeetingInfo.setExternalUserId(String.valueOf(userRequestDto.id()));
        doctorMeetingInfo.setSignalingUrl(meeting.mediaPlacement().signalingUrl());
        doctorMeetingInfo.setTurnControlUrl(meeting.mediaPlacement().turnControlUrl());
        return doctorMeetingInfo;
    }

    private JoinMeetingResponse mapToJoinMeetingResponse(Attendee attendee, DoctorMeetingInfo doctorMeetingInfo) {
        JoinMeetingResponse joinMeetingResponse = new JoinMeetingResponse();
        joinMeetingResponse.setMeetingId(doctorMeetingInfo.getMeetingId());
        joinMeetingResponse.setExternalUserId(attendee.externalUserId());
        joinMeetingResponse.setJoinToken(attendee.joinToken());
        joinMeetingResponse.setAttendeeId(attendee.attendeeId());
        joinMeetingResponse.setMediaRegion(doctorMeetingInfo.getMediaRegion());
        joinMeetingResponse.setAudioHostUrl(doctorMeetingInfo.getAudioHostUrl());
        joinMeetingResponse.setSignalingUrl(doctorMeetingInfo.getSignalingUrl());
        joinMeetingResponse.setTurnControlUrl(doctorMeetingInfo.getTurnControlUrl());
        return joinMeetingResponse;
    }
}
