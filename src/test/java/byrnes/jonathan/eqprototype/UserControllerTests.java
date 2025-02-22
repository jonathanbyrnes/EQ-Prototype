package byrnes.jonathan.eqprototype;

import byrnes.jonathan.eqprototype.dto.UserRegistrationDto;
import byrnes.jonathan.eqprototype.repository.LinkedRoleRepository;
import byrnes.jonathan.eqprototype.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkedRoleRepository linkedRoleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void clean() {
        userRepository.findByEmail("registertest@example.com").ifPresent(user -> {
            linkedRoleRepository.delete(user.getLinkedRole());
            userRepository.delete(user);
        });
    }

    @Test
    public void testRegisterUser() throws Exception {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("registertest@example.com");
        registrationDto.setPassword("password123");

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk());
    }

}
