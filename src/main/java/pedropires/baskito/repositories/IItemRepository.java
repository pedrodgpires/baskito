package pedropires.baskito.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pedropires.baskito.domain.Item;

public interface IItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByBasketId(UUID basketId);

}
