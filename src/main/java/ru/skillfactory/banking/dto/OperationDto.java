package ru.skillfactory.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillfactory.banking.model.OperationType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OperationDto {
    private long userId;
    private double cash;
    private OperationType operationType;
    private LocalDate date;
}
