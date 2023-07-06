package htwberlin.cryptmark.auth;

import htwberlin.cryptmark.jwt.JwtService;
import htwberlin.cryptmark.user.User;
import htwberlin.cryptmark.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private AuthenticationService subject;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        subject = new AuthenticationService(
                userRepository,
                mock(PasswordEncoder.class),
                jwtService,
                mock(AuthenticationManager.class));
    }
    @Test
    void canNotRegisterWithAnExistingUsername() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(mock(User.class)));

        assertThatThrownBy(() -> subject.register(RegisterRequest.builder().username("user").build()))
                .isInstanceOf(UserAlreadyExistAuthenticationException.class);
    }

    @Test
    void registeringReturnsAJWTToken() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(jwtService.generateToken(any())).thenReturn("jwt");

        AuthenticationResponse res = subject.register(RegisterRequest.builder().username("user").build());

        var arg = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(arg.capture());
        assertThat(res.getToken()).isEqualTo("jwt");
        assertThat(arg.getValue().getUsername()).isEqualTo("user");
    }
}