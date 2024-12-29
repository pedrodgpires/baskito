package pedropires.baskito.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pedropires.baskito.domain.Basket;
import pedropires.baskito.domain.BasketUser;
import pedropires.baskito.domain.Item;
import pedropires.baskito.domain.User;
import pedropires.baskito.dtos.BasketDto;
import pedropires.baskito.dtos.BasketSummaryDto;
import pedropires.baskito.dtos.ItemDto;
import pedropires.baskito.dtos.ParticipantDto;
import pedropires.baskito.repositories.IBasketRepository;
import pedropires.baskito.repositories.IBasketUserRepository;
import pedropires.baskito.repositories.IItemRepository;
import pedropires.baskito.repositories.IUserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public BasketDto getBasket(String basketId) {
        if(!isAllowedToAccessBasket(basketId)) {
            throw new IllegalArgumentException("User don't have access");
        }
        Basket oBasket = isBasketPresent(basketId);
        // Get all the invited users
        Map<String, String> owners = new HashMap<>();
        List<BasketUser> invitedUsers = basketUserRepository.findByBasketId(basketId);
        if (!invitedUsers.isEmpty()) {
            for (BasketUser invitedUser : invitedUsers) {
                String otherOwnerId = invitedUser.getBasketId();
                Optional<User> oUser = userRepository.findById(otherOwnerId);
                oUser.ifPresent(user -> owners.put(user.getName(), user.getEmail()));
            }
        }
        // Get all the items in the basket
        List<Item> items = getBasketItems(basketId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.getChecked()));
        }

        BasketDto basketDto = new BasketDto();
        basketDto.setBasketId(basketId);
        basketDto.setDescription(oBasket.getDescription());
        basketDto.setOwners(owners);
        basketDto.setItems(itemDtos);
        return basketDto;
    }

    public List<BasketSummaryDto> getAssociatedBaskets() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String userId = userRepository.findByEmail(userEmail).get().getUserId();
        List<BasketUser> basketUsers = basketUserRepository.findByUserId(userId);
        List<BasketSummaryDto> basketSummaryDtos = new ArrayList<>();
        for (BasketUser basketUser : basketUsers) {
            Optional<Basket> oBasket = basketRepository.findById(basketUser.getBasketId());
            if (oBasket.isPresent()) {
                Basket basket = oBasket.get();
                BasketSummaryDto basketSummaryDto = new BasketSummaryDto(basket.getBasketId(), basket.getDescription(), basket.getCreatedAt());
                basketSummaryDtos.add(basketSummaryDto);
            }
        }
        return basketSummaryDtos;
    }

    public BasketDto createBasket(String description, List<String> emailsInvited) {
        BasketDto basketDto = new BasketDto();
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String userId = userRepository.findByEmail(userEmail).get().getUserId();
        // Create basket
        Basket basket = new Basket(description, userId);
        Basket savedBasket = basketRepository.save(basket);
        BasketUser basketUser = new BasketUser(savedBasket.getBasketId(), userId);
        basketUserRepository.save(basketUser);
        if (emailsInvited != null && !emailsInvited.isEmpty()) {
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
            basketDto.setOwners(invitedEmails);
        }
        basketDto.setBasketId(savedBasket.getBasketId());
        basketDto.setDescription(savedBasket.getDescription());
        return basketDto;
    }

    public boolean removeBasket(String basketId) {
        if (!isOwnerOfBasket(basketId)) {
            throw new IllegalArgumentException("User don't have access to remove this list");
        }
        Optional<Basket> oBasket = basketRepository.findById(basketId);
        if (oBasket.isEmpty()) {
            throw new IllegalArgumentException("List not found");
        }
        basketRepository.deleteById(basketId);
        return true;
    }


    public ItemDto addItemToBasket(String description, String basketId) {
        if(!isAllowedToAccessBasket(basketId)) {
            throw new IllegalArgumentException("User don't have access");
        }
        Item item = new Item(basketId, description);
        itemRepository.save(item);

        return new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.getChecked());
    }

    public boolean removeItemFromBasket(String basketId, String itemId) {
        if(!isAllowedToAccessBasket(basketId)) {
            throw new IllegalArgumentException("User don't have access to this list");
        }
        Optional<Item> oItem = itemRepository.findById(itemId);
        if (oItem.isEmpty() || !oItem.get().getBasketId().equals(basketId)) {
            throw new IllegalArgumentException("Item cannot be removed");
        }
        itemRepository.deleteById(itemId);
        return true;
    }

    public ItemDto editItem(String basketId, String itemId, ItemDto itemDto) {
        if(!isAllowedToAccessBasket(basketId)) {
            throw new IllegalArgumentException("User don't have access");
        }
        Optional<Item> oItem = itemRepository.findById(itemId);
        if (oItem.isEmpty() || !oItem.get().getBasketId().equals(basketId)) {
            throw new IllegalArgumentException("Item cannot be updated");
        }
        Item item = oItem.get();
        boolean isUpdated = item.update(itemDto.getDescription(), itemDto.getChecked());
        if (!isUpdated) {
            throw new IllegalArgumentException("Item was not updated");
        }
        Item updatedItem = itemRepository.save(item);
        return new ItemDto(updatedItem.getItemId(), updatedItem.getBasketId(), updatedItem.getDescription(), updatedItem.getChecked());
    }

    private boolean isAllowedToAccessBasket(String basketId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String userId = userRepository.findByEmail(userEmail).get().getUserId();
        Optional<BasketUser> basketUser = basketUserRepository.findByBasketIdAndUserId(basketId, userId);
        return basketUser.isPresent();
    }
    private boolean isOwnerOfBasket(String basketId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String userId = userRepository.findByEmail(userEmail).get().getUserId();
        Optional<Basket> basket = basketRepository.findById(basketId);
        if(basket.isEmpty()) {
            throw new IllegalArgumentException("List not found");
        }
        String ownerId = basket.get().getOwnerId();
        return ownerId.equals(userId);
    }

    private List<Item> getBasketItems(String basketId) {
        List<Item> itemsResponse = itemRepository.findByBasketId(basketId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : itemsResponse) {
            itemDtos.add(new ItemDto(item.getItemId(), item.getBasketId(), item.getDescription(), item.getChecked()));
        }
        return itemsResponse;
    }

    private Basket isBasketPresent(String basketId) {
        Optional<Basket> oBasket = basketRepository.findById(basketId);
        if (oBasket.isEmpty()) {
            throw new IllegalArgumentException("List not found");
        }
        return oBasket.get();
    }


    public BasketDto addParticipantToBasket(String email, String basketId) {
        if(!isAllowedToAccessBasket(basketId)) {
            throw new IllegalArgumentException("User don't have access");
        }
        Optional<User> oUser = userRepository.findByEmail(email);
        if (oUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        String userId = oUser.get().getUserId();
        if (isAlreadyParticipant(userId, basketId)) {
            throw new IllegalArgumentException("User already in the list");
        }
        BasketUser basketUser = new BasketUser();
        basketUser.setBasketId(basketId);
        basketUser.setUserId(oUser.get().getUserId());
        basketUserRepository.save(basketUser);
        return getBasket(basketId);
    }

    private boolean isAlreadyParticipant(String userId, String basketId) {
        Optional<BasketUser> oBasketUser = basketUserRepository.findByBasketIdAndUserId(basketId, userId);
        return oBasketUser.isPresent();
    }

    public boolean removeParticipantFromBasket(String email, String basketId) {
        if (!isOwnerOfBasket(basketId)) {
            throw new IllegalArgumentException("User don't have access");
        }
        String requestUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (requestUserEmail.equals(email)) {
            throw new IllegalArgumentException("User cannot remove itself");
        }
        Optional<User> oUser = userRepository.findByEmail(email);
        if (oUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        String userId = oUser.get().getUserId();

        Optional<BasketUser> oBasketUser = basketUserRepository.findByBasketIdAndUserId(basketId, oUser.get().getUserId());
        if (oBasketUser.isEmpty()) {
            throw new IllegalArgumentException("User not found in the list");
        }
        basketUserRepository.delete(oBasketUser.get());
        return true;
    }

    public List<ParticipantDto> getParticipants(String basketId) {
        if(!isAllowedToAccessBasket(basketId)) {
            throw new IllegalArgumentException("User don't have access");
        }
        List<BasketUser> basketUsers = basketUserRepository.findByBasketId(basketId);
        List<ParticipantDto> participants = new ArrayList<>();
        for (BasketUser basketUser : basketUsers) {
            Optional<User> oUser = userRepository.findById(basketUser.getUserId());
            oUser.ifPresent(user -> participants.add(new ParticipantDto(user.getName(), user.getEmail())));
        }
        return participants;
    }
}
