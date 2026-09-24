package com.devflow.integration;

import com.devflow.config.AppConfig;
import java.util.UUID;

public class JitsiService {

    public static String generateRoomCode(String projectKey, String meetingTitle) {
        String sanitizedKey = projectKey != null ? projectKey.replaceAll("[^a-zA-Z0-9]", "").toLowerCase() : "proj";
        String sanitizedTitle = meetingTitle != null ? meetingTitle.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase() : "meeting";
        if (sanitizedTitle.length() > 20) sanitizedTitle = sanitizedTitle.substring(0, 20);
        String uuidSnippet = UUID.randomUUID().toString().substring(0, 8);
        return "devflow-" + sanitizedKey + "-" + sanitizedTitle + "-" + uuidSnippet;
    }

    public static String buildMeetingUrl(String roomCode) {
        String domain = AppConfig.get("app.jitsi.domain", "meet.jit.si");
        return "https://" + domain + "/" + roomCode;
    }
}
