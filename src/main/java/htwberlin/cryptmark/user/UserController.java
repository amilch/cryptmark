package htwberlin.cryptmark.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return service.getUser(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/{username}/seed")
    public String getUserSeed(@PathVariable String username) {
        return service.getUser(username)
                .orElseThrow(UserNotFoundException::new)
                .getSeed();
    }


}
