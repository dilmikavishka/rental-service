package lk.ijse.rentalservice.service;

import lk.ijse.rentalservice.dto.RentalDTO;

import java.util.List;

public interface RentalService {

    RentalDTO createRental(RentalDTO dto);

    RentalDTO updateRental(Long id, RentalDTO dto);

    void deleteRental(Long id);

    RentalDTO getRental(Long id);

    List<RentalDTO> getAllRentals();

    List<RentalDTO> getRentalsByUserId(String userId);

    List<RentalDTO> getRentalsByBookId(Long bookId);
}