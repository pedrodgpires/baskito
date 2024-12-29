package pedropires.baskito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pedropires.baskito.domain.BasketUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBasketUserRepository extends JpaRepository<BasketUser, String> {

    List<BasketUser> findByUserId(String userId);
    Optional<BasketUser> findByBasketIdAndUserId(String basketId, String userId);
    List<BasketUser> findByBasketId(String basketId);


}
