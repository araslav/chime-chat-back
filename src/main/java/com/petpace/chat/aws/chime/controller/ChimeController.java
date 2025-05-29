package com.petpace.chat.aws.chime.controller;

import com.petpace.chat.aws.chime.dto.MeetingInfoDto;
import com.petpace.chat.aws.chime.dto.JoinMeetingResponse;
import com.petpace.chat.aws.chime.dto.MeetingInfoRequestDto;
import com.petpace.chat.aws.chime.dto.UserRequestDto;
import com.petpace.chat.aws.chime.service.AwsChimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chime/meeting")
public class ChimeController {
    private final AwsChimeService awsChimeService;

    @GetMapping("/")
    public void test() {
        System.out.println("test");
    }

    @PostMapping("/")
    public MeetingInfoDto createMeeting(@RequestBody UserRequestDto doctorRequestDto) {
        return awsChimeService.createMeeting(doctorRequestDto);
    }

    @PostMapping("/join")
    public JoinMeetingResponse joinMeeting(@RequestBody UserRequestDto userRequestDto) {
        return awsChimeService.joinMeeting(userRequestDto);
    }

    @PatchMapping("/{id}")
    public void updateMeeting(@RequestBody MeetingInfoRequestDto meetingInfoRequestDto, @PathVariable String id) {
        log.info("updateMeetingStatus called with meetingInfoRequestDto = {}", meetingInfoRequestDto);
        log.info("updateMeetingStatus called with meetingId = {}", id);
        awsChimeService.updateMeetingStatus(meetingInfoRequestDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteMeeting(@PathVariable String id) {
        awsChimeService.deleteMeeting(id);
    }
}
