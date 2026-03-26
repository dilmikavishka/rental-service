package lk.ijse.rentalservice.repository;

import lk.ijse.rentalservice.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByUserIdOrderByDateDescRentalIdDesc(String userId);

    List<Rental> findByBookIdOrderByDateDescRentalIdDesc(Long bookId);
}