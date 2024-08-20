package be.zsoft.zscore.core.controller.external.currency;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyOfferMapper;
import be.zsoft.zscore.core.dto.response.currency.CurrencyOfferResponse;
import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.service.currency.CurrencyOfferService;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import be.zsoft.zscore.core.service.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external/currencies")
public class ExternalCurrencyController {

    private final GameService gameService;
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;
    private final CurrencyOfferService currencyOfferService;
    private final CurrencyOfferMapper currencyOfferMapper;

    @Secured({"ROLE_PLAYER"})
    @GetMapping
    @ResponseBody
    public PaginatedResponse<CurrencyResponse> getCurrencies(Pageable pageable) {
        Game game = gameService.getAuthenicatedGame();
        Page<Currency> currencies = currencyService.getCurrenciesByGame(game, pageable);

        return PaginatedResponse.createResponse(currencyMapper.toResponse(currencies), "/external/currencies");
    }

    @Secured({"ROLE_PLAYER"})
    @GetMapping("/{currencyId}/offers")
    @ResponseBody
    public PaginatedResponse<CurrencyOfferResponse> getCurrencyOffers(@PathVariable UUID currencyId, Pageable pageable) {
        Game game = gameService.getAuthenicatedGame();
        Currency currency = currencyService.getCurrencyById(game, currencyId);
        Page<CurrencyOffer> offers = currencyOfferService.getCurrencyOffersByCurrency(currency, pageable);

        return PaginatedResponse.createResponse(
                currencyOfferMapper.toResponse(offers),
                "/external/currencies/%s/offers".formatted(currencyId)
        );
    }
}
