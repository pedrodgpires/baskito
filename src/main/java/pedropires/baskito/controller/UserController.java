package pedropires.baskito.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedropires.baskito.dtos.UserDto;
import pedropires.baskito.dtos.UserLoginRequestDto;
import pedropires.baskito.dtos.UserRegistrationRequestDto;
import pedropires.baskito.repositories.IUserRepository;
import pedropires.baskito.services.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    IUserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
        boolean registered = userService.register(userRegistrationRequestDto.getName(),
                userRegistrationRequestDto.getEmail(), userRegistrationRequestDto.getPassword());
        if (!registered) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        UserDto userLoginResponseDto = userService.login(
                userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword());
        if (userLoginResponseDto == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(userLoginResponseDto, HttpStatus.OK);
        }
}
