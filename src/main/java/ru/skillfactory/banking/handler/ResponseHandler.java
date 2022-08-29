package ru.skillfactory.banking.handler;

import org.springframework.http.ResponseEntity;
import ru.skillfactory.banking.model.Operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public static ResponseEntity<Map<Long, Operation>> generateOperationsList(Set<Operation> operations) {
        Map<Long, Operation> map = new HashMap<>();
        for (Operation o : operations) {
            map.put(o.getId(), o);
        }
        return ResponseEntity.ok(map);
    }
}
