package ru.skillfactory.banking.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillfactory.banking.handler.ResponseHandler;
import ru.skillfactory.banking.services.BankService;

@RestController
@RequiredArgsConstructor
public class BankController {

    @Autowired
    private final BankService service;

    @GetMapping(path = "/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBalance(@RequestParam("id") long userId) {
        return ResponseHandler.generateDoubleResponse(service.getBalance(userId), HttpStatus.OK);
    }

    @GetMapping("/take")
    public ResponseEntity<Object> takeMoney(@RequestParam("id") long userId, @RequestParam("cash") double cash) {
        return ResponseHandler.generateIntegerResponse(service.takeMoney(userId, cash), HttpStatus.OK);
    }

    @GetMapping("/put")
    public ResponseEntity<Object> putMoney(@RequestParam("id") long userId, @RequestParam("cash") double cash) {
        return ResponseHandler.generateIntegerResponse(service.putMoney(userId, cash), HttpStatus.OK);

    }
}
