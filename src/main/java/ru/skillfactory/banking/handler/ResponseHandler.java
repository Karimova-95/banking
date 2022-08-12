package ru.skillfactory.banking.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateDoubleResponse(String cash, HttpStatus status) {
        Map<String, String> map = new HashMap<>();
        map.put("value", cash);
        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> generateIntegerResponse(int result, HttpStatus status) {
        Map<String, Integer> map = new HashMap<>();
        map.put("value", result);
        return new ResponseEntity<>(map, status);
    }
}
