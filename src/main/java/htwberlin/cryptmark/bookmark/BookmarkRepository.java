package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
    Optional<Bookmark> findByUserAndId(User user, Long id);
    void deleteByUserAndId(User user, Long id);
}
