package com.petpace.chat.aws.chime.service;

import com.petpace.chat.aws.chime.dto.MeetingInfoDto;
import com.petpace.chat.aws.chime.dto.JoinMeetingResponse;
import com.petpace.chat.aws.chime.dto.UserRequestDto;
import com.petpace.chat.aws.chime.entity.User;
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

    public MeetingInfoDto createMeeting(UserRequestDto doctorRequestDto) {
        String joinToken = String.valueOf(doctorRequestDto.hashCode());

        // TODO create bean
        CreateMeetingRequest meetingRequest = CreateMeetingRequest.builder()
                .clientRequestToken(joinToken)
                .externalMeetingId("meeting-" + doctorRequestDto.id())
                .mediaRegion("us-east-1")
                .build();

        CreateMeetingResponse meetingResponse = chimeClient.createMeeting(meetingRequest);

        MeetingInfoDto doctorMeetingInfo = mapToMeetingInfo(doctorRequestDto, meetingResponse.meeting());
        doctorMeetingInfo.setJoinToken(joinToken);

        meetingPoolService.addMeeting(doctorRequestDto.id(), doctorMeetingInfo);
        return doctorMeetingInfo;
    }

    public JoinMeetingResponse joinMeeting(UserRequestDto userRequestDto) {

        // create patient JoinMeetingResponse
        // create doctor  JoinMeetingResponse


        // End

        MeetingInfoDto meetingInfo = meetingPoolService.findFreeMeeting().get();

        User patient = new User(userRequestDto.id(), userRequestDto.name(),
                userRequestDto.name(), userRequestDto.role());

        JoinMeetingResponse doctorMeetingResponse = getMeetingResponse(meetingInfo, meetingInfo.getDoctorInfo());
        JoinMeetingResponse patientMeetingResponse = getMeetingResponse(meetingInfo, patient);

        emitterService.notifyClient(doctorMeetingResponse);
        return patientMeetingResponse;
    }

    private JoinMeetingResponse getMeetingResponse(MeetingInfoDto meetingInfoDto, User user) {
        Attendee attendee = createAttendee(meetingInfoDto.getMeetingId(), user);
        return mapToJoinMeetingResponse(meetingInfoDto, attendee);
    }


    private Attendee createAttendee(String meetingId, User user) {
        CreateAttendeeRequest attendeeRequest = CreateAttendeeRequest.builder()
                .meetingId(meetingId)
                .externalUserId(user.getFirstName() + "_" + user.getLastName() + "-" + user.getId())
                .build();

        CreateAttendeeResponse attendeeResponse = chimeClient.createAttendee(attendeeRequest);
        return attendeeResponse.attendee();
    }

    private MeetingInfoDto mapToMeetingInfo(UserRequestDto userRequestDto, Meeting meeting) {
        MeetingInfoDto meetingInfo = new MeetingInfoDto();
        meetingInfo.setMeetingId(meeting.meetingId());
//        meetingInfo.setDoctorId(userRequestDto.id());
//        meetingInfo.setExternalUserId(String.valueOf(userRequestDto.id())); // User Id (doctor ID)
        meetingInfo.setAudioHostUrl(meeting.mediaPlacement().audioHostUrl());
        meetingInfo.setMediaRegion(meeting.mediaRegion());
        meetingInfo.setSignalingUrl(meeting.mediaPlacement().signalingUrl());
        meetingInfo.setTurnControlUrl(meeting.mediaPlacement().turnControlUrl());
        meetingInfo.setDoctorInfo(new User(userRequestDto.id(), userRequestDto.name(),
                userRequestDto.name(), userRequestDto.role()));
        return meetingInfo;
    }

    private JoinMeetingResponse mapToJoinMeetingResponse(MeetingInfoDto meetingInfoDto,Attendee attendee) {
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
