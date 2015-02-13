package lt.mexahuk.samples.cards.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
class PropertiesConfiguration {

    private static final Logger log = LoggerFactory.getLogger(PropertiesConfiguration.class);

    @Bean
    static PropertySourcesPlaceholderConfigurer propertiesConfigurer(
            @Value("classpath:app.properties") Resource defaultBundle,
            @Value("file:///${app.home}/app.properties") Resource appHomeBundle) throws IOException {

        log.info("Setup properties");
        log.debug("Default bundle: {}", defaultBundle.getFile());
        log.debug("application.home bundle: {}", appHomeBundle.getFile());

        List<Resource> locations = new ArrayList<>();
        locations.add(defaultBundle);
        if (appHomeBundle.exists()) {
            locations.add(appHomeBundle);
        }

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocations(locations.toArray(new Resource[]{}));
//        configurer.setIgnoreResourceNotFound(ignoreAppHomeBundleNotFound);
        return configurer;
    }
}

