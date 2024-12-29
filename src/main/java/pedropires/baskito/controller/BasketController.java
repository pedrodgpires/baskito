package pedropires.baskito.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedropires.baskito.dtos.BasketCreateRequest;
import pedropires.baskito.dtos.BasketDto;
import pedropires.baskito.dtos.BasketSummaryDto;
import pedropires.baskito.dtos.ItemCreateRequestDto;
import pedropires.baskito.dtos.ItemDto;
import pedropires.baskito.dtos.ParticipantDto;
import pedropires.baskito.dtos.ParticipantRequestDto;
import pedropires.baskito.services.BasketService;

import java.util.List;

/**
 * BasketController (/basket)
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basket")
public class BasketController {
    @Autowired
    BasketService basketService;

    @PostMapping("/create")
    public ResponseEntity<BasketDto> createBasket(@RequestBody BasketCreateRequest basketCreateRequest) {
        try {
            BasketDto basket = basketService.createBasket(basketCreateRequest.getDescription(), basketCreateRequest.getEmailsInvited());
            return new ResponseEntity<>(basket, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{basketId}/remove")
    public ResponseEntity<BasketDto> removeBasket(@PathVariable String basketId) {
        try {
            basketService.removeBasket(basketId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<BasketSummaryDto>> getAssociatedBaskets() {
        try {
            List<BasketSummaryDto> baskets = basketService.getAssociatedBaskets();
            return new ResponseEntity<>(baskets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{basketId}/find")
    public ResponseEntity<BasketDto> getBasket(@PathVariable String basketId) {
        try {
            BasketDto basket = basketService.getBasket(basketId);
            return new ResponseEntity<>(basket, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{basketId}/add/participant")
    public ResponseEntity<BasketDto> addParticipantToBasket(@PathVariable String basketId, @RequestBody ParticipantRequestDto participantDto) {
        try {
            BasketDto basket = basketService.addParticipantToBasket(participantDto.getEmail(), basketId);
            return new ResponseEntity<>(basket, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{basketId}/remove/participant")
    public ResponseEntity<BasketDto> removeParticipantFromBasket(@PathVariable String basketId, @RequestBody ParticipantRequestDto participantDto) {
        try {
            basketService.removeParticipantFromBasket(participantDto.getEmail(), basketId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{basketId}/participants")
    public ResponseEntity<List<ParticipantDto>> getParticipants(@PathVariable String basketId) {
        try {
            List<ParticipantDto> participants = basketService.getParticipants(basketId);
            return new ResponseEntity<>(participants, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{basketId}/add/item")
    public ResponseEntity<ItemDto> addItemToCart(@PathVariable String basketId, @RequestBody ItemCreateRequestDto itemCreateRequestDto) {
        try {
            ItemDto itemDto = basketService.addItemToBasket(itemCreateRequestDto.getDescription(), basketId);
            return new ResponseEntity<>(itemDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{basketId}/remove/item/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable String basketId, @PathVariable String itemId) {
        try {
            basketService.removeItemFromBasket(basketId, itemId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{basketId}/update/item/{itemId}")
    public ResponseEntity<ItemDto> checkItem(@PathVariable String basketId, @PathVariable String itemId,
                                             @RequestBody ItemDto itemDto) {
        try {
            ItemDto itemUpdatedDto = basketService.editItem(basketId, itemId, itemDto);
            return new ResponseEntity<>(itemUpdatedDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
