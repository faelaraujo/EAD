package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticatorController {

    Logger logger = LogManager.getLogger(AuthenticatorController.class);
    final UserService userService;

    public AuthenticatorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                               @Validated(UserRecordDTO.UserView.RegistrationPost.class)
                                               @JsonView(UserRecordDTO.UserView.RegistrationPost.class)
                                               UserRecordDTO userRecordDTO){
        logger.debug("POST registerUser userRecordDto received {} ", userRecordDTO);
        if(userService.existsByUsername(userRecordDTO.username())){
            logger.warn("Username {} is Alread Taken", userRecordDTO.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }
        if(userService.existsByEmail(userRecordDTO.email())){
            logger.warn("POST registerUser Email {} is Alread Taken", userRecordDTO.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: E-mail is Already Taken!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDTO));
    }

    @GetMapping("/logs")
    public String index(){
        logger.trace("TRACE");
        logger.debug("DEBUG");
        logger.info("INFO");
        logger.warn("WARNING");
        logger.error("ERROR");
        return "Logging Spring Boot ...";
    }
}
