package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.UserLoginDto;
import byrnes.jonathan.eqprototype.dto.UserRegistrationDto;
import byrnes.jonathan.eqprototype.dto.UserUpdateDto;
import byrnes.jonathan.eqprototype.model.LinkedRole;
import byrnes.jonathan.eqprototype.model.Role;
import byrnes.jonathan.eqprototype.model.User;
import byrnes.jonathan.eqprototype.repository.LinkedRoleRepository;
import byrnes.jonathan.eqprototype.repository.RoleRepository;
import byrnes.jonathan.eqprototype.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LinkedRoleRepository linkedRoleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, LinkedRoleRepository linkedRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.linkedRoleRepository = linkedRoleRepository;
    }

    public User register(UserRegistrationDto userRegistrationDto) {
        Optional<User> userOptional = this.userRepository.findByEmail(userRegistrationDto.getEmail());
        if(userOptional.isPresent()) {
            throw new IllegalArgumentException("This email already exists.");
        }

        Role role = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role("USER")));

        LinkedRole linkedRole = new LinkedRole(role);

        User user = new User(
                linkedRole,
                userRegistrationDto.getEmail(),
                userRegistrationDto.getPassword(),
                new Date(),
                false);

        linkedRole.setUser(user);
        this.linkedRoleRepository.save(linkedRole);
        return this.userRepository.save(user);
    }

    public User login(UserLoginDto userLoginDto) {
        Optional<User> userOptional = this.userRepository.findByEmail(userLoginDto.getEmail());
        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException("This email cannot be found.");
        }

        User user = userOptional.get();
        if(userLoginDto.getEmail().equals(user.getEmail())) {
            if(userLoginDto.getPassword().equals(user.getPassword())) {
                System.out.println("Successfully logged in User: " + user.getEmail());
                return user;
            }
        }
        throw new IllegalArgumentException("Email/Password Incorrect.");
    }

    public User update(String userId, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException("This user cannot be found.");
        }

        User user = userOptional.get();

        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isBlank()){
            user.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isBlank()){
            user.setPassword(userUpdateDto.getPassword());
        }

        return this.userRepository.save(user);
    }
}
