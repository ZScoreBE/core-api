package be.zsoft.zscore.core.service.currency;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.request.currency.CurrencyRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.repository.currency.CurrencyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CurrencyMapper currencyMapper;
    private final CurrencyRepo currencyRepo;

    public Currency createCurrency(Game game, CurrencyRequest request) {
        Currency currency = currencyMapper.fromRequest(request);
        currency.setGame(game);

        return currencyRepo.saveAndFlush(currency);
    }

    public Page<Currency> getCurrenciesByGame(Game game, Pageable pageable) {
        return currencyRepo.findAllByGame(game, pageable);
    }

    public Page<Currency> searchCurrenciesByGame(String search, Game game, Pageable pageable) {
        return currencyRepo.searchAllOnNameOrKeyByGame("%" + search.toLowerCase() + "%", game, pageable);
    }

    public Currency getCurrencyById(Game game, UUID id) {
        return currencyRepo.findByIdAndGame(id, game)
                .orElseThrow(() -> new NotFoundException("Could not find currency '%s' for game '%s'".formatted(id, game.getId())));
    }

    public Currency updateCurrencyById(Game game, UUID id, CurrencyRequest request) {
        Currency currency = currencyMapper.fromRequest(request, getCurrencyById(game, id));

        return currencyRepo.saveAndFlush(currency);
    }

    public void deleteCurrencyById(Game game, UUID id) {
        Currency currency = getCurrencyById(game, id);
        currencyRepo.delete(currency);
    }
}
