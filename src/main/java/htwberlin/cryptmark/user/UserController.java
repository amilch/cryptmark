package htwberlin.cryptmark.user;

import htwberlin.cryptmark.bookmark.Bookmark;
import htwberlin.cryptmark.bookmark.BookmarkNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
private final UserRepository userRepository;

    @GetMapping("/{username}")
    public User one(@PathVariable String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @GetMapping("/{username}/seed")
    public String seed(@PathVariable String username) {
        return userRepository.findByUsername(username).orElseThrow().getSeed();
    }


}
