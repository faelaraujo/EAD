package com.ead.course.controllers;

import com.ead.course.dtos.CourseRecordDto;
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

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable(value = "courseId")UUID courseId){
        List<ModuleModel> moduleModels = moduleService.findAllModulesIntoCourse(courseId);
        return  ResponseEntity.status(HttpStatus.OK).body(moduleModels);

    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule( @PathVariable(value = "courseId") UUID courseId,
                                                @PathVariable(value = "moduleId") UUID moduleId){
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        return  ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional);
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId){
        moduleService.delete( moduleService.findModuleIntoCourse(courseId, moduleId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully");
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleRecordDto moduleRecordDto){
        //moduleService.update(moduleRecordDto, moduleService.findModuleIntoCourse(courseId, moduleId).get());
        return ResponseEntity.status(HttpStatus.OK).body( moduleService.update(moduleRecordDto,
                moduleService.findModuleIntoCourse(courseId, moduleId).get()));
    }
}
