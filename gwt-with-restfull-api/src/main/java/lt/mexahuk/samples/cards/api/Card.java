package lt.mexahuk.samples.cards.api;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class Card {
    private final String id;
    private final String status;

    @JsonCreator
    public Card(@JsonProperty("id") String id, @JsonProperty("status") String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "card {id: " + id + ", status: " + status + "}";
    }
}
