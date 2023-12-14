package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorpRepository extends JpaRepository<Ticker, Long> {

}
