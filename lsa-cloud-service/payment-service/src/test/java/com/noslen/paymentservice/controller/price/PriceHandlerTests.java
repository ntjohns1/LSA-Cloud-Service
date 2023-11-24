package com.noslen.paymentservice.controller.price;

import com.noslen.paymentservice.dto.PriceDTO;
import com.noslen.paymentservice.service.price.PricingService;
import com.stripe.model.Price;
import com.stripe.model.StripeCollection;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Log4j2
@WebFluxTest
@Import(PriceHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class PriceHandlerTests {

    @MockBean
    private PricingService pricingService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PriceHandler priceHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new PriceEndpointConfig(priceHandler).priceRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldListAllPrices() {

        StripeCollection<Price> prices = createMockPriceCollection();
        when(pricingService.listAllPrices())
                     .thenReturn(Mono.just(prices));

        webTestClient.get()
                .uri("/api/price")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.object")
                .isEmpty()
                .jsonPath("$.data")
                .isNotEmpty();
    }

    @Test
    void shouldRetrievePrice() {
        Price price = createMockPrice("price_123",
                                      5000L,
                                      "Test Price",
                                      "Test Product");
        when(pricingService.retrievePrice(anyString()))
                     .thenReturn(Mono.just(price));

        webTestClient.get()
                .uri("/api/price/price_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(price.getId())
                .jsonPath("$.unitAmount")
                .isEqualTo(price.getUnitAmount())
                .jsonPath("$.lookupKey")
                .isEqualTo(price.getLookupKey())
                .jsonPath("$.product")
                .isEqualTo(price.getProduct());
    }

    @Test
    void shouldCreatePrice() {

        Price price = createMockPrice("price_123",
                                      5000L,
                                      "Test Price",
                                      "Test Product");
        PriceDTO dto = createMockPriceDto("test_key",
                                          true);
        when(pricingService.createPrice(any(PriceDTO.class)))
                     .thenReturn(Mono.just(price));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/price")
                .body(Mono.just(dto),
                      PriceDTO.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();
    }

    @Test
    void shouldUpdatePrice() {

        Price price = createMockPrice("price_123",
                                      5000L,
                                      "updated_key",
                                      "prod_123");
        PriceDTO dto = createMockPriceDto("updated_key",
                                          false);
        when(pricingService.updatePrice(anyString(),
                                        any(PriceDTO.class)))
                     .thenReturn(Mono.empty());
        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/price/price_123")
                .body(Mono.just(dto),
                      PriceDTO.class)
                .exchange()
                .expectStatus()
                .isNoContent();


    }

    private Price createMockPrice(String id, Long unitAmount, String lookupKey, String product) {
        Price price = Mockito.mock(Price.class);
        when(price.getId()).thenReturn(id);
        when(price.getLookupKey()).thenReturn(lookupKey);
        when(price.getProduct()).thenReturn(product);
        when(price.getUnitAmount()).thenReturn(unitAmount);

        return price;
    }

    private PriceDTO createMockPriceDto(String lookupKey, Boolean isActive) {
        return new PriceDTO("price_123",
                                    5000L,
                                    "usd",
                                    lookupKey,
                                    "prod_123",
                                    isActive,
                                    true);

    }

    private StripeCollection<Price> createMockPriceCollection() {
        StripeCollection<Price> prices = new StripeCollection<>();
        Price price1 = createMockPrice("price_123",
                                       5000L,
                                       "key_1",
                                       "prod_123");
        Price price2 = createMockPrice("price_456",
                                       6000L,
                                       "key_2",
                                       "prod_456");

        List<Price> priceList = Arrays.asList(price1,
                                              price2);
        prices.setData(priceList);
        return prices;
    }
}
