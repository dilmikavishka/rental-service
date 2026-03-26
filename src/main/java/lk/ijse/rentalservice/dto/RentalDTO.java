package lk.ijse.rentalservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalDTO {

    private Long rentalId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Rental date is required")
    private LocalDate date;

    @NotBlank(message = "Status is required")
    private String status;

    private UserDTO user;
    private BookDTO book;
}