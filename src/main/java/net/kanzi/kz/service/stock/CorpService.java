package net.kanzi.kz.service.stock;

import lombok.RequiredArgsConstructor;
import net.kanzi.kz.dto.stock.TickerListResponse;
import net.kanzi.kz.dto.stock.TickerRequest;
import net.kanzi.kz.repository.stock.CorpRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CorpService {
    private final CorpRepositoryImpl corpRepository;

    public Map findByCodeQ(TickerRequest request) {
        return corpRepository.findByCodeQ(request);
    }
    public List<TickerListResponse> findAllTicker() {
        List<Map<String, String>> allTickers = corpRepository.findAllTickers();

        return allTickers.stream()
                .map(t -> TickerListResponse.builder()
                        .stockCode(t.get("stockCode"))
                        .stockName(t.get("stockName"))
                        .mktType(t.get("mktType"))
                        .build())
                .collect(Collectors.toList());
    }

    public Map insertProxy(List proxy) {
        return corpRepository.insertProxy(proxy);
    }



}
