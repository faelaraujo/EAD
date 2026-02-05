package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/instructors")
public class InstructorController {
    
    final UserService userService;


    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid
                                                             InstructorRecordDto instructorRecordDto){
        userService.findById(instructorRecordDto.userId());
        UserModel userModel = userService.registerInstructor(userService.findById(instructorRecordDto.userId()).get());
        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
}
