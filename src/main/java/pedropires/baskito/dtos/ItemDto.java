package pedropires.baskito.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {

    String itemId;

    String basketId;

    String description;

    Boolean checked;

}
