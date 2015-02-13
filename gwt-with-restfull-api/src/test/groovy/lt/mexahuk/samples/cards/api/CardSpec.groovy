package lt.mexahuk.samples.cards.api

import spock.lang.Specification

public class CardSpec extends Specification {

    def "toString() produces string as expected"() {
        given:
        Card card = new Card("abc", "SELECTED")

        expect:
        card.toString() == "card {id: ${card.id}, status: ${card.status}}"
    }
}