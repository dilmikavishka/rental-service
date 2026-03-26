package lk.ijse.rentalservice.mapper;

import lk.ijse.rentalservice.dto.BookDTO;
import lk.ijse.rentalservice.dto.RentalDTO;
import lk.ijse.rentalservice.dto.UserDTO;
import lk.ijse.rentalservice.entity.Rental;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RentalMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "book", source = "book")
    RentalDTO toDto(Rental rental, UserDTO user, BookDTO book);

    @Mapping(target = "rentalId", ignore = true)
    Rental toEntity(RentalDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rentalId", ignore = true)
    void updateEntity(RentalDTO dto, @MappingTarget Rental rental);
}