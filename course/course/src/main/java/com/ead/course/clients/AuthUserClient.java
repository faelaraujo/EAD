package com.ead.course.clients;

import com.ead.course.dtos.CourseUserRecordDto;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserRecordDTO;
import com.ead.course.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class AuthUserClient {
    Logger logger = LogManager.getLogger(AuthUserClient.class);

    @Value("${ead.api.url.authuser}")
    String baseUrlAuthUser;

    final RestClient restClient;


    public AuthUserClient(RestClient.Builder restClientBuilder) {

        this.restClient = restClientBuilder.build();
    }

    public Page<UserRecordDTO> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        String url = baseUrlAuthUser + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize()
                + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
        logger.debug(url);
        try {
            return restClient.get()
                    .uri(url).
                    retrieve().
                    body(new ParameterizedTypeReference<ResponsePageDTO<UserRecordDTO>>(){});

        }catch (RestClientException e) {
            logger.error("Error RequestClient with cause: {}",e.getMessage());
            throw  new RuntimeException("Error RequestClient: " + e);
        }
    }

    public ResponseEntity<UserRecordDTO> getOneUserById(UUID userId){
        String url = baseUrlAuthUser + "/users/"+ userId;
        logger.debug("Request URL: {}", url);

        return restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    logger.error("Error: User not found: {}", userId);
                    throw  new NotFoundException("Error: User not found.");


                })
                .toEntity(UserRecordDTO.class);
    }


    public void postSubscriptionUserinCourse (UUID courseId, UUID userId){
        String url = baseUrlAuthUser + "/users/" + userId + "/courses/subscription";
        logger.debug("POST: Request URL: {}", url);
        try {
            var courseUserRecordDto = new CourseUserRecordDto(courseId, userId);
            restClient.post().
                    uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(courseUserRecordDto)
                    .retrieve()
                    .toBodilessEntity();
        }catch (RestClientException e){
            logger.error("Error Request POST RequestClient with cause: {}",e.getMessage());
            throw  new RuntimeException("Error Request POST RequestClient: " + e);
        }
    }

    public void deleteCourseUserinAuthUser(UUID courseId){
        String url = baseUrlAuthUser + "/users/courses/" + courseId;
        logger.debug("Request URL: {}", url);
        try {
            restClient.delete()
                    .uri(url)
                    .retrieve().
                    toBodilessEntity();
        }catch (RestClientException e){
            logger.error("Error Request DELETE RequestClient with cause: {}",e.getMessage());
            throw  new RuntimeException("Error Request POST RequestClient: " + e);
        }
    }
}
