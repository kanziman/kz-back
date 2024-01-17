package net.kanzi.kz.repository.stock;

import net.kanzi.kz.domain.stock.Ticker;
import net.kanzi.kz.dto.stock.TickerListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CorpRepository extends JpaRepository<Ticker, Long> {

//    @Query("select new net.kanzi.kz.dto.stock.TickerListResponse(t.종목코드, t.종목명, t.시장구분) " +
//            "from kor_ticker_today t ")
//    List<TickerListResponse> findTickerList();



}
