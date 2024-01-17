package net.kanzi.kz.service;

import lombok.RequiredArgsConstructor;
import net.kanzi.kz.domain.stock.Ticker;
import net.kanzi.kz.repository.CorpRepository;
import net.kanzi.kz.repository.CorpRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CorpService {
    private final CorpRepositoryImpl corpRepository;
    private final CorpRepository jpaRepository;

    public List<Ticker> findByCode(String code) {
        return corpRepository.findByCode(code);
    }
    public Map findByCodeQ(Map code) {
        return corpRepository.findByCodeQ(code);
    }
    public List<Map<String, Object>> findAllTicker() {
        return corpRepository.findAllTickers();
    }

    public Map insertProxy(List proxy) {
        return corpRepository.insertProxy(proxy);
    }



}
