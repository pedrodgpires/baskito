package pedropires.baskito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pedropires.baskito.domain.Item;

import java.util.List;

@Repository
public interface IItemRepository extends JpaRepository<Item, String> {

    List<Item> findByBasketId(String basketId);

}
