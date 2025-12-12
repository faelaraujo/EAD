package com.ead.course.controllers;

import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ModuleController {
    
    final ModuleService moduleService;
    final CourseService courseService;


    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId")UUID courseId,
                                             @RequestBody @Valid ModuleRecordDto moduleRecordDto){
        //moduleService.save(moduleRecordDto, courseService.findById(courseId).get());
        return ResponseEntity.status(HttpStatus.CREATED).
                body(moduleService.save(moduleRecordDto, courseService.findById(courseId).get()));
    }

    @GetMapping("/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(){
        List<ModuleModel> moduleModels = moduleService.findAll();
        return  ResponseEntity.status(HttpStatus.OK).body(moduleModels);

    }

    @GetMapping("modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "moduleId") UUID moduleId){
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        return  ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional);
    }

    @DeleteMapping("modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "moduleId") UUID moduleId){
        moduleService.delete( moduleService.findById(moduleId).get());
        return ResponseEntity.status(HttpStatus.OK).body("module deleted successfully");
    }
}
