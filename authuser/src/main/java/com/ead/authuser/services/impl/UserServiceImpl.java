package com.ead.authuser.services.impl;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.enums.ActionType;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.publishers.UserEventPublisher;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final CourseClient courseClient;
    final UserEventPublisher userEventPublisher;

    public UserServiceImpl(UserRepository userRepository, CourseClient courseClient, UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.courseClient = courseClient;
        this.userEventPublisher = userEventPublisher;
    }

    //@Override
    //public List<UserModel> findAll() {
    //    return userRepository.findAll();
    //}

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        return userRepository.findAll(spec,pageable);
    }


    @Override
    public Optional<UserModel> findById(UUID userId) {
        Optional<UserModel> userModelOptional = userRepository.findById(userId);
        if(userModelOptional.isEmpty()){
            throw new NotFoundException("Error: User not found.");
        }
        return userModelOptional;
    }

    @Transactional
    @Override
    public void delete(UserModel userModel) {
        /*boolean deleteUserCourseinCourse = false;
        List<UserCourseModel> userCourseModelList = userCourseRepository.findAllUserCourseIntoUser(userModel.getUserId());
        if (!userCourseModelList.isEmpty()){
            userCourseRepository.deleteAll(userCourseModelList);
            deleteUserCourseinCourse = true;
        }*/
        userRepository.delete(userModel);
        /*if(deleteUserCourseinCourse){
            courseClient.deleteUserCourseInCourse(userModel.getUserId());
        }*/

    }

    @Transactional
    @Override
    public UserModel registerUser(UserRecordDTO userRecordDTO) {
        var userModel = new UserModel();
        //casting de userRecordDTO para userModel
        BeanUtils.copyProperties(userRecordDTO,userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.save(userModel);
        userEventPublisher.publishUserEvent(userModel.convertToUserEventDto(ActionType.CREATE));
        return userModel;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserModel updateUser(UserRecordDTO userRecordDTO, UserModel userModel) {
        userModel.setFullName(userRecordDTO.fullName());
        userModel.setPhoneNumber(userRecordDTO.phoneNumber());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updatePassword(UserRecordDTO userRecordDTO, UserModel userModel) {
        userModel.setPassword(userRecordDTO.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateImage(UserRecordDTO userRecordDTO, UserModel userModel) {
        userModel.setImgUrl(userRecordDTO.imageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel registerInstructor(UserModel userModel) {
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }


}
