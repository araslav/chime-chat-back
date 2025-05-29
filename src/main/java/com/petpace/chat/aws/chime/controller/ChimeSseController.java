package com.petpace.chat.aws.chime.controller;

import com.petpace.chat.aws.chime.service.EmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chime/meeting")
public class ChimeSseController {

    private final EmitterService emitterService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam String meetingId) {
        return emitterService.subscribe(meetingId);
    }
}