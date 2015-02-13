package lt.mexahuk.samples.cards.server.model;

import com.google.common.base.Optional;

public interface CardRepository {

    /**
     * Loads all cards, without any criteria
     *
     * @return an empty iterable if there are no entities
     */
    Iterable<CardEntity> list();

    /**
     * Load card by its ID
     *
     * @param id card id
     * @return card found or throws NullPointerException if no card found for given ID
     */
    CardEntity get(String id);

    /**
     * Loads card that has been added last
     *
     * @return card found or absent. Null is never returned
     */
    Optional<CardEntity> getLatest();

    String persist(CardEntity card);
}
