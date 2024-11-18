package pedropires.baskito.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasketUser {

    public BasketUser(UUID basketId, UUID userId) {
        this.basketId = basketId;
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID basketUserId;

    private UUID basketId;

    private UUID userId;

    @PrePersist
    public void prePersist() {
        if (basketUserId == null) {
            this.basketUserId = userId.toString() + "-" + basketId.toString();
        }
    }



}
