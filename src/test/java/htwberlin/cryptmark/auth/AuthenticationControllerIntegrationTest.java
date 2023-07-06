package htwberlin.cryptmark.auth;

import htwberlin.cryptmark.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(
        properties = { "SECRET_JWT_KEY=ePce9nfB7cxeiM07N7bqOo2IQ8sTKxo6w4CIsjaVKNE="}
)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AuthenticationControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private JacksonTester<RegisterRequest> jsonRegisterRequest;

    @Autowired
    private JacksonTester<AuthenticationRequest> jsonAuthenticationRequest;

    @Autowired
    private JacksonTester<AuthenticationResponse> jsonAuthenticationResponse;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        authenticationService.register(RegisterRequest.builder()
                .username("user")
                .password("password")
                .build());
    }

    @Test
    void canRegisterNewUser() throws Exception {
        var res = mvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegisterRequest.write(
                                        RegisterRequest.builder()
                                                .username("newuser")
                                                .seed("seed")
                                                .password("password").build()
                                ).getJson()
                        )
        ).andReturn().getResponse();
        AuthenticationResponse parsedRes = jsonAuthenticationResponse.parse(res.getContentAsString()).getObject();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(parsedRes.getToken()).isNotBlank();
        assertThat(userRepository.findByUsername("newuser")).isPresent();
    }

    @Test
    void canNotRegisterWithExistingUsername() throws Exception {
        var res = mvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegisterRequest.write(
                                RegisterRequest.builder()
                                        .username("user")
                                        .seed("seed")
                                        .password("password").build()
                                ).getJson()
                        )
        ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(res.getContentAsString()).isBlank();
    }

    @Test
    void canAuthenticateWithValidCredentials() throws Exception {
        var res = mvc.perform(
                post("/auth/authenticate")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAuthenticationRequest.write(
                                        AuthenticationRequest.builder()
                                                .username("user")
                                                .password("password").build()
                                ).getJson()
                        )
        ).andReturn().getResponse();
        AuthenticationResponse parsedRes = jsonAuthenticationResponse.parse(res.getContentAsString()).getObject();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(parsedRes.getToken()).isNotBlank();
    }

    @Test
    void canNotAuthenticateWithWrongPassword() throws Exception {
        var res = mvc.perform(
                post("/auth/authenticate")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAuthenticationRequest.write(
                                        AuthenticationRequest.builder()
                                                .username("user")
                                                .password("wrong").build()
                                ).getJson()
                        )
        ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(res.getContentAsString()).isBlank();
    }
}