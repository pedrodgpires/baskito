package pedropires.baskito.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pedropires.baskito.domain.Basket;

public interface IBasketRepository extends JpaRepository<Basket, UUID> {
    
}
