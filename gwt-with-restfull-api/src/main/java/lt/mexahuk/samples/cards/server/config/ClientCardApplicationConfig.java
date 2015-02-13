package lt.mexahuk.samples.cards.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "lt.mexahuk.samples.cards")
class ClientCardApplicationConfig {

    private static final Logger log = LoggerFactory.getLogger(ClientCardApplicationConfig.class);

    @PostConstruct
    public void postConstruct() {
        log.info("Client card application is warming up");
    }
}
