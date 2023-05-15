package htwberlin.cryptmark.bookmark;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import htwberlin.cryptmark.user.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Bookmark {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String encryptedKey;

    @Column
    private String encryptedContent;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    public Bookmark() {}

    public Bookmark(String encryptedKey, String encryptedContent, User user) {
        this.encryptedKey = encryptedKey;
        this.encryptedContent = encryptedContent;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }

    public void setEncryptedContent(String encryptedContent) {
        this.encryptedContent = encryptedContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return Objects.equals(id, bookmark.id) && Objects.equals(encryptedKey, bookmark.encryptedKey) && Objects.equals(encryptedContent, bookmark.encryptedContent) && Objects.equals(user, bookmark.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, encryptedKey, encryptedContent, user);
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "id=" + id +
                ", encryptedKey='" + encryptedKey + '\'' +
                ", encryptedContent='" + encryptedContent + '\'' +
                ", user=" + user +
                '}';
    }
}
