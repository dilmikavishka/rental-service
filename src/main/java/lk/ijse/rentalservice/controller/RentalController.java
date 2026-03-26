package lk.ijse.rentalservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lk.ijse.rentalservice.dto.RentalDTO;
import lk.ijse.rentalservice.service.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RentalController {

    private final RentalService rentalService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalDTO> createRental(@Valid @RequestBody RentalDTO dto) {
        log.info("POST /api/v1/rentals - userId: {}, bookId: {}", dto.getUserId(), dto.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalService.createRental(dto));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalDTO> getRental(
            @PathVariable @Positive(message = "Rental ID must be positive") Long id) {
        log.info("GET /api/v1/rentals/{}", id);
        return ResponseEntity.ok(rentalService.getRental(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        log.info("GET /api/v1/rentals - retrieving all rentals");
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping(params = "userId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RentalDTO>> getRentalsByUserId(
            @RequestParam @NotBlank(message = "User ID must not be blank") String userId) {
        log.info("GET /api/v1/rentals?userId={} - retrieving rentals by user", userId);
        return ResponseEntity.ok(rentalService.getRentalsByUserId(userId));
    }

    @GetMapping(params = "bookId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RentalDTO>> getRentalsByBookId(
            @RequestParam @Positive(message = "Book ID must be positive") Long bookId) {
        log.info("GET /api/v1/rentals?bookId={} - retrieving rentals by book", bookId);
        return ResponseEntity.ok(rentalService.getRentalsByBookId(bookId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalDTO> updateRental(
            @PathVariable @Positive(message = "Rental ID must be positive") Long id,
            @Valid @RequestBody RentalDTO dto) {
        log.info("PUT /api/v1/rentals/{}", id);
        return ResponseEntity.ok(rentalService.updateRental(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(
            @PathVariable @Positive(message = "Rental ID must be positive") Long id) {
        log.info("DELETE /api/v1/rentals/{}", id);
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }
}