package ru.skillfactory.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.skillfactory.banking.model.OperationType;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class OperationDto {
    private long userId;
    private double cash;
    private OperationType operationType;
    private LocalDate date;
}
