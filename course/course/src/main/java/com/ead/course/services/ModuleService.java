package com.ead.course.services;


import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void delete(ModuleModel moduleModel);

    ModuleModel save( ModuleRecordDto moduleRecordDto, CourseModel courseModel);

    List<ModuleModel> findAll();

    Optional<ModuleModel> findById(UUID moduleId);
}
