package htwberlin.cryptmark.bookmark;

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
    private String encryptedItemKey;

    @Column
    private String encryptedItem;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    public Bookmark() {}

    public Bookmark(String encryptedItemKey, String encryptedItem, User user) {
        this.encryptedItemKey = encryptedItemKey;
        this.encryptedItem = encryptedItem;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEncryptedItemKey() {
        return encryptedItemKey;
    }

    public void setEncryptedItemKey(String encryptedItemKey) {
        this.encryptedItemKey = encryptedItemKey;
    }

    public String getEncryptedItem() {
        return encryptedItem;
    }

    public void setEncryptedItem(String encryptedItem) {
        this.encryptedItem = encryptedItem;
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
        return Objects.equals(id, bookmark.id) && Objects.equals(encryptedItemKey, bookmark.encryptedItemKey) && Objects.equals(encryptedItem, bookmark.encryptedItem) && Objects.equals(user, bookmark.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, encryptedItemKey, encryptedItem, user);
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "id=" + id +
                ", encryptedKey='" + encryptedItemKey + '\'' +
                ", encryptedContent='" + encryptedItem + '\'' +
                ", user=" + user +
                '}';
    }
}
