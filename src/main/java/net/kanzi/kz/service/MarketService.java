package net.kanzi.kz.service;

import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.Market;
import net.kanzi.kz.repository.MarketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MarketService {
    private final MarketRepository jpaRepository;

    public List<Market> findAll() {
        return jpaRepository.findAll();
    }

}
