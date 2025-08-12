package com.sterling.Push;

import java.util.List;
import java.util.Map;

public interface PushGateway {
    void send(List<String> deviceTokens, String title, String body, Map<String,Object> data);
}
