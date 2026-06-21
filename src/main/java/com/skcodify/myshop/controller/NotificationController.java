package com.skcodify.myshop.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String userId) {
        SseEmitter emitter = new SseEmitter(900_000L); // 15-minute timeout session
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> {
            log.info("SSE connection completed for user {}.", userId);
            emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.info("SSE connection timed out for user {}.", userId);
            emitters.remove(userId);
        });
        emitter.onError((e) -> {
            log.error("SSE connection error for user {}: {}", userId, e.getMessage());
            emitters.remove(userId);
        });

        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connected successfully!"));
            log.info("User {} subscribed to real-time notifications. Active subscribers: {}", userId, emitters.size());
        } catch (IOException e) {
            emitters.remove(userId);
        }

        return emitter;
    }

    public static void sendNotification(String userId, String eventName, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(message));
                log.info("Successfully sent real-time event '{}' to user {}: {}", eventName, userId, message);
            } catch (IOException e) {
                emitters.remove(userId);
                log.warn("Connection lost for user {}, tearing down SSE emitter.", userId);
            }
        } else {
            log.warn("No active subscription (SseEmitter) found for user {}. Event '{}' not sent.", userId, eventName);
        }
    }
}