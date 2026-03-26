package lk.ijse.rentalservice.service.impl;

import lk.ijse.rentalservice.client.BookServiceClient;
import lk.ijse.rentalservice.client.UserServiceClient;
import lk.ijse.rentalservice.dto.BookDTO;
import lk.ijse.rentalservice.dto.RentalDTO;
import lk.ijse.rentalservice.dto.UserDTO;
import lk.ijse.rentalservice.entity.Rental;
import lk.ijse.rentalservice.exception.RentalNotFoundException;
import lk.ijse.rentalservice.mapper.RentalMapper;
import lk.ijse.rentalservice.repository.RentalRepository;
import lk.ijse.rentalservice.service.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserServiceClient userServiceClient;
    private final BookServiceClient bookServiceClient;

    @Override
    @Transactional
    public RentalDTO createRental(RentalDTO dto) {
        log.debug("Creating rental for user: {}, book: {}", dto.getUserId(), dto.getBookId());

        // Fetch user & book details before saving
        UserDTO user = userServiceClient.getUser(dto.getUserId());
        BookDTO book = bookServiceClient.getBook(dto.getBookId());

        Rental rental = rentalMapper.toEntity(dto);
        Rental saved = rentalRepository.save(rental);

        log.info("Rental created successfully with ID: {}", saved.getRentalId());
        return rentalMapper.toDto(saved, user, book);
    }

    @Override
    @Transactional
    public RentalDTO updateRental(Long id, RentalDTO dto) {
        log.debug("Updating rental ID: {}", id);

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));

        UserDTO user = userServiceClient.getUser(dto.getUserId());
        BookDTO book = bookServiceClient.getBook(dto.getBookId());

        rentalMapper.updateEntity(dto, rental);
        Rental updated = rentalRepository.save(rental);

        log.info("Rental updated successfully: {}", id);
        return rentalMapper.toDto(updated, user, book);
    }

    @Override
    @Transactional
    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
        rentalRepository.delete(rental);
        log.info("Rental deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public RentalDTO getRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));

        UserDTO user = userServiceClient.getUser(rental.getUserId());
        BookDTO book = bookServiceClient.getBook(rental.getBookId());

        return rentalMapper.toDto(rental, user, book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(r -> rentalMapper.toDto(
                        r,
                        userServiceClient.getUser(r.getUserId()),
                        bookServiceClient.getBook(r.getBookId())
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getRentalsByUserId(String userId) {
        return rentalRepository.findByUserIdOrderByDateDescRentalIdDesc(userId)
                .stream()
                .map(r -> rentalMapper.toDto(
                        r,
                        userServiceClient.getUser(r.getUserId()),
                        bookServiceClient.getBook(r.getBookId())
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getRentalsByBookId(Long bookId) {
        return rentalRepository.findByBookIdOrderByDateDescRentalIdDesc(bookId)
                .stream()
                .map(r -> rentalMapper.toDto(
                        r,
                        userServiceClient.getUser(r.getUserId()),
                        bookServiceClient.getBook(r.getBookId())
                ))
                .toList();
    }
}