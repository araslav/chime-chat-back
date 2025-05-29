package com.petpace.chat.aws.chime.service;

import com.petpace.chat.aws.chime.component.EmitterPoolComponent;
import com.petpace.chat.aws.chime.dto.JoinMeetingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class EmitterService {
    private final EmitterPoolComponent emitterPoolService;

    public SseEmitter subscribe(String meetingId) {
        SseEmitter emitter = new SseEmitter(0L); // never timeout
        emitterPoolService.addEmitter(meetingId, emitter);

        emitter.onCompletion(() -> emitterPoolService.remove(meetingId));
        emitter.onTimeout(() -> emitterPoolService.remove(meetingId));

        return emitter;
    }

    public void notifyClient(JoinMeetingResponse doctorMeetingInfo) {
        SseEmitter emitter = emitterPoolService.getEmitter(doctorMeetingInfo.getMeetingId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("patient-joined")
                        .data(doctorMeetingInfo)
                );
            } catch (IOException e) {
                throw new RuntimeException("Something gone wrong!", e);
            }
        }
    }
}
