package ru.skillfactory.banking.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillfactory.banking.services.BankService;

@RestController
@RequiredArgsConstructor
public class BankController {

    @Autowired
    private final BankService service;

    @GetMapping("/balance")
    public double getBalance(@RequestParam("id") long userId) {
        return service.getBalance(userId);
    }

    @GetMapping("/take")
    public int takeMoney(@RequestParam("id") long userId, @RequestParam("cash") double cash) {
        return service.takeMoney(userId, cash);
    }

    @GetMapping("/put")
    public int putMoney(@RequestParam("id") long userId, @RequestParam("cash") double cash) {
        return service.putMoney(userId, cash);
    }
}
