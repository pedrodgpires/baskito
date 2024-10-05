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

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Basket {


    @Id
    private UUID basketId;

    private String description;

    private double totalPrice;

    public Basket(@NonNull String description) {
        this.description = description;
    }


}
