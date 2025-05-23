package com.petpace.chat.aws.chime.controller;

import com.petpace.chat.aws.chime.dto.DoctorMeetingInfo;
import com.petpace.chat.aws.chime.dto.JoinMeetingResponse;
import com.petpace.chat.aws.chime.dto.UserRequestDto;
import com.petpace.chat.aws.chime.service.AwsChimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chime")
public class ChimeController {
    private final AwsChimeService awsChimeService;

    @GetMapping("/")
    public void test() {
        System.out.println("test");
    }

    @PostMapping("/create-meeting")
    public DoctorMeetingInfo createMeeting(@RequestBody UserRequestDto doctorRequestDto) {
        return awsChimeService.createMeeting(doctorRequestDto);
    }

    @PostMapping("/join-meeting")
    public JoinMeetingResponse joinMeeting(@RequestBody UserRequestDto userRequestDto) {
        return awsChimeService.joinMeeting(userRequestDto);
    }
}
