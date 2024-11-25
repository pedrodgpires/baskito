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
        try {
            boolean registered = userService.register(userRegistrationRequestDto.getName(),
                    userRegistrationRequestDto.getEmail(), userRegistrationRequestDto.getPassword());
            if (registered) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login (@RequestBody UserLoginRequestDto userLoginRequestDto){
        try {
            UserDto userDto = userService.login(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword());
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
