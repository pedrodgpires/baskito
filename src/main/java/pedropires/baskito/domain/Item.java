package pedropires.baskito.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID itemId;

    private UUID basketId;

    private String description;

    private double price;

    public Item(@NonNull UUID basketId, @NonNull String description, double price) {

        this.basketId = basketId;
        this.description = description;
        this.price = price;
    }
    


}
