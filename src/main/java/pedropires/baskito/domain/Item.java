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
import pedropires.baskito.dtos.ItemDto;


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

    private boolean checked;

    public Item(@NonNull UUID basketId, @NonNull String description) {
        this.basketId = basketId;
        this.description = description;
        this.checked = false;
    }

    public boolean update (ItemDto itemDto) {
        boolean updated = false;
        if (itemDto.getDescription() != null && !itemDto.getDescription().equals(this.description)) {
            this.description = itemDto.getDescription();
            updated = true;
        }
        if (itemDto.isChecked() != this.checked) {
            this.checked = itemDto.isChecked();
            updated = true;
        }
        return updated;
    }


}
