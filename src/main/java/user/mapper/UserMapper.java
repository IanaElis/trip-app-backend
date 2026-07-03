package user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import user.dto.UserDto;
import user.entity.User;

import java.util.List;

@Mapper(componentModel = "cdi", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {

    UserDto toDto(User user);
    List<UserDto> toDto(List<User> users);
}
