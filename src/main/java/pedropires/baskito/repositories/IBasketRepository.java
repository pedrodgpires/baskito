package pedropires.baskito.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pedropires.baskito.domain.Basket;

@Repository
public interface IBasketRepository extends JpaRepository<Basket, UUID> {
    
}
