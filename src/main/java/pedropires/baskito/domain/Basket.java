package pedropires.baskito.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Basket {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String basketId;

    private String description;

    private Instant createdAt;

    private String ownerId;

    public Basket(@NonNull String description, @NonNull String ownerId) {
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = Instant.now();
    }


}
