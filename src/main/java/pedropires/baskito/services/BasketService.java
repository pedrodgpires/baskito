package pedropires.baskito.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import pedropires.baskito.domain.Basket;
import pedropires.baskito.domain.BasketUser;
import pedropires.baskito.domain.Item;
import pedropires.baskito.domain.User;
import pedropires.baskito.dtos.BasketDto;
import pedropires.baskito.dtos.BasketItemDto;
import pedropires.baskito.dtos.ItemDto;
import pedropires.baskito.repositories.IBasketRepository;
import pedropires.baskito.repositories.IBasketUserRepository;
import pedropires.baskito.repositories.IItemRepository;
import pedropires.baskito.repositories.IUserRepository;

@Service
@AllArgsConstructor
public class BasketService {

    @Autowired
    IBasketRepository basketRepository;
    @Autowired
    IItemRepository itemRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IBasketUserRepository basketUserRepository;


    public BasketDto getBasket(UUID basketId) {
        UUID userId = UUID.randomUUID(); // FUTURE
        Optional<Basket> oBasket = basketRepository.findById(basketId);
        if (oBasket.isEmpty()) {
            return null;
        }
        // Check if user is part of the basket
        BasketUser basketUser = basketUserRepository.findByBasketIdAndUserId(basketId, userId);
        if (basketUser == null) {
            return null;
        }
        // Get all the invited users
        List<String> owners = new ArrayList<>();
        List<BasketUser> invitedUsers = basketUserRepository.findInvitedEmailsByBasketId(basketId);
        if (!invitedUsers.isEmpty()) {
            for (BasketUser invitedUser : invitedUsers) {
                UUID otherOwnerId = invitedUser.getBasketUserId();
                Optional<User> oUser = userRepository.findById(otherOwnerId);
                oUser.ifPresent(user -> owners.add(user.getEmail()));
            }
        }
        // Get all the items in the basket
        // FUTURE
        UUID id = oBasket.get().getBasketId();
        String description = oBasket.get().getDescription();

        return new BasketDto(id, description, owners);
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

    public BasketDto createBasket(String description, List<String> emailsInvited) {
        //Check is emaisl are valid
        List<String> validEmails = new ArrayList<>();
        if (emailsInvited != null && !emailsInvited.isEmpty()) {
            for (String email : emailsInvited) {
                if (userRepository.findByEmail(email).isPresent()) {
                    validEmails.add(email);
                }
            }
        }
        // Get Id from the user that is creating the basket - FUTURE
        UUID ownerId = UUID.randomUUID();
        // Create basket
        Basket basket = new Basket(description, ownerId);
        Basket savedBasket = basketRepository.save(basket);
        // Add users to basket
        if (!validEmails.isEmpty()) {
            UUID basketId = savedBasket.getBasketId();
            for (String email : validEmails) {
                //Send email to {email} with invitation to join basket {description} FUTURE WORK
                UUID userId = userRepository.findByEmail(email).get().getUserId();
                BasketUser bastUser = new BasketUser(userId, basketId);
                basketUserRepository.save(bastUser);
            }
        }
        return new BasketDto(basket.getBasketId(), basket.getDescription(), validEmails);
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
