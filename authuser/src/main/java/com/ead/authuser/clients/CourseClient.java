package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseRecordDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;


@Component
public class CourseClient {

    Logger logger = LogManager.getLogger(CourseClient.class);

    @Value("${ead.api.url.course}")
    String baseUrlCourse;

    final RestClient restClient;

    public CourseClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Page<CourseRecordDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        String url = baseUrlCourse + "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize()
                + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
        try {

            return restClient.get()
                    .uri(url).
                    retrieve().
                    body(new ParameterizedTypeReference<ResponsePageDTO<CourseRecordDTO>>(){});

        }catch (RestClientException e) {
        logger.error("Error RequestClient with cause: {}",e.getMessage());
        throw  new RuntimeException("Error RequestClient: " + e);
        }
    }
}
