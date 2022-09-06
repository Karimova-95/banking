package ru.skillfactory.banking.handler;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Map<String, String>> generateDoubleResponse(String cash) {
        Map<String, String> map = new HashMap<>();
        map.put("value", cash);
        return ResponseEntity.ok(map);
    }

    public static ResponseEntity<Map<String, Integer>> generateIntegerResponse(int result) {
        Map<String, Integer> map = new HashMap<>();
        map.put("value", result);
        return ResponseEntity.ok(map);
    }
}
