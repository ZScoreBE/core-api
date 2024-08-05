package be.zsoft.zscore.core.controller.currency;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.request.currency.CurrencyRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
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
@RequestMapping("/games/{gameId}/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;
    private final GameService gameService;

    @PostMapping
    @Secured("ROLE_USER")
    public CurrencyResponse createCurrency(
            @PathVariable UUID gameId, @Valid @RequestBody CurrencyRequest currencyRequest
    ) {
        Game game = gameService.getById(gameId);
        Currency currency = currencyService.createCurrency(game, currencyRequest);

        return currencyMapper.toResponse(currency);
    }

    @GetMapping
    @Secured("ROLE_USER")
    public PaginatedResponse<CurrencyResponse> getCurrencies(
            @PathVariable UUID gameId,
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable
    ) {
        Game game = gameService.getById(gameId);
        Page<Currency> currencies = StringUtils.hasText(search) ?
                currencyService.searchCurrenciesByGame(search, game, pageable) :
                currencyService.getCurrenciesByGame(game, pageable);

        return PaginatedResponse.createResponse(
                currencyMapper.toResponse(currencies),
                "/games/%s/currencies".formatted(gameId)
        );
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public CurrencyResponse getCurrency(
            @PathVariable UUID gameId, @PathVariable UUID id
    ) {
        Game game = gameService.getById(gameId);
        Currency currency = currencyService.getCurrencyById(game, id);

        return currencyMapper.toResponse(currency);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public CurrencyResponse updateCurrency(
            @PathVariable UUID gameId, @PathVariable UUID id, @Valid @RequestBody CurrencyRequest currencyRequest
    ) {
        Game game = gameService.getById(gameId);
        Currency currency = currencyService.updateCurrencyById(game, id, currencyRequest);

        return currencyMapper.toResponse(currency);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public void deleteCurrency(
            @PathVariable UUID gameId, @PathVariable UUID id
    ) {
        Game game = gameService.getById(gameId);
        currencyService.deleteCurrencyById(game, id);
    }
}
