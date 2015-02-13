package lt.mexahuk.samples.cards.server.impl.api;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;


public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(RequestContextFilter.class);
        register(JacksonFeature.class);
        register(CardResourceImpl.class);
    }
}
