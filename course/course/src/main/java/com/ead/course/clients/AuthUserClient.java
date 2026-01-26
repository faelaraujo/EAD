package com.ead.course.clients;

import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserRecordDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class AuthUserClient {
    Logger logger = LogManager.getLogger(AuthUserClient.class);

    @Value("${ead.api.url.authuser}")
    String baseUrlCourse;

    final RestClient restClient;


    public AuthUserClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Page<UserRecordDTO> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        String url = baseUrlCourse + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize()
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
}
