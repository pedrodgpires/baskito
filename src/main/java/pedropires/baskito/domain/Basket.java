package pedropires.baskito.domain;

import java.time.Instant;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID basketId;

    private String description;

    private Double totalPrice;

    private Instant createdAt;

    public Basket(@NonNull String description, Double totalPrice) {
        this.description = description;
        this.totalPrice = validateTotalPrice(totalPrice);
        this.createdAt = Instant.now();
    }

    private double validateTotalPrice(Double totalPrice) {
        Double fixedTotalPrice = 0.0;
        if (totalPrice != null && totalPrice >= 0) {
            fixedTotalPrice = totalPrice;
        }
        return fixedTotalPrice;
    }

}
