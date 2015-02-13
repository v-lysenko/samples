package lt.mexahuk.samples.cards.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import lt.mexahuk.samples.cards.api.Card;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class EntryPoint implements com.google.gwt.core.client.EntryPoint {

    @Override
    public void onModuleLoad() {
        Defaults.setServiceRoot(GWT.getHostPageBaseURL());
        final CardResourceClient cardResource = GWT.create(CardResourceClient.class);
        final ClientCardPresenter cardPresenter = new ClientCardPresenter(cardResource);
        RootPanel.get("cardContainer").add(cardPresenter);

        // hack to reset allready completed card by producing new one
        RootPanel.get("add_new_order_btn").addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                cardResource.newCard(new MethodCallback<Card>() {
                    @Override
                    public void onFailure(Method method, Throwable ex) {
                        Window.alert("Failed to generate new card. Error: " + ex.toString());
                    }

                    @Override
                    public void onSuccess(Method method, Card card) {
                        cardPresenter.swapCard(card);
                    }
                });
            }
        }, ClickEvent.getType());
    }

}
