package pedropires.baskito.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasketUser {

    public BasketUser(String basketId, String userId) {
        this.basketId = basketId;
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String basketUserId;

    private String basketId;

    private String userId;



}
