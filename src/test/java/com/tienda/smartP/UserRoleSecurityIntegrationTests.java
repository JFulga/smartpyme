package com.tienda.smartP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.smartP.model.Role;
import com.tienda.smartP.model.User;
import com.tienda.smartP.repository.UserRepository;
import com.tienda.smartP.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:roles;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
})
class UserRoleSecurityIntegrationTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        createExistingUser("admin", Role.ADMIN);
        createExistingUser("vendedor", Role.VENDEDOR);
        createExistingUser("bodega", Role.BODEGA);
    }

    @Test
    void publicRegistrationCreatesVendedor() throws Exception {
        register("publico", "secret").andExpect(status().isOk());

        assertThat(roleOf("publico")).isEqualTo(Role.VENDEDOR);
    }

    @Test
    void publicRegistrationIgnoresSubmittedAdminRole() throws Exception {
        registerWithRole("atacante", "123456", "ADMIN").andExpect(status().isOk());

        assertThat(roleOf("atacante")).isEqualTo(Role.VENDEDOR);
    }

    @Test
    void unauthenticatedUserCreationIsUnauthorized() throws Exception {
        createUser(null, "sin-sesion", Role.VENDEDOR).andExpect(status().isUnauthorized());
    }

    @Test
    void vendedorCannotCreateUsers() throws Exception {
        createUser(tokenFor("vendedor"), "nuevo-vendedor", Role.ADMIN).andExpect(status().isForbidden());
    }

    @Test
    void bodegaCannotCreateUsers() throws Exception {
        createUser(tokenFor("bodega"), "nuevo-bodega", Role.ADMIN).andExpect(status().isForbidden());
    }

    @Test
    void adminCanCreateEverySupportedRole() throws Exception {
        String token = tokenFor("admin");

        createUser(token, "admin-crea-vendedor", Role.VENDEDOR).andExpect(status().isCreated());
        createUser(token, "admin-crea-bodega", Role.BODEGA).andExpect(status().isCreated());
        createUser(token, "admin-crea-admin", Role.ADMIN).andExpect(status().isCreated());

        assertThat(roleOf("admin-crea-vendedor")).isEqualTo(Role.VENDEDOR);
        assertThat(roleOf("admin-crea-bodega")).isEqualTo(Role.BODEGA);
        assertThat(roleOf("admin-crea-admin")).isEqualTo(Role.ADMIN);
    }

    @Test
    void loginStillReturnsUsableJwtForExistingAdmin() throws Exception {
        String loginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(loginResponse).get("token").asText();
        createUser(token, "creado-con-login", Role.VENDEDOR).andExpect(status().isCreated());
        assertThat(roleOf("creado-con-login")).isEqualTo(Role.VENDEDOR);
    }

    private void createExistingUser(String username, Role role) {
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode("password"))
                .role(role)
                .build());
    }

    private Role roleOf(String username) {
        return userRepository.findByUsername(username).orElseThrow().getRole();
    }

    private String tokenFor(String username) {
        return jwtService.generateToken(username);
    }

    private org.springframework.test.web.servlet.ResultActions register(String username, String password) throws Exception {
        return mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"));
    }

    private org.springframework.test.web.servlet.ResultActions registerWithRole(String username, String password, String role) throws Exception {
        return mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"role\":\"" + role + "\"}"));
    }

    private org.springframework.test.web.servlet.ResultActions createUser(String token, String username, Role role) throws Exception {
        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"password\",\"role\":\"" + role + "\"}");
        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        }
        return mockMvc.perform(request);
    }
}
