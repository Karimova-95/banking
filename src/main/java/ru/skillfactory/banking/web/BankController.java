package ru.skillfactory.banking.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillfactory.banking.services.BankService;

@RestController
@RequiredArgsConstructor
public class BankController {

    @Autowired
    private final BankService service;

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(@RequestParam("id") long userId) {
        return ResponseEntity.accepted().body(service.getBalance(userId));
    }

    @GetMapping("/take")
    public ResponseEntity<Integer> takeMoney(@RequestParam("id") long userId, @RequestParam("cash") double cash) {
        return ResponseEntity.accepted().body(service.takeMoney(userId, cash));
    }

    @GetMapping("/put")
    public ResponseEntity<Integer> putMoney(@RequestParam("id") long userId, @RequestParam("cash") double cash) {
        return ResponseEntity.accepted().body(service.putMoney(userId, cash));

    }
}
