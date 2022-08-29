package ru.skillfactory.banking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillfactory.banking.model.Operation;
import ru.skillfactory.banking.model.User;
import ru.skillfactory.banking.dto.OperationDto;

@Mapper
public interface OperationMapper {

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    public OperationDto toDto(Operation operation);

    @Named("userToUserId")
    public static long userToUserId(User user) {
        return user.getId();
    }
}
