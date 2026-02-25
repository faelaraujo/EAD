package com.ead.course.dtos;

import com.ead.course.models.UserModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public record UserEventRecordDto(@JsonProperty("uuid")
                                 UUID userId,
                                 String userName,
                                 String email,
                                 String fullName,
                                 String userStatus,
                                 String userType,
                                 String phoneNumber,
                                 String imageUrl,
                                 String actionType) {

    public UserModel convertToUserModel(){
        var userModel = new UserModel();
        BeanUtils.copyProperties(this, userModel);
        return userModel;
    }


}
