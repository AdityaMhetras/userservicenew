package dev.naman.userservicetestfinal.security;

import dev.naman.userservicetestfinal.models.User;
import dev.naman.userservicetestfinal.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //get user from database
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Create UserDetails object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.get().getEmail())
                .password(user.get().getPassword())
                .authorities("USER") // You can customize authorities based on your roles
                .build();
    }
}
