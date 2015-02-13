package lt.mexahuk.samples.cards.server.model;

import javax.persistence.*;

import static lt.mexahuk.samples.cards.server.model.CardEntity.Status.*;
import static org.springframework.util.Assert.state;

@Entity(name = "CARDS")
public class CardEntity {

    public enum Status {
        NORMAL, SELECTED, COMPLETED, DELIVERED
    }

    @Id
    @Column(name = "ID")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Status status;

    // For ORM only
    CardEntity() {

    }

    public CardEntity(String id) {
        this.id = id;
        this.status = NORMAL;
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void select() {
        state(status == NORMAL || status == SELECTED, "Card already is moved on");
        status = SELECTED;
    }

    public void unselect() {
        state(status == SELECTED || status == NORMAL, "Card is not selected");
        status = NORMAL;
    }

    public void complete() {
        state(status == SELECTED || status == COMPLETED, "Card is not selected");
        status = COMPLETED;
    }

    public void deliver() {
        state(status == COMPLETED || status == DELIVERED, "Card is not completed yet");
        status = DELIVERED;
    }

    public Status moveForward() {
        nextStep().moveInto();
        return status;
    }

    private Transition nextStep() {
        switch (status) {
            case NORMAL:
                return toSelected();
            case SELECTED:
                return toCompleted();

            case COMPLETED:
                return toDelivered();

            case DELIVERED:
            default:
                return toNowhere();
        }
    }

    private Transition toNowhere() {
        return new Transition() {
            @Override
            public void moveInto() {
                // There is nowhere to pass to
            }
        };
    }

    private Transition toSelected() {
        return new Transition() {
            @Override
            public void moveInto() {
                select();
            }
        };
    }

    private Transition toCompleted() {
        return new Transition() {
            @Override
            public void moveInto() {
                complete();
            }
        };
    }

    private Transition toDelivered() {
        return new Transition() {
            @Override
            public void moveInto() {
                deliver();
            }
        };
    }

    private interface Transition {
        void moveInto();
    }
}
