package net.kanzi.kz.service.stock;

import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.stock.KorMarket;
import net.kanzi.kz.domain.stock.Market;
import net.kanzi.kz.dto.stock.MarketResponse;
import net.kanzi.kz.repository.stock.MarketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MarketService {
    private final MarketRepository jpaRepository;

    public List<MarketResponse> findAll() {
        List<KorMarket> all = jpaRepository.findAll();

        return all.stream()
                .map(m -> MarketResponse.of(m))
                .collect(Collectors.toList());
    }

}
