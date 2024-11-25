package pedropires.baskito.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pedropires.baskito.repositories.IUserRepository;
import java.util.Optional;


@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    IUserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<pedropires.baskito.domain.User> user = repository.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return new User(user.get().getEmail(), user.get().getPassword(), user.get().getAuthorities());
    }
}