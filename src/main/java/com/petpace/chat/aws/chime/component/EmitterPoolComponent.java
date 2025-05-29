package com.petpace.chat.aws.chime.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmitterPoolComponent {
    private final Map<String, SseEmitter> doctorEmitters = new ConcurrentHashMap<>();

    public void addEmitter(String mittingId, SseEmitter emitter) {
        doctorEmitters.put(mittingId, emitter);
    }

    public SseEmitter getEmitter(String mittingId) {
        return doctorEmitters.get(mittingId);
    }

    public void remove(String mittingId) {
        doctorEmitters.remove(mittingId);
    }
}
