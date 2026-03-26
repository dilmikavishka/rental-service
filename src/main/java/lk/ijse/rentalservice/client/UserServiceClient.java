package lk.ijse.rentalservice.client;

import lk.ijse.rentalservice.dto.UserDTO;
import lk.ijse.rentalservice.exception.UserServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient(@LoadBalanced RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://USER-SERVICE")
                .build();
    }

    public UserDTO getUser(String userId) {
        log.debug("Fetching user from User-Service for ID: {}", userId);
        try {
            return restClient.get()
                    .uri("/api/v1/users/{id}", userId)
                    .retrieve()
                    .body(UserDTO.class);
        } catch (RestClientException e) {
            log.error("Failed to fetch user details for ID: {}", userId, e);
            throw new UserServiceException("Unable to retrieve user details for: " + userId, e);
        }
    }
}