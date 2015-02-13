package lt.mexahuk.samples.cards.server.impl.api;

import lt.mexahuk.samples.cards.api.Card;
import lt.mexahuk.samples.cards.api.CardResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class CardResourceImpl implements CardResource {

    private static final Logger log = LoggerFactory.getLogger(CardResourceImpl.class);

    private CardService cardService;

    @Autowired
    CardResourceImpl(CardService cardService) {
        this.cardService = cardService;
    }

    @PostConstruct
    public void postConstruct() {
        log.info("Resource initialized");
    }

    @Override
    public Iterable<Card> list() {
        log.debug("List cards");
        return cardService.allCards();
    }

    @Override
    public Card latest() {
        log.debug("return latest card");
        return cardService.latestCard().orNull();
    }

    @Override
    public Card getCard(String id) {
        log.debug("Card for id: {}", id);
        return cardService.oneCard(id);
    }

//    @Override
//    public void updateCard(String id, Card card) {
//        log.debug("Card is to be updated: {}", card);
//        if (cardService.oneCard(id) == null) {
//            throw new RuntimeException("No card found: " + id);
//        }
//        cardService.updateCardStatus(id, card.getStatus());
//    }

    @Override
    public Card moveForward(String id) {
        log.debug("Move card forward: {}", id);
        return cardService.moveForward(id);
    }

    @Override
    public Card unselect(String id) {
        log.debug("About to unselect card with id: {}", id);
        return cardService.unselect(id);
    }

    @Override
    public Card newCard() {
        log.debug("About to create new card");
        return cardService.newCard();
    }
}
