package pedropires.baskito.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pedropires.baskito.domain.Basket;
import pedropires.baskito.domain.Item;
import pedropires.baskito.dtos.BasketDto;
import pedropires.baskito.dtos.BasketItemDto;
import pedropires.baskito.dtos.ItemDto;
import pedropires.baskito.repositories.IBasketRepository;
import pedropires.baskito.repositories.IItemRepository;

@Service
@AllArgsConstructor
public class BasketService {

    IBasketRepository basketRepository;
    IItemRepository itemRepository;


    public BasketDto getCart(UUID basketId) {
        Optional<Basket> oBasket = basketRepository.findById(basketId);
        if (oBasket.isEmpty()) {
            return null;
        }
        UUID id = oBasket.get().getBasketId();
        String description = oBasket.get().getDescription();
        double totalPrice = oBasket.get().getTotalPrice();
        return new BasketDto(id, description, totalPrice);
    }

    public List<BasketItemDto> getCartItems(UUID basketId) {
        BasketDto basket = getCart(basketId);
        if (basket == null) {
            return null;
        }
        List<Item> itemsResponse = itemRepository.findByBasketId(basketId);
        List<ItemDto> itemDtos = new ArrayList<>();
      

        return List.of(new BasketItemDto(basket, itemDtos));
    }

    public BasketDto createBasket(String description) {
        Basket basket = new Basket(description);
        basketRepository.save(basket);
        return new BasketDto(basket.getBasketId(), basket.getDescription(), basket.getTotalPrice());
    }

    public void addItemToCart() {
        // TODO
    }

    public void removeItemFromCart() {
        // TODO
    }





}
