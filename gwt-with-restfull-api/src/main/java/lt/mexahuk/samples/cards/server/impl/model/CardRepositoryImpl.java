package lt.mexahuk.samples.cards.server.impl.model;


import com.google.common.base.Optional;
import lt.mexahuk.samples.cards.server.model.CardEntity;
import lt.mexahuk.samples.cards.server.model.CardRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Order.desc;
import static org.springframework.util.Assert.notNull;

@Repository
class CardRepositoryImpl implements CardRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    CardRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Iterable<CardEntity> list() {
        @SuppressWarnings("unchecked")
        Iterable<CardEntity> cards = (Iterable<CardEntity>) criteria().list();
        return cards;
    }

    @Override
    public Optional<CardEntity> getLatest() {
        CardEntity card = (CardEntity) criteria()
                .addOrder(desc("id")).setMaxResults(1)
                .uniqueResult();
        return Optional.fromNullable(card);
    }

    @Override
    public CardEntity get(String id) {
        CardEntity card = (CardEntity) session().get(CardEntity.class, id);
        notNull(card, "No card found for id: " + id);
        return card;
    }

    @Override
    public String persist(CardEntity card) {
        session().persist(card);
        return card.getId();
    }

    private Criteria criteria() {
        return session().createCriteria(CardEntity.class);
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
}
