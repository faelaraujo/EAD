package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class CourseUserController {

    final CourseService courseService;
    final UserService userService;

    public CourseUserController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }


    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(SpecificationTemplate.UserSpec spec,
                                                      @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                      @PathVariable(value = "courseId") UUID courseId){
        courseService.findById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.
                findAll(SpecificationTemplate.userCourseId(courseId).and(spec),pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserinCourse(@PathVariable(value = "courseId") UUID courseId,
                                                               @RequestBody @Valid SubscriptionRecordDto subscriptionRecordDto){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        //verifications with state transfer

        /*if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionRecordDto.userId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists!");
        }

        ResponseEntity<UserRecordDTO> responseUser = authUserClient.getOneUserById(subscriptionRecordDto.userId());
        if(responseUser.getBody().userStatus().equals(UserStatus.BLOCKED)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User is blocked!");
        }

        CourseUserModel courseUserModel = courseUserService.
                saveAndSendSubscriptionUserInCourse(courseModelOptional.get().
                        convertToCourseUserModel(subscriptionRecordDto.userId()));*/

        return ResponseEntity.status(HttpStatus.CREATED).body(" ");

    }

/*    @DeleteMapping("/courses/users/{userId}")
    public ResponseEntity<Object> deleteCourseUserbyUser(@PathVariable(value = "userId") UUID userId){
        if (!courseUserService.existsByuserId(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser not found!");
        }
        courseUserService.deleteAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body("CourseUser deleted successfully!");
    }*/
}
