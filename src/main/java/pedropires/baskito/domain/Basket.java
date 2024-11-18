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

    private Instant createdAt;

    private UUID ownerId;

    public Basket(@NonNull String description, @NonNull UUID ownerId) {
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = Instant.now();
    }


}
