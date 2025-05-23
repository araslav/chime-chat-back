package com.petpace.chat.aws.chime.controller;

import com.petpace.chat.aws.chime.service.EmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chime")
public class ChimeSseController {

    private final EmitterService emitterService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam String meetingId) {
        return emitterService.subscribe(meetingId);
    }

//    @PostMapping("/notify-patient-joined")
//    public ResponseEntity<Void> notifyPatientJoined(@RequestParam String meetingId, @RequestParam String patientName) {
//        SseEmitter emitter = doctorEmitters.get(meetingId);
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event()
//                    .name("patient-joined")
//                    .data(patientName));
//            } catch (IOException e) {
//                doctorEmitters.remove(meetingId);
//            }
//        }
//        return ResponseEntity.ok().build();
//    }
}