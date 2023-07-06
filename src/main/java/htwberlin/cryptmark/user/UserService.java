package htwberlin.cryptmark.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repo;

    public Optional<User> getUser(String username) {
        return repo.findByUsername(username);
    }
}
