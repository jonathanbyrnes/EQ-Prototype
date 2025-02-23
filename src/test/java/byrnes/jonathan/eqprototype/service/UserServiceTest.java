package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.UserRegistrationDto;
import byrnes.jonathan.eqprototype.model.LinkedRole;
import byrnes.jonathan.eqprototype.model.Role;
import byrnes.jonathan.eqprototype.model.User;
import byrnes.jonathan.eqprototype.repository.LinkedRoleRepository;
import byrnes.jonathan.eqprototype.repository.RoleRepository;
import byrnes.jonathan.eqprototype.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private LinkedRoleRepository linkedRoleRepository;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto userDto;

    @BeforeEach
    void setup() {
        userDto = new UserRegistrationDto(
                "example@gmail.com", "password");
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmail(userDto.getEmail()))
                .thenReturn(Optional.empty());

        Role role = new Role("USER");

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.of(role));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        LinkedRole linkedRole = new LinkedRole(role);
        User savedUser = new User(linkedRole, userDto.getEmail(), userDto.getPassword(), new Date(), false);
        linkedRole.setUser(savedUser);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        User result = userService.register(userDto);

        verify(userRepository).save(userCaptor.capture());
        assertThat(result).isNotNull();
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("example@gmail.com");

    }

    @Test
    void testRegisterUser_DuplicateEmail_ShouldThrowException() {
        User existingUser = new User(null, userDto.getEmail(), "password", new Date(), false);

        when(userRepository.findByEmail(userDto.getEmail()))
                .thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userService.register(userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("This email already exists.");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsesExistingRole() {
        when(userRepository.findByEmail(userDto.getEmail()))
                .thenReturn(Optional.empty());

        Role existingRole = new Role("USER");

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.of(existingRole));

        userService.register(userDto);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void testRegisterUser_CreatesNewRoleIfNotFound() {
        when(userRepository.findByEmail(userDto.getEmail()))
                .thenReturn(Optional.empty());

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.empty());

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);

        when(roleRepository.save(any(Role.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.register(userDto);
        verify(roleRepository).save(roleCaptor.capture());

        assertThat(roleCaptor.getValue().getName()).isEqualTo("USER");
    }

    @Test
    void testRegisterUser_LinkedRoleIsSaved() {
        when(userRepository.findByEmail(userDto.getEmail()))
                .thenReturn(Optional.empty());

        Role role = new Role("USER");

        when(roleRepository.findByName("USER")).
                thenReturn(Optional.of(role));

        ArgumentCaptor<LinkedRole> linkedRoleCaptor = ArgumentCaptor.forClass(LinkedRole.class);

        when(linkedRoleRepository.save(any(LinkedRole.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.register(userDto);
        verify(linkedRoleRepository).save(linkedRoleCaptor.capture());

        assertThat(linkedRoleCaptor.getValue().getRole().getName())
                .isEqualTo("USER");
    }


}