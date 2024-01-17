package net.kanzi.kz.repository;

import net.kanzi.kz.domain.stock.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {

}
