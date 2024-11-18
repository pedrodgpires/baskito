package pedropires.baskito.controller;

import lombok.AllArgsConstructor;
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
import pedropires.baskito.dtos.BasketItemDto;
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

    BasketService basketService;

    @GetMapping("/{basketId}")
    public ResponseEntity<BasketDto> getBasket(@PathVariable UUID basketId) {
        BasketDto basket = basketService.getBasket(basketId);
        if (basket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(basket, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BasketDto> createBasket(@RequestBody BasketCreateRequest basketCreateRequest) {
        BasketDto basket = basketService.createBasket(basketCreateRequest.getDescription(), basketCreateRequest.getEmailsInvited());
        if (basket == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(basket, HttpStatus.CREATED);
    }

    @GetMapping("/{basketId}/items")
    public ResponseEntity<BasketItemDto> getCartItems(@PathVariable UUID basketId) {
        BasketItemDto basketItemsDto = basketService.getBasketItems(basketId);
        if (basketItemsDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(basketItemsDto, HttpStatus.OK);
    }

    @PostMapping("/{basketId}/items")
    public ResponseEntity<ItemDto> addItemToCart(@PathVariable UUID basketId, @RequestBody ItemCreateRequest itemCreateRequest) {
        ItemDto itemDto = basketService.addItemToCart(itemCreateRequest.getDescription(), itemCreateRequest.getQuantity(), basketId);
        if (itemDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(itemDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{basketId}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable UUID basketId, @PathVariable UUID itemId) {
        if (!basketService.removeItemFromCart(itemId, basketId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{basketId}/items/{itemId}/check")
    public ResponseEntity<ItemDto> checkItem(@PathVariable UUID basketId, @PathVariable UUID itemId) {
        ItemDto itemDto = basketService.checkItem(basketId, itemId);
        if (itemDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(itemDto, HttpStatus.OK);
    }

}
