package com.petpace.chat.aws.chime.service;

import com.petpace.chat.aws.chime.component.MeetingPoolComponent;
import com.petpace.chat.aws.chime.dto.MeetingInfoDto;
import com.petpace.chat.aws.chime.dto.JoinMeetingResponse;
import com.petpace.chat.aws.chime.dto.MeetingInfoRequestDto;
import com.petpace.chat.aws.chime.dto.UserRequestDto;
import com.petpace.chat.aws.chime.entity.User;
import com.petpace.chat.aws.chime.mapper.JoinMeetingResponseMapper;
import com.petpace.chat.aws.chime.mapper.MeetingInfoMapper;
import com.petpace.chat.aws.chime.service.impl.AwsRecordingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.Attendee;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateAttendeeRequest;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateAttendeeResponse;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateMeetingRequest;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateMeetingResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsChimeService {
    private final ChimeSdkMeetingsClient chimeClient;
    private final MeetingPoolComponent meetingPoolService;
    private final EmitterService emitterService;
    private final MeetingInfoMapper meetingInfoMapper;
    private final JoinMeetingResponseMapper joinMeetingResponseMapper;
    private final AwsRecordingServiceImpl awsRecordingService;
    private final ChimeMessagingService chimeMessagingService;

    public MeetingInfoDto createMeeting(UserRequestDto doctorRequestDto) {
        String joinToken = String.valueOf(doctorRequestDto.hashCode());

        CreateMeetingResponse meetingResponse = chimeClient.createMeeting(createMeetingRequest(
                joinToken, doctorRequestDto.id()));

        MeetingInfoDto meetingInfo = meetingInfoMapper.mapToMeetingInfoDto(doctorRequestDto, meetingResponse.meeting());
        meetingInfo.setJoinToken(joinToken);

        meetingPoolService.addMeeting(meetingInfo.getMeetingId(), meetingInfo);

        System.out.println(meetingInfo.getMeetingId());
        System.out.println(meetingInfo);
        System.out.println("-----meetingInfo-------");
        return meetingInfo;
    }

    public JoinMeetingResponse joinMeeting(UserRequestDto userRequestDto) {
        MeetingInfoDto meetingInfo = meetingPoolService.findFreeMeeting().orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Didn't find free doctor. Try later!")
        );

        meetingInfo.setBusy(true);

        User patient = new User(userRequestDto.id(), userRequestDto.name(),
                userRequestDto.name(), userRequestDto.role());

        JoinMeetingResponse doctorMeetingResponse = getMeetingResponse(meetingInfo, meetingInfo.getDoctorInfo());
        JoinMeetingResponse patientMeetingResponse = getMeetingResponse(meetingInfo, patient);

        log.info("notify Client before = {}", doctorMeetingResponse);
        emitterService.notifyClient(doctorMeetingResponse);
//        awsRecordingService.startRecording(meetingInfo.getMeetingId());

        return patientMeetingResponse;
    }

    private JoinMeetingResponse getMeetingResponse(MeetingInfoDto meetingInfoDto, User user) {
        Attendee attendee = createAttendee(meetingInfoDto.getMeetingId(), user);
        return  joinMeetingResponseMapper.mapToJoinMeetingResponse(meetingInfoDto, attendee);
    }


    private Attendee createAttendee(String meetingId, User user) {
        CreateAttendeeRequest attendeeRequest = CreateAttendeeRequest.builder()
                .meetingId(meetingId)
                .externalUserId(user.getFirstName() + "_" + user.getLastName() + "-" + user.getId())
                .build();

        CreateAttendeeResponse attendeeResponse = chimeClient.createAttendee(attendeeRequest);
        return attendeeResponse.attendee();
    }

    private CreateMeetingRequest createMeetingRequest (String joinToken, long id) {
         return CreateMeetingRequest.builder()
                .clientRequestToken(joinToken)
                .externalMeetingId("meeting-" + id)
                .mediaRegion(Region.US_EAST_1.toString())
                .build();
    }

    public void updateMeetingStatus(MeetingInfoRequestDto meetingInfoRequestDto, String meetingId) {
        MeetingInfoDto meetingInfoDto = meetingPoolService.getMeetingInfoByMeetingId(meetingId)
                .orElseThrow(
                        () -> new RuntimeException("Meeting with id " + meetingId + " not found"));
        meetingInfoDto.setBusy(meetingInfoRequestDto.isBusy());
        System.out.println(meetingPoolService.getAllMeetings().toString());
    }

    public void deleteMeeting(String id) {
        meetingPoolService.removeMeeting(id);
        System.out.println(meetingPoolService.getAllMeetings().toString());
    }
}
