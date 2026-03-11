package com.recruit.system.mapper;

import com.recruit.system.dto.request.RegisterRequest;
import com.recruit.system.dto.response.UserResponse;
import com.recruit.system.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public Users toEntity(RegisterRequest request) {

        Users user = new Users();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public UserResponse toResponse(Users user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        if(user.getRole() != null){
            response.setRole(user.getRole().getName());
        }

        return response;
    }
}