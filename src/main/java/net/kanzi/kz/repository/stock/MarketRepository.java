package net.kanzi.kz.repository.stock;

import net.kanzi.kz.domain.stock.KorMarket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<KorMarket, Long> {

}
