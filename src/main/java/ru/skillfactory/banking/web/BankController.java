package ru.skillfactory.banking.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillfactory.banking.dto.OperationDto;
import ru.skillfactory.banking.handler.ResponseHandler;
import ru.skillfactory.banking.services.BankService;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class BankController {

    @Autowired
    private final BankService service;

    @GetMapping(path = "/balance/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getBalance(
            @PathVariable("id") long userId) {
        return ResponseHandler.generateDoubleResponse(service.getBalance(userId));
    }

    @GetMapping("/take/{id}/{cash}")
    public ResponseEntity<Map<String, Integer>> takeMoney(
            @PathVariable("id") long userId,
            @PathVariable("cash") double cash) {
        return ResponseHandler.generateIntegerResponse(service.takeMoney(userId, cash, LocalDate.now()));
    }

    @GetMapping("/put/{id}/{cash}")
    public ResponseEntity<Map<String, Integer>> putMoney(
            @PathVariable("id") long userId,
            @PathVariable("cash") double cash) {
        return ResponseHandler.generateIntegerResponse(service.putMoney(userId, cash, LocalDate.now()));
    }

    @GetMapping("/all")
    public Set<OperationDto> getOperationList(
            @RequestParam("id") long userId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getOperationList(userId, from, to);
    }

    @GetMapping("/transfer/{from}/{to}/{cash}")
    public ResponseEntity<Map<String, Integer>> transfer(
            @PathVariable("from") long fromUserId,
            @PathVariable("to") long toUserId,
            @PathVariable("cash") double cash) {
        return ResponseHandler.generateIntegerResponse(service.transfer(fromUserId, toUserId, cash));
    }
}
