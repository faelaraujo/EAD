package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.configs.security.WebSecurityConfig;
import com.ead.authuser.dtos.JwtRecordDto;
import com.ead.authuser.dtos.LoginRecordDto;
import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.services.UserService;
import com.ead.authuser.validations.UserValidator;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticatorController {

    Logger logger = LogManager.getLogger(AuthenticatorController.class);
    final UserService userService;
    final UserValidator userValidator;
    final JwtProvider jwtProvider;
    final AuthenticationManager authenticationManager;


    public AuthenticatorController(UserService userService, UserValidator userValidator, WebSecurityConfig webSecurityConfig, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                               @Validated(UserRecordDTO.UserView.RegistrationPost.class)
                                               @JsonView(UserRecordDTO.UserView.RegistrationPost.class)
                                               UserRecordDTO userRecordDTO,
                                               Errors errors){
        logger.debug("POST registerUser userRecordDto received {} ", userRecordDTO);

        // Validação por Custom Validation
        userValidator.validate(userRecordDTO, errors);
        if (errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }

        /*if(userService.existsByUsername(userRecordDTO.username())){
            logger.warn("Username {} is Alread Taken", userRecordDTO.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }
        if(userService.existsByEmail(userRecordDTO.email())){
            logger.warn("POST registerUser Email {} is Alread Taken", userRecordDTO.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: E-mail is Already Taken!");
        }*/
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtRecordDto> authenticateUser(@RequestBody @Valid LoginRecordDto loginRecordDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRecordDto.username(), loginRecordDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new JwtRecordDto(jwt));

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
