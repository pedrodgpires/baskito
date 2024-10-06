package pedropires.baskito.dtos;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {

    UUID itemId;

    UUID basketId;

    String description;

    int quantity;

    boolean checked;

}
