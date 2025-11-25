package com.ead.authuser.services;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.models.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserModel> findAll();

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userModel);

    UserModel registerUser(UserRecordDTO userRecordDTO);

    boolean existsByUsername(String username);

    boolean existsByEmail(String username);

    UserModel updateUser(UserRecordDTO userRecordDTO, UserModel userModel);

    UserModel updatePassword(UserRecordDTO userRecordDTO, UserModel userModel);
}

