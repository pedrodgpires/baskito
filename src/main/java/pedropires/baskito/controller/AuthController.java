package pedropires.baskito.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedropires.baskito.domain.User;
import pedropires.baskito.dtos.SignResponseDto;
import pedropires.baskito.dtos.UserLoginRequestDto;
import pedropires.baskito.dtos.UserRegistrationRequestDto;
import pedropires.baskito.infra.security.TokenService;
import pedropires.baskito.repositories.IUserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<SignResponseDto> login(@RequestBody UserLoginRequestDto body){
        User user = this.repository.findByEmail(body.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new SignResponseDto(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity<SignResponseDto> register(@RequestBody UserRegistrationRequestDto body){
        Optional<User> user = this.repository.findByEmail(body.getEmail());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.getPassword()));
            newUser.setEmail(body.getEmail());
            newUser.setName(body.getName());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new SignResponseDto(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
