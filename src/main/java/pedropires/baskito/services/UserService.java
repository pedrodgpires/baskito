package pedropires.baskito.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pedropires.baskito.domain.User;
import pedropires.baskito.dtos.UserDto;
import pedropires.baskito.repositories.IUserRepository;
import pedropires.baskito.security.TokenService;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public boolean register(String name, String email, String password) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: name, email, or password must not be empty");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User(name, email, bCryptPasswordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        return savedUser != null;
    }

    public UserDto login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email and password must not be empty");
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid email");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            String token = tokenService.generateToken((User) authentication.getPrincipal());
            return new UserDto(userOptional.get().getName(), email, token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}
