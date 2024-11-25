package pedropires.baskito.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationRequestDto {

    private String name;
    private String email;
    private String password;


}
