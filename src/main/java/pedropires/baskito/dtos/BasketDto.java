package pedropires.baskito.dtos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasketDto {

    private UUID basketId;

    private String description;

    private Map<String, String> owners;

    private List<ItemDto> items;

}
