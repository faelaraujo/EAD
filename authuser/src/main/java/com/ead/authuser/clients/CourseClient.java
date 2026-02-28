package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseRecordDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import io.github.resilience4j.retry.annotation.Retry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
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

    @Retry(name = "retryInstance", fallbackMethod = "retryfallback")
    public Page<CourseRecordDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        String url = baseUrlCourse + "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize()
                + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
        logger.debug(url);
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

    public Page<CourseRecordDTO> retryfallback(UUID userId, Pageable pageable, Throwable t){
        logger.error("Inside retry retryfallback, cause - {}", t.toString());
        List<CourseRecordDTO> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

   /* public void deleteUserCourseInCourse(UUID userid){
        String url = baseUrlCourse + "/courses/users/"+userid;
        logger.debug("Request URL: {}", url);
        try {
            restClient.delete()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity();

        }catch (RestClientException e){
            logger.error("Error Request DELETE RequestClient with cause: {}",e.getMessage());
            throw  new RuntimeException("Error Request DELETE RequestClient " + e);
        }
    }*/
}
