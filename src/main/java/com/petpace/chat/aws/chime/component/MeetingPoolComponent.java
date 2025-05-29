package com.petpace.chat.aws.chime.component;

import com.petpace.chat.aws.chime.dto.MeetingInfoDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MeetingPoolComponent {
    private final Map<String, MeetingInfoDto> meetingMap = new ConcurrentHashMap<>();

    public void addMeeting(String meetingId, MeetingInfoDto info) {
        meetingMap.put(meetingId, info);
    }

    public Optional<MeetingInfoDto> findFreeMeeting() {
        return meetingMap.values().stream()
                .filter(info -> !info.isBusy())
                .findFirst();
    }

    public void removeMeeting(String meetingId) {
        meetingMap.remove(meetingId);
    }

    public Map<String, MeetingInfoDto> getAllMeetings() {
        return meetingMap;
    }

    public Optional<MeetingInfoDto> getMeetingInfoByMeetingId(String meetingId) {
        return meetingMap.values().stream()
                .filter(info -> info.getMeetingId().equals(meetingId))
                .findFirst();
    }
}
