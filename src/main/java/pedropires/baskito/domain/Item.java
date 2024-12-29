package pedropires.baskito.domain;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String itemId;

    private String basketId;

    private String description;

    private Boolean checked;

    public Item(@NonNull String basketId, @NonNull String description) {
        this.basketId = basketId;
        this.description = description;
        this.checked = false;
    }

    public boolean update (String description, Boolean checked) {
        boolean updated = false;
        if (description != null && !description.equals(this.description)) {
            this.description = description;
            updated = true;
        }
        if (checked != null && !checked.equals(this.checked)) {
            this.checked = checked;
            updated = true;
        }
        return updated;
    }


}
