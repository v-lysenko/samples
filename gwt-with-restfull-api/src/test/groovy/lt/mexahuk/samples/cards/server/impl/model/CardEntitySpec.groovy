package lt.mexahuk.samples.cards.server.impl.model

import lt.mexahuk.samples.cards.server.model.CardEntity
import spock.lang.Specification

import static lt.mexahuk.samples.cards.server.model.CardEntity.Status.*

class CardEntitySpec extends Specification {

    def "Card selection is allowed for status NORMAL only"() {
        given:
        def card = new CardEntity("1");

        when:
        card.select()
        then:
        card.getStatus() == SELECTED

        when: "on consequent calls no status changed"
        card.select()
        then:
        card.getStatus() == SELECTED

        when:
        card.moveForward()
        card.select()
        then:
        thrown(IllegalStateException)
    }

    def "unselecting is allowed for selected card only"() {
        when:
        def card = new CardEntity("1");
        then:
        card.getStatus() == NORMAL

        when: "status is NORMAL nothing happens"
        card.unselect()
        then:
        card.getStatus() == NORMAL

        when:
        card.select()
        card.unselect()
        then:
        card.getStatus() == NORMAL

        when:
        card.select()
        card.moveForward()
        card.unselect()
        then:
        thrown(IllegalStateException)
    }

    def "Card completion is allowed for selected card only"() {
        given:
        def card = new CardEntity("1");

        when:
        card.complete()
        then:
        thrown(IllegalStateException)

        when:
        card.select()
        card.complete()
        then:
        card.getStatus() == COMPLETED

        when: "on consequent calls no status changed"
        card.complete()
        then:
        card.getStatus() == COMPLETED

        when:
        card.moveForward()
        card.complete()
        then:
        thrown(IllegalStateException)
    }

    def "Card delivery is allowed for completed card only"() {
        given:
        def card = new CardEntity("1");

        when:
        card.deliver()
        then:
        thrown(IllegalStateException)

        when:
        card.select()
        card.complete()
        card.deliver()
        then:
        card.getStatus() == DELIVERED

        when: "on consequent calls no status changed"
        card.deliver()
        then:
        card.getStatus() == DELIVERED

        when:
        card.moveForward()
        card.complete()
        then:
        thrown(IllegalStateException)
    }


    def "Move forward proceeds according to rules"() {
        when:
        def card = new CardEntity("1");
        then:
        card.getStatus() == NORMAL

        when:
        card.moveForward()
        then:
        card.getStatus() == SELECTED

        when:
        card.moveForward()
        then:
        card.getStatus() == COMPLETED

        when:
        card.moveForward()
        then:
        card.getStatus() == DELIVERED

        when: "On consequent moves no changes happens"
        card.moveForward()
        then:
        card.getStatus() == DELIVERED
    }
}
