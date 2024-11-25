package pedropires.baskito.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedropires.baskito.dtos.BasketCreateRequest;
import pedropires.baskito.dtos.BasketDto;
import pedropires.baskito.dtos.ItemCreateRequest;
import pedropires.baskito.dtos.ItemDto;
import pedropires.baskito.services.BasketService;

import java.util.UUID;

/**
 * BasketController (/basket)
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basket")
public class BasketController {
    @Autowired
    BasketService basketService;

    @GetMapping("/{basketId}")
    public ResponseEntity<BasketDto> getBasket(@PathVariable UUID basketId) {
        try {
            BasketDto basket = basketService.getBasket(basketId);
            return new ResponseEntity<>(basket, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<BasketDto> createBasket(@RequestBody BasketCreateRequest basketCreateRequest) {
        try {
            BasketDto basket = basketService.createBasket(basketCreateRequest.getDescription(), basketCreateRequest.getEmailsInvited());
            return new ResponseEntity<>(basket, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{basketId}/add/item")
    public ResponseEntity<ItemDto> addItemToCart(@PathVariable UUID basketId, @RequestBody ItemCreateRequest itemCreateRequest) {
        try {
            ItemDto itemDto = basketService.addItemToBasket(itemCreateRequest.getDescription(), basketId);
            return new ResponseEntity<>(itemDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{basketId}/rm/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable UUID basketId, @PathVariable UUID itemId) {
        try {
            basketService.removeItemFromBasket(basketId, itemId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{basketId}/update/item/{itemId}")
    public ResponseEntity<ItemDto> checkItem(@PathVariable UUID basketId, @PathVariable UUID itemId,
                                             @RequestBody ItemDto itemDto) {
        try {
            ItemDto itemUpdatedDto = basketService.editItem(basketId, itemId, itemDto);
            return new ResponseEntity<>(itemUpdatedDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
