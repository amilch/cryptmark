package htwberlin.cryptmark.bookmark;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import htwberlin.cryptmark.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonIgnore
    public void setUser(User user) {
        this.user = user;
    }
}
