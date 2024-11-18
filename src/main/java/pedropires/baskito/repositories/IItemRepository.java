package pedropires.baskito.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pedropires.baskito.domain.Item;

@Repository
public interface IItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByBasketId(UUID basketId);

}
