package com.ead.course.validations;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    Logger logger = LogManager.getLogger(CourseValidator.class);

    final Validator validator;
    final CourseService courseService;
    final UserService userService;

    public CourseValidator(@Qualifier("defaultValidator") Validator validator, CourseService courseService, UserService userService) {
        this.validator = validator;
        this.courseService = courseService;
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseRecordDto courseRecordDto = (CourseRecordDto) o;
        validator.validate(courseRecordDto, errors);
        if (!errors.hasErrors()){
            validateCursoName(courseRecordDto, errors);
            validateUserInstructor(courseRecordDto.userInstructor(), errors);
        }

    }

    private void validateCursoName(CourseRecordDto courseRecordDto, Errors errors){
        if (courseService.existsByName(courseRecordDto.name())){
            errors.rejectValue("name", "courseNameConflict",
                    "Course Name is Already Taken.");
            logger.error("Error: Error validation courseName: {}", courseRecordDto.name());
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors){
        Optional<UserModel> userModelOptional = userService.findbyId(userInstructor);
        if (userModelOptional.get().getUserType().equals(UserType.STUDENT.toString()) ||
            userModelOptional.get().getUserType().equals(UserType.USER.toString())){

            errors.rejectValue("userInstructor", "UserInstructorError",
                    "User must be INSTRUCTOR or ADMIN");
            logger.error("Error: Error validation userInstructors: {}", userInstructor);
        }
    }
}
