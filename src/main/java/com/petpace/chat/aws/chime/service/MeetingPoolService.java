package com.petpace.chat.aws.chime.service;

import com.petpace.chat.aws.chime.dto.MeetingInfoDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MeetingPoolService {
    private final Map<Long, MeetingInfoDto> meetingMap = new ConcurrentHashMap<>();

    public void addMeeting(Long userId, MeetingInfoDto info) {
        meetingMap.put(userId, info);
    }

    public Optional<MeetingInfoDto> findFreeMeeting() {
        return meetingMap.values().stream()
                .filter(info -> !info.isBusy())
                .findFirst();
    }

    public MeetingInfoDto getByUserId(Long userId) {
        return meetingMap.get(userId);
    }

    public void markDoctorBusy(Long doctorId) {
        if (meetingMap.containsKey(doctorId)) {
            meetingMap.get(doctorId).setBusy(true);
        }
    }

    public void removeMeeting(Long doctorId) {
        meetingMap.remove(doctorId);
    }

    public Map<Long, MeetingInfoDto> getAllMeetings() {
        return meetingMap;
    }
}
