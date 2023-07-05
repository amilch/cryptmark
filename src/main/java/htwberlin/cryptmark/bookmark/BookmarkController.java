package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService service;

    @GetMapping("/bookmarks/{id}")
    public Bookmark getBookmark(@PathVariable Long id){
        return service.getBookmark(id)
                .orElseThrow(() -> new BookmarkNotFoundException(id));
    }

    @GetMapping("/bookmarks")
    public List<Bookmark> getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return service.getAll(user);
    }

    @PostMapping("/bookmarks")
    @Transactional
    public Bookmark createBookmark(@RequestBody Bookmark newBookmark) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        newBookmark.setUser(user);
        return service.createBookmark(newBookmark);
    }

    @PutMapping("/bookmarks")
    @Transactional
    public Optional<Bookmark> updateBookmark(@RequestBody Bookmark newBookmark) {
        return service.updateBookmark(newBookmark);
    }

    @DeleteMapping("/bookmarks/{id}")
    @Transactional
    public void deleteBookmark(@PathVariable Long id) {
        service.deleteBookmark(id);
    }
}
