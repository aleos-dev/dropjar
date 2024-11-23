package com.aleos.mapper;

import com.aleos.dto.SignUpPayload;
import com.aleos.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * UserMapper is a MapStruct mapper interface used to map between
 * SignUpPayload DTO and User entity.
 * It is responsible for converting SignUpPayload objects to User entities which can then be persisted.
 * This mapper uses Spring's component model for dependency injection and
 * follows a policy to ignore unmapped target properties.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User toEntity(SignUpPayload signUpPayload);

}
