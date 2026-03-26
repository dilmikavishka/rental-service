package lk.ijse.rentalservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
//@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    //@Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    //@Column(name = "book_id", nullable = false)
    private Long bookId;

    //@Column(name = "rental_date", nullable = false)
    private LocalDate date;

    //@Column(name = "status", nullable = false, length = 20)
    private String status;
}
