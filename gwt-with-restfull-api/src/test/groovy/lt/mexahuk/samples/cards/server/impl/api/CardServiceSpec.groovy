package lt.mexahuk.samples.cards.server.impl.api

import com.google.common.base.Optional
import lt.mexahuk.samples.cards.api.Card
import lt.mexahuk.samples.cards.server.model.CardEntity
import lt.mexahuk.samples.cards.server.model.CardRepository
import spock.lang.Specification

import static lt.mexahuk.samples.cards.server.model.CardEntity.Status.COMPLETED
import static lt.mexahuk.samples.cards.server.model.CardEntity.Status.NORMAL

class CardServiceSpec extends Specification {

    CardService cardService
    CardRepository repository = Mock()
    Iterable<CardEntity> allCardEntities = []

    CardEntity cardEntityOne,
               cardEntityTwo

    def setup() {
        def cardEntity = { id, status ->
            Mock(CardEntity) {
                getId() >> id
                getStatus() >> status
            }
        }
        cardEntityOne = cardEntity("ONE", NORMAL)
        cardEntityTwo = cardEntity("TWO", COMPLETED)
        allCardEntities << cardEntityOne << cardEntityTwo

        cardService = new CardService(repository)
    }

    def "All cards from the repository are returned"() {
        given:
        repository.list() >> allCardEntities

        when:
        def allCards = cardService.allCards()
        then:
        allCards.each { Card card ->
            assert allCardEntities.any { CardEntity entity -> entity.id.equals(card.id)
            }
        }
    }

    def "Last card is returned"() {
        given:
        repository.getLatest() >> Optional.fromNullable(cardEntityOne)

        when:
        def latestCard = cardService.latestCard()
        then:
        assert latestCard.get().id.equals(cardEntityOne.id)
        assert latestCard.get().status.equals(cardEntityOne.status.name())
    }

    def "One card is returned"() {
        given:
        repository.get(_) >> cardEntityOne >> { throw new NullPointerException() }

        when:
        def aCard = cardService.oneCard(cardEntityOne.id)
        then:
        assert aCard.id.equals(cardEntityOne.id)
        assert aCard.status.equals(cardEntityOne.status.name())

        when:
        cardService.oneCard(cardEntityOne.id)
        then:
        thrown(NullPointerException)
    }

    def "Move card forward"() {
        given:
        repository.get(_) >> cardEntityOne

        when:
        def aCard = cardService.moveForward(cardEntityOne.id)
        then:
        1 * cardEntityOne.moveForward()
        and:
        assert aCard.id.equals(cardEntityOne.id)
        assert aCard.status.equals(cardEntityOne.status.name())
    }

    def "Unselect card"() {
        given:
        repository.get(_) >> cardEntityOne

        when:
        def aCard = cardService.unselect(cardEntityOne.id)
        then:
        1 * cardEntityOne.unselect()
        and:
        assert aCard.id.equals(cardEntityOne.id)
        assert aCard.status.equals(cardEntityOne.status.name())
    }

    def "Card generated and persisted"() {

        when:
        def aCard = cardService.newCard()
        then:
        1 * repository.persist(_)
        and:
        assert aCard != null
    }

}
