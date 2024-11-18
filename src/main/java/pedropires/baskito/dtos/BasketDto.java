package pedropires.baskito.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BasketDto {

    private UUID basketId;

    private String description;

    private List<String> invitedEmails;


}
