package htwberlin.cryptmark.auth;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistAuthenticationException extends AuthenticationException {
    public UserAlreadyExistAuthenticationException(final String username) {
        super("User " + username + " already exists");
    }
}
