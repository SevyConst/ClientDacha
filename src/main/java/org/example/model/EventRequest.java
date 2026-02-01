package org.example.model;

import java.util.List;

public record EventRequest(List<Event> events, String deviceId) {}
