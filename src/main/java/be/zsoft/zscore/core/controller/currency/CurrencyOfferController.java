package be.zsoft.zscore.core.controller.currency;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyOfferMapper;
import be.zsoft.zscore.core.dto.request.currency.CurrencyOfferRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyOfferResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.service.currency.CurrencyOfferService;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import be.zsoft.zscore.core.service.game.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games/{gameId}/currencies/{currencyId}/offers")
public class CurrencyOfferController {

    private final GameService gameService;
    private final CurrencyService currencyService;
    private final CurrencyOfferMapper currencyOfferMapper;
    private final CurrencyOfferService currencyOfferService;

    @PostMapping
    @Secured("ROLE_USER")
    public CurrencyOfferResponse createCurrencyOffer(
            @PathVariable UUID gameId, @PathVariable UUID currencyId,
            @Valid @RequestBody CurrencyOfferRequest currencyOfferRequest
    ) {
        Game game = gameService.getById(gameId);
        Currency currency = currencyService.getCurrencyById(game, currencyId);
        CurrencyOffer currencyOffer = currencyOfferService.createCurrencyOffer(currency, currencyOfferRequest);

        return currencyOfferMapper.toResponse(currencyOffer);
    }

    @GetMapping
    @Secured("ROLE_USER")
    public PaginatedResponse<CurrencyOfferResponse> getCurrencyOffers(
            @PathVariable UUID gameId, @PathVariable UUID currencyId,
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable
    ) {
        Game game = gameService.getById(gameId);
        Currency currency = currencyService.getCurrencyById(game, currencyId);
        Page<CurrencyOffer> currencyOffers = StringUtils.hasText(search) ?
                currencyOfferService.searchCurrencyOffersByCurrency(search, currency, pageable) :
                currencyOfferService.getCurrencyOffersByCurrency(currency, pageable);

        return PaginatedResponse.createResponse(
                currencyOfferMapper.toResponse(currencyOffers),
                "/game/%s/currencies/%s/offers"
        );
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public CurrencyOfferResponse getCurrencyOffer(
            @PathVariable UUID gameId, @PathVariable UUID currencyId, @PathVariable UUID id
    ) {
        Game game = gameService.getById(gameId);
        Currency currency = currencyService.getCurrencyById(game, currencyId);
        CurrencyOffer currencyOffer = currencyOfferService.getCurrencyOfferById(currency, id);

        return currencyOfferMapper.toResponse(currencyOffer);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public CurrencyOfferResponse updateCurrencyOffer(
            @PathVariable UUID gameId, @PathVariable UUID currencyId, @PathVariable UUID id,
            @Valid @RequestBody CurrencyOfferRequest currencyOfferRequest
    ) {
        Game game = gameService.getById(gameId);
        Currency currency = currencyService.getCurrencyById(game, currencyId);
        CurrencyOffer currencyOffer = currencyOfferService.updateCurrencyOfferById(currency, id, currencyOfferRequest);

        return currencyOfferMapper.toResponse(currencyOffer);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public void deleteCurrencyOffer(
            @PathVariable UUID gameId, @PathVariable UUID currencyId, @PathVariable UUID id
    ) {
        Game game = gameService.getById(gameId);
        Currency currency = currencyService.getCurrencyById(game, currencyId);
        currencyOfferService.deleteCurrencyOfferById(currency, id);
    }
}
