package ru.skillfactory.banking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillfactory.banking.model.Operation;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    Set<Operation> findOperationByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);
    Set<Operation> findOperationByUserId(Long userId);

}
