package be.zsoft.zscore.core.service.trigger;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.trigger.TriggerMapper;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.repository.trigger.TriggerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TriggerService {

    private final TriggerRepo triggerRepo;
    private final TriggerMapper triggerMapper;

    public Trigger createTrigger(TriggerRequest request, Game game) {
        Trigger trigger = triggerMapper.fromRequest(request);
        trigger.setGame(game);

        return triggerRepo.saveAndFlush(trigger);
    }

    public Page<Trigger> getTriggersByGame(Game game, Pageable pageable) {
        return triggerRepo.findAllByGame(game, pageable);
    }

    public Page<Trigger> searchTriggersByGame(String search, Game game, Pageable pageable) {
        return triggerRepo.searchAllByGame("%" + search.toLowerCase() + "%", game, pageable);
    }

    public Trigger getTriggerByIdAndGame(UUID id, Game game) {
        return triggerRepo.findByIdAndGame(id, game)
                .orElseThrow(() -> new NotFoundException("Could not find trigger '%s' for game '%s'".formatted(id, game.getId())));
    }

    public Trigger updateTrigger(TriggerRequest request, UUID id, Game game) {
        Trigger trigger = getTriggerByIdAndGame(id, game);
        trigger = triggerMapper.fromRequest(request, trigger);

        return triggerRepo.saveAndFlush(trigger);
    }

    public void deleteByIdAndGame(UUID id, Game game) {
        Trigger trigger = getTriggerByIdAndGame(id, game);
        triggerRepo.delete(trigger);
    }
}
