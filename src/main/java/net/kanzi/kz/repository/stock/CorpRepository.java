package net.kanzi.kz.repository.stock;

import net.kanzi.kz.domain.stock.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorpRepository extends JpaRepository<Ticker, Long> {
}
