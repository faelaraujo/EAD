package com.ead.authuser.validations;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    Logger logger = LogManager.getLogger(UserValidator.class);

    private final Validator validator;
    private final UserService userService;

    public UserValidator(Validator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRecordDTO userRecordDTO = (UserRecordDTO) o;
        validator.validate(userRecordDTO, errors);
        if (!errors.hasErrors()){
            validateUser(userRecordDTO,errors);
            validateEmail(userRecordDTO, errors);
        }
    }

    private void validateUser(UserRecordDTO userRecordDTO, Errors errors){
        if(userService.existsByUsername(userRecordDTO.username())){
            errors.rejectValue("username", "UserNameConflict", "Username is Alread Taken.");
            logger.warn("Username {} is Alread Taken", userRecordDTO.username());
        }

    }
    private void validateEmail(UserRecordDTO userRecordDTO, Errors errors){
        if (userService.existsByEmail(userRecordDTO.email())){
            errors.rejectValue("email", "E-mail Conflict", "Error: Email is Alread Taken." );
            logger.warn("Email {} is Alread Taken", userRecordDTO.email());
        }

    }
}
