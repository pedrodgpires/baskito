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


    public BasketDto getBasket(UUID basketId) {
        Optional<Basket> oBasket = basketRepository.findById(basketId);
        if (oBasket.isEmpty()) {
            return null;
        }
        UUID id = oBasket.get().getBasketId();
        String description = oBasket.get().getDescription();
        double totalPrice = oBasket.get().getTotalPrice();
        return new BasketDto(id, description, totalPrice);
    }

    public BasketItemDto getBasketItems(UUID basketId) {
        BasketDto basket = getBasket(basketId);
        if (basket == null) {
            return null;
        }
        List<Item> itemsResponse = itemRepository.findByBasketId(basketId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : itemsResponse) {
            itemDtos.add(new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.getQuantity(), item.isChecked()));
        }
        return new BasketItemDto(basket, itemDtos);
    }

    public BasketDto createBasket(String description, Double totalPrice) {
        Basket basket = new Basket(description, totalPrice);
        basketRepository.save(basket);
        return new BasketDto(basket.getBasketId(), basket.getDescription(), basket.getTotalPrice());
    }

    public ItemDto addItemToCart(String description, int quantity, UUID basketId) {
        Optional<Basket> oBasket = basketRepository.findById(basketId);
        if (oBasket.isEmpty()) {
            return null;
        }
        Item item = new Item(basketId, description, quantity);
        itemRepository.save(item);

        return new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.getQuantity(), item.isChecked());
    }

    public boolean removeItemFromCart(UUID basketId, UUID itemId) {
        Optional<Item> oItem = itemRepository.findById(itemId);
        if (oItem.isEmpty() || !oItem.get().getBasketId().equals(basketId)) {
            return false;
        }
        itemRepository.deleteById(itemId);

        return true;
    }

    public ItemDto checkItem(UUID basketId, UUID itemId) {
        Optional<Item> oItem = itemRepository.findById(itemId);
        if (oItem.isEmpty() || !oItem.get().getBasketId().equals(basketId)) {
            return null;
        }
        Item item = oItem.get();
        item.setChecked(true);
        itemRepository.save(item);

        return new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.getQuantity(), item.isChecked());
    }




}
