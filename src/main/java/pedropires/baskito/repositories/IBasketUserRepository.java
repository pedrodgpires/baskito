package pedropires.baskito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pedropires.baskito.domain.BasketUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IBasketUserRepository extends JpaRepository<BasketUser, UUID> {

    BasketUser findByBasketId(UUID basketId);
    BasketUser findByUserId(UUID userId);
    Optional<BasketUser> findByBasketIdAndUserId(UUID basketId, UUID userId);
    List<BasketUser> findInvitedEmailsByBasketId(UUID basketId);



}
