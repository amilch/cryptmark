package htwberlin.cryptmark.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    JwtService jwtService;

    @Mock
    UserDetailsService userDetailsService;

    JwtAuthenticationFilter subject;

    @BeforeEach
    void setUp() {
        subject = new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Test
    void authenticationShouldBeGrantedWithValidToken() throws ServletException, IOException {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn(List.of());
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtService.extractUsername(any())).thenReturn("user");
        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer token");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        subject.doFilterInternal(req, mock(HttpServletResponse.class), mock(FilterChain.class));

        verify(jwtService).extractUsername("token");
        verify(jwtService).isTokenValid(any(), any());
        verify(securityContext, atLeastOnce()).setAuthentication(any());
    }

    @Test
    void authenticationShouldBeDeniedWithInvalidToken() throws ServletException, IOException {
        when(jwtService.extractUsername(any())).thenReturn("user");
        when(jwtService.isTokenValid(any(), any())).thenReturn(false);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer token");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        subject.doFilterInternal(req, mock(HttpServletResponse.class), mock(FilterChain.class));

        verify(jwtService).extractUsername("token");
        verify(jwtService).isTokenValid(any(), any());
        verify(securityContext, never()).setAuthentication(any());
    }

}