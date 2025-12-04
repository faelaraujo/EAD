package com.ead.authuser.services;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userModel);

    UserModel registerUser(UserRecordDTO userRecordDTO);

    boolean existsByUsername(String username);

    boolean existsByEmail(String username);

    UserModel updateUser(UserRecordDTO userRecordDTO, UserModel userModel);

    UserModel updatePassword(UserRecordDTO userRecordDTO, UserModel userModel);

    UserModel updateImage(UserRecordDTO userRecordDTO, UserModel userModel);

    //Page<UserModel> findAll(Pageable pageable);
}

