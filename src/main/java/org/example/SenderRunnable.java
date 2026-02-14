package org.example;

import org.example.model.Event;
import org.example.model.EventRequest;
import org.example.model.EventType;

import java.time.LocalDateTime;
import java.util.Collections;

public class SenderRunnable implements Runnable {

    private final HttpSender httpSender;
    private final String deviceId;

    private long messageId = 0;

    public SenderRunnable(HttpSender httpSender, String deviceId) {
        this.httpSender = httpSender;
        this.deviceId = deviceId;
    }

    @Override
    public void run() {
        Event event = new Event(messageId++, EventType.PING, LocalDateTime.now().format(Main.dateTimeFormatter));
        httpSender.send(new EventRequest(Collections.singletonList(event), deviceId));
    }
}
