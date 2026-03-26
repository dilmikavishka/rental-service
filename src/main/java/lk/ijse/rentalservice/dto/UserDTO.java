package lk.ijse.rentalservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}
