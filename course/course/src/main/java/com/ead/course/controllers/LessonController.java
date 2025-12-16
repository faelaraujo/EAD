package com.ead.course.controllers;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class LessonController {
    final LessonService lessonService;
    final ModuleService moduleService;

    public LessonController(LessonService lessonService, ModuleService moduleService) {
        this.lessonService = lessonService;
        this.moduleService = moduleService;
    }

    @PostMapping("modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonRecordDto lessonRecordDto){
        //moduleService.save(moduleRecordDto, courseService.findById(courseId).get());
        LessonModel lessonModel = lessonService.save(lessonRecordDto, moduleService.findById(moduleId).get());
        return ResponseEntity.status(HttpStatus.CREATED).
                body(lessonModel);
    }

    @GetMapping("modules/{moduleId}/lessons")
    public ResponseEntity<List<LessonModel>> getAllLessonsIntoModule(@PathVariable(value = "moduleId")UUID moduleId){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllLessonsIntoModule(moduleId));

    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson( @PathVariable(value = "moduleId") UUID moduleId,
                                                @PathVariable(value = "lessonId") UUID lessonId){
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        return  ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional);
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){
        lessonService.delete(lessonService.findLessonIntoModule(moduleId,lessonId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonRecordDto lessonRecordDto){
        //moduleService.update(moduleRecordDto, moduleService.findModuleIntoCourse(courseId, moduleId).get());
        return ResponseEntity.status(HttpStatus.OK).body( lessonService.update(lessonRecordDto,
                lessonService.findLessonIntoModule(moduleId, lessonId).get()));
    }

}
