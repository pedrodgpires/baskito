package pedropires.baskito.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pedropires.baskito.controller.BasketController;
import pedropires.baskito.domain.Basket;
import pedropires.baskito.domain.BasketUser;
import pedropires.baskito.domain.Item;
import pedropires.baskito.domain.User;
import pedropires.baskito.dtos.BasketDto;
import pedropires.baskito.dtos.ItemDto;
import pedropires.baskito.repositories.IBasketRepository;
import pedropires.baskito.repositories.IBasketUserRepository;
import pedropires.baskito.repositories.IItemRepository;
import pedropires.baskito.repositories.IUserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
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
        Basket oBasket = isBasketPresent(basketId);
        UUID userId = isUserAllowedToAccessBasket(basketId);

        // Get all the invited users
        Map<String, String> owners = new HashMap<>();
        List<BasketUser> invitedUsers = basketUserRepository.findInvitedEmailsByBasketId(basketId);
        if (!invitedUsers.isEmpty()) {
            for (BasketUser invitedUser : invitedUsers) {
                UUID otherOwnerId = invitedUser.getBasketId();
                Optional<User> oUser = userRepository.findById(otherOwnerId);
                oUser.ifPresent(user -> owners.put(user.getName(), user.getEmail()));
            }
        }
        // Get all the items in the basket
        List<Item> items = getBasketItems(basketId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.isChecked()));
        }

        BasketDto basketDto = new BasketDto();
        basketDto.setBasketId(basketId);
        basketDto.setDescription(oBasket.getDescription());
        basketDto.setOwners(owners);
        basketDto.setItems(itemDtos);
        return basketDto;
    }

    public BasketDto createBasket(String description, List<String> emailsInvited) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userRepository.findByEmail(userEmail).get().getUserId();
        // Create basket
        Basket basket = new Basket(description, userId);
        Basket savedBasket = basketRepository.save(basket);
        BasketUser basketUser = new BasketUser(savedBasket.getBasketId(), userId);
        basketUserRepository.save(basketUser);
        Map<String, String> invitedEmails = new HashMap<>();
        // Invite users
        for (String email : emailsInvited) {
            Optional<User> oUser = userRepository.findByEmail(email);
            if (oUser.isPresent()) {
                BasketUser invitedUser = new BasketUser();
                invitedUser.setBasketId(savedBasket.getBasketId());
                invitedUser.setUserId(oUser.get().getUserId());
                basketUserRepository.save(invitedUser);
                invitedEmails.put(oUser.get().getName(), oUser.get().getEmail());
            }
            // If the user doesn't exist, we don't do anything
        }
        BasketDto basketDto = new BasketDto();
        basketDto.setBasketId(savedBasket.getBasketId());
        basketDto.setDescription(savedBasket.getDescription());
        basketDto.setOwners(invitedEmails);
        return basketDto;
    }

    public ItemDto addItemToBasket(String description, UUID basketId) {
        Basket oBasket = isBasketPresent(basketId);
        UUID userId = isUserAllowedToAccessBasket(basketId);

        Item item = new Item();
        item.setBasketId(basketId);
        item.setDescription(description);
        itemRepository.save(item);

        return new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.isChecked());
    }

    public boolean removeItemFromBasket(UUID basketId, UUID itemId) {
        Basket oBasket = isBasketPresent(basketId);
        UUID userId = isUserAllowedToAccessBasket(basketId);

        Optional<Item> oItem = itemRepository.findById(itemId);
        if (oItem.isEmpty() || !oItem.get().getBasketId().equals(basketId)) {
            throw new IllegalArgumentException("Item cannot be removed");
        }
        itemRepository.deleteById(itemId);
        return true;
    }

    public ItemDto editItem(UUID basketId, UUID itemId, ItemDto itemDto) {
        Basket oBasket = isBasketPresent(basketId);
        UUID userId = isUserAllowedToAccessBasket(basketId);
        Optional<Item> oItem = itemRepository.findById(itemId);
        if (oItem.isEmpty() || !oItem.get().getBasketId().equals(basketId)) {
            throw new IllegalArgumentException("Item cannot be checked");
        }
        Item item = oItem.get();
        boolean isUpdated = item.update(itemDto);
        if (!isUpdated) {
            throw new IllegalArgumentException("Item was not updated");
        }
        Item updatedItem = itemRepository.save(item);
        return new ItemDto(updatedItem.getItemId(), updatedItem.getBasketId(), updatedItem.getDescription(), updatedItem.isChecked());
    }

    private List<Item> getBasketItems(UUID basketId) {
        List<Item> itemsResponse = itemRepository.findByBasketId(basketId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : itemsResponse) {
            itemDtos.add(new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.isChecked()));
        }
        return itemsResponse;
    }

    private UUID isUserAllowedToAccessBasket(UUID basketId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userRepository.findByEmail(userEmail).get().getUserId();
        Optional<BasketUser> basketUser = basketUserRepository.findByBasketIdAndUserId(basketId, userId);
        if (basketUser.isEmpty()) {
            throw new IllegalArgumentException("User don't have access to this list");
        }
        return userId;
    }

    private Basket isBasketPresent(UUID basketId) {
        Optional<Basket> oBasket = basketRepository.findById(basketId);
        if (oBasket.isEmpty()) {
            throw new IllegalArgumentException("List not found");
        }
        return oBasket.get();
    }


}
