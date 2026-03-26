package lk.ijse.rentalservice.client;

import lk.ijse.rentalservice.dto.BookDTO;
import lk.ijse.rentalservice.exception.BookServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class BookServiceClient {

    private final RestClient restClient;

    public BookServiceClient(@LoadBalanced RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://BOOK-SERVICE")
                .build();
    }

    public BookDTO getBook(Long bookId) {
        log.debug("Fetching book from Book-Service for ID: {}", bookId);
        try {
            return restClient.get()
                    .uri("/api/v1/books/{id}", bookId)
                    .retrieve()
                    .body(BookDTO.class);
        } catch (RestClientException e) {
            log.error("Failed to fetch book details for ID: {}", bookId, e);
            throw new BookServiceException("Unable to retrieve book details for: " + bookId, e);
        }
    }
}