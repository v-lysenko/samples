package lt.mexahuk.samples.cards.server.impl.api;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import lt.mexahuk.samples.cards.api.Card;
import lt.mexahuk.samples.cards.server.model.CardEntity;
import lt.mexahuk.samples.cards.server.model.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    private CardRepository repository;

    @Autowired
    CardService(CardRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Iterable<Card> allCards() {
        Iterable<CardEntity> cardEntries = repository.list();
        List<Card> cards = new ArrayList<>();
        for (CardEntity entry : cardEntries) {
            cards.add(cardEntityToBean(entry));
        }
        return cards;
    }

    @Transactional
    public Optional<Card> latestCard() {
        Optional<CardEntity> latestCard = repository.getLatest();
        return latestCard.transform(new Function<CardEntity, Card>() {
            @Override
            public Card apply(CardEntity entity) {
                return cardEntityToBean(entity);
            }
        });
    }

    private Card cardEntityToBean(CardEntity entry) {
        return new Card(entry.getId(), entry.getStatus().name());
    }

    @Transactional
    public Card oneCard(String id) {
        return cardEntityToBean(repository.get(id));
    }

    @Transactional
    public Card moveForward(String id) {
        CardEntity cardEntity = repository.get(id);
        cardEntity.moveForward();
        return cardEntityToBean(cardEntity);
    }

    @Transactional
    public Card unselect(String id) {
        CardEntity cardEntity = repository.get(id);
        cardEntity.unselect();
        return cardEntityToBean(cardEntity);
    }

    @Transactional
    public Card newCard() {
        CardEntity cardEntity = new CardEntity("" + System.currentTimeMillis());
        repository.persist(cardEntity);
        return cardEntityToBean(cardEntity);
    }
}
