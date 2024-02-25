package net.kanzi.kz.repository.stock;

import net.kanzi.kz.domain.stock.KorMarket;
import net.kanzi.kz.data.queryDto.KorTicker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarketRepository extends JpaRepository<KorMarket, Long> {

}
