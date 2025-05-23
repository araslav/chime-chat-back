package com.petpace.chat.aws.chime.service;

import com.petpace.chat.aws.chime.dto.DoctorMeetingInfo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MeetingPoolService {
    private final Map<Long, DoctorMeetingInfo> meetingMap = new ConcurrentHashMap<>();

    public void addMeeting(Long doctorId, DoctorMeetingInfo info) {
        meetingMap.put(doctorId, info);
    }

    public Optional<DoctorMeetingInfo> findFreeDoctor() {
        return meetingMap.values().stream()
                .filter(info -> !info.isBusy())
                .findFirst();
    }

    public void markDoctorBusy(Long doctorId) {
        if (meetingMap.containsKey(doctorId)) {
            meetingMap.get(doctorId).setBusy(true);
        }
    }

    public void removeMeeting(Long doctorId) {
        meetingMap.remove(doctorId);
    }

    public Map<Long, DoctorMeetingInfo> getAllMeetings() {
        return meetingMap;
    }
}
