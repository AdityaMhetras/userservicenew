package dev.naman.userservicetestfinal.services;

import dev.naman.userservicetestfinal.dtos.UserDto;
import dev.naman.userservicetestfinal.mapper.UserMapper;
import dev.naman.userservicetestfinal.models.Role;
import dev.naman.userservicetestfinal.models.User;
import dev.naman.userservicetestfinal.repositories.RoleRepository;
import dev.naman.userservicetestfinal.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.map(UserMapper.INSTANCE::toDto).orElse(null);

    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        user.setRoles(Set.copyOf(roles));

        User savedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toDto(savedUser);
    }
}
