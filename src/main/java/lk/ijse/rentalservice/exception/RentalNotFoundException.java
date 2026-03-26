package lk.ijse.rentalservice.exception;

public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(Long id) {
        super("Enrollment with ID '" + id + "' not found");
    }
}
