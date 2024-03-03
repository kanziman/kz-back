package net.kanzi.kz.docs;

import net.kanzi.kz.apicontroller.StockController;
import net.kanzi.kz.dto.stock.MarketResponse;
import net.kanzi.kz.dto.stock.TickerQResponse;
import net.kanzi.kz.dto.stock.TickerRequest;
import net.kanzi.kz.dto.stock.TickerResponse;
import net.kanzi.kz.service.stock.CorpService;
import net.kanzi.kz.service.stock.MarketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StockApiDocsTest extends RestDocsSupport{

    private final MarketService marketService = mock(MarketService.class);
    private final CorpService corpService = mock(CorpService.class);

    @Override
    protected Object initController() {
        return new StockController(corpService,marketService);
    }

    @DisplayName("시장 정보 조회가 가능하다.")
    @Test
    public void findAll() throws Exception {
        MarketResponse kospi = MarketResponse.builder()
                .id(1L)
                .baseDate(LocalDateTime.now())
                .mktType("KOSPI")
                .open(2583.8F)
                .high(2682.6F)
                .low(2528.5F)
                .close(2660F)
                .volume(64949800L)
                .amount(3771010L)
                .value(357772992L)
                .per(27.4F)
                .pbr(0.88F)
                .dy(0.63F)
                .credit(8407500L)
                .deposit(43692800L)
                .adr(98.25F)
                .build();

        List<MarketResponse> marketResponseList = List.of(kospi);

        given(marketService.findAll()).willReturn(marketResponseList);

        // when // then
        mockMvc.perform(
                get("/api/stock/market")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(document("market-findList",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                                .description("코드"),
                        fieldWithPath("status").type(JsonFieldType.STRING)
                                .description("상태"),
                        fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("메시지"),
                        fieldWithPath("timeStamp").type(JsonFieldType.STRING)
                                .description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY)
                                .description("응답 데이터"),

                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
                                .description("글 ID"),
                        fieldWithPath("data.[].baseDate").type(JsonFieldType.STRING)
                                .description("글 제목"),
                        fieldWithPath("data.[].mktType").type(JsonFieldType.STRING)
                                .description("글 내용"),
                        fieldWithPath("data.[].open").type(JsonFieldType.NUMBER)
                                .description("글 분류"),
                        fieldWithPath("data.[].high").type(JsonFieldType.NUMBER)
                                .description("글 태그"),
                        fieldWithPath("data.[].high").type(JsonFieldType.NUMBER)
                                .description("유저 아이디"),
                        fieldWithPath("data.[].low").type(JsonFieldType.NUMBER)
                                .description("유저 사진"),
                        fieldWithPath("data.[].close").type(JsonFieldType.NUMBER)
                                .description("유저 별명"),

                        fieldWithPath("data.[].volume").type(JsonFieldType.NUMBER)
                                .description("글 조회수"),
                        fieldWithPath("data.[].amount").type(JsonFieldType.NUMBER)
                                .description("글 좋아요 수"),
                        fieldWithPath("data.[].value").type(JsonFieldType.NUMBER)
                                .description("글 북마크 수"),
                        fieldWithPath("data.[].per").type(JsonFieldType.NUMBER)
                                .description("글 댓글 수"),
                        fieldWithPath("data.[].pbr").type(JsonFieldType.NUMBER)
                                .description("글 작성일"),
                        fieldWithPath("data.[].dy").type(JsonFieldType.NUMBER)
                                .description("글 조회수"),
                        fieldWithPath("data.[].credit").type(JsonFieldType.NUMBER)
                                .description("글 좋아요 수"),
                        fieldWithPath("data.[].deposit").type(JsonFieldType.NUMBER)
                                .description("글 북마크 수"),
                        fieldWithPath("data.[].adr").type(JsonFieldType.NUMBER)
                                .description("글 댓글 수")


                )
        ));

    }

    @DisplayName("특정 종목 정보 조회가 가능하다.")
    @Test
    public void findCorp() throws Exception {
        TickerRequest request = new TickerRequest("005930", "연결재무제표");

        TickerResponse tickerResponse = TickerResponse.builder()
                .id(1L)
                .baseDate(LocalDateTime.now())
                .stockCode("005930")
                .fsType("연결재무제표")
                .per(12.18F)
                .pbr(1.12F)
                .psr(1.54F)
                .por(49.9F)
                .dy(1.1F)
                .value(401766005000000L)
                .shares(5969780224L)
                .volume(12599299L)
                .close(67300L)
                .build();
        TickerQResponse tickerQResponse = TickerQResponse.builder()
                .id(1L)
                .baseDate(LocalDateTime.now())
                .stockCode("005930")
                .fsType("연결재무제표")
                .stockName("삼성전자")
                .account("당기순이익")
                .gongsi("3q")
                .fsName("손익계산서")
                .indicator("")
                .ttm(2956L)
                .shares(27931500L)
                .perShare(10583L)
                .value(67003700000L)
                .close(64544L)
                .build();

        HashMap<String, Object> returnMap = new HashMap<>();
        List<TickerResponse> tickerResponses = List.of(tickerResponse);
        List<TickerQResponse> tickerQResponses = List.of(tickerQResponse);
        returnMap.put("ticker", tickerResponses);
        returnMap.put("tickerQ", tickerQResponses);

        given(corpService.findByCodeQ(any(TickerRequest.class))).willReturn(returnMap);

        // when // then
        mockMvc.perform(
                        post("/api/stock/ticker")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("ticker-findOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("종목코드"),
                                fieldWithPath("option").type(JsonFieldType.STRING)
                                        .description("재무제표 구분")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("timeStamp").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                // ticker
                                fieldWithPath("data.ticker[].id").type(JsonFieldType.NUMBER)
                                        .description("ID"),
                                fieldWithPath("data.ticker[].baseDate").type(JsonFieldType.STRING)
                                        .description("기준일"),
                                fieldWithPath("data.ticker[].stockCode").type(JsonFieldType.STRING)
                                        .description("종목코드"),
                                fieldWithPath("data.ticker[].fsType").type(JsonFieldType.STRING)
                                        .description("재무제표 구분"),
                                fieldWithPath("data.ticker[].per").type(JsonFieldType.NUMBER)
                                        .description("PER"),
                                fieldWithPath("data.ticker[].pbr").type(JsonFieldType.NUMBER)
                                        .description("PBR"),
                                fieldWithPath("data.ticker[].psr").type(JsonFieldType.NUMBER)
                                        .description("PSR"),
                                fieldWithPath("data.ticker[].por").type(JsonFieldType.NUMBER)
                                        .description("POR"),
                                fieldWithPath("data.ticker[].dy").type(JsonFieldType.NUMBER)
                                        .description("DY"),
                                fieldWithPath("data.ticker[].value").type(JsonFieldType.NUMBER)
                                        .description("시가총액"),
                                fieldWithPath("data.ticker[].shares").type(JsonFieldType.NUMBER)
                                        .description("주식수"),
                                fieldWithPath("data.ticker[].volume").type(JsonFieldType.NUMBER)
                                        .description("거래량"),
                                fieldWithPath("data.ticker[].close").type(JsonFieldType.NUMBER)
                                        .description("종가"),
                                //ticker Q
                                fieldWithPath("data.tickerQ[].id").type(JsonFieldType.NUMBER)
                                        .description("ID"),
                                fieldWithPath("data.tickerQ[].baseDate").type(JsonFieldType.STRING)
                                        .description("기준일"),
                                fieldWithPath("data.tickerQ[].stockCode").type(JsonFieldType.STRING)
                                        .description("종목코드"),
                                fieldWithPath("data.tickerQ[].fsType").type(JsonFieldType.STRING)
                                        .description("재무제표 구분"),
                                fieldWithPath("data.tickerQ[].stockName").type(JsonFieldType.STRING)
                                        .description("종목명"),
                                fieldWithPath("data.tickerQ[].account").type(JsonFieldType.STRING)
                                        .description("계정"),
                                fieldWithPath("data.tickerQ[].gongsi").type(JsonFieldType.STRING)
                                        .description("공시구분"),
                                fieldWithPath("data.tickerQ[].fsName").type(JsonFieldType.STRING)
                                        .description("재무제표명"),
                                fieldWithPath("data.tickerQ[].indicator").type(JsonFieldType.STRING)
                                        .description("지표"),
                                fieldWithPath("data.tickerQ[].value").type(JsonFieldType.NUMBER)
                                        .description("시가총액"),
                                fieldWithPath("data.tickerQ[].close").type(JsonFieldType.NUMBER)
                                        .description("종가"),
                                fieldWithPath("data.tickerQ[].ttm").type(JsonFieldType.NUMBER)
                                        .description("직전 4개분기 합"),
                                fieldWithPath("data.tickerQ[].shares").type(JsonFieldType.NUMBER)
                                        .description("주식수"),
                                fieldWithPath("data.tickerQ[].perShare").type(JsonFieldType.NUMBER)
                                        .description("주당 밸류")
                        )
                ));

    }
}


