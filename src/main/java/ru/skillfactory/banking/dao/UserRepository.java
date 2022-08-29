package ru.skillfactory.banking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillfactory.banking.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
