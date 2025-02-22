package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.UserRegistrationDto;
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

        LinkedRole linkedRole = new LinkedRole(null, role);

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

}
