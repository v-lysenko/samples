package lt.mexahuk.samples.cards.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import lt.mexahuk.samples.cards.api.Card;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class ClientCardPresenter extends Composite {

    private static ClientCardPresenterUiBinder uiBinder = GWT.create(ClientCardPresenterUiBinder.class);
    @UiField
    Image settingsIcon;
    @UiField
    Image cancelIcon;
    @UiField
    HTML orderNumber;
    @UiField
    HTMLPanel cardElement;
    @UiField
    HTMLPanel layerElement;
    @UiField
    HTMLPanel deliveredFromLabControl;

    private final CardResourceClient cardResource;

    private Card cardData;

    private boolean handlersRegistered = false;

    public ClientCardPresenter(CardResourceClient cardResource) {
        initWidget(uiBinder.createAndBindUi(this));
        this.cardResource = cardResource;

        cardResource.latest(new CallbackForRefresh() {
            @Override
            public void onSuccess(Method method, Card card) {
                super.onSuccess(method, card);

                if (card == null) {
                    Window.alert("There is no latest card. Please, register one using API and refresh view");
                }

                registerHandlers();
            }
        });
    }

    private void registerHandlers() {
        if (handlersRegistered) {
            return;
        }
        registerSelectionHandler();
        registerCancelSelectionHandler();
        registerCompletionHandler();
        registerDeliveryHandler();
        handlersRegistered = true;
    }

    private void registerSelectionHandler() {
        settingsIcon.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                moveCardForward();
            }
        });
    }

    private void moveCardForward() {
        cardResource.moveForward(cardData.getId(), new CallbackForRefresh());
    }

    private void registerCancelSelectionHandler() {
        cancelIcon.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.stopPropagation();
                cardResource.unselect(cardData.getId(), new CallbackForRefresh());
            }
        });
    }

    private void registerCompletionHandler() {
        layerElement.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                // Due to layout specifics and event propagation from deeper controls to layout element
                if ("SELECTED".equals(cardData.getStatus())) {
                    moveCardForward();
                }
            }
        }, ClickEvent.getType());
    }

    private void registerDeliveryHandler() {
        deliveredFromLabControl.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                moveCardForward();
            }
        }, ClickEvent.getType());
    }

    public void refresh() {
        String styleName = "card";
        orderNumber.setText("#" + cardData.getId());
        switch (cardData.getStatus()) {
            case "NORMAL":
                break;
            case "SELECTED":
                styleName = styleName.concat(" selected");
                break;
            case "COMPLETED":
                styleName = styleName.concat(" completed");
                break;
            case "DELIVERED":
                styleName = styleName.concat(" outOfLab");
                break;
        }
        cardElement.setStyleName(styleName);
    }

    interface ClientCardPresenterUiBinder extends UiBinder<Widget, ClientCardPresenter> {
    }

    private class CallbackForRefresh implements MethodCallback<Card> {
        @Override
        public void onFailure(Method method, Throwable ex) {
            Window.alert("Something went wrong. Error: " + ex.toString());
        }

        @Override
        public void onSuccess(Method method, Card card) {
            cardData = card;
            refresh();
        }
    }

    void swapCard(Card newCard) {
        cardData = newCard;
        if (!handlersRegistered) {
            registerHandlers();
        }
        refresh();
    }
    
}
