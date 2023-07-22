package org.example.gc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OauthConfiguration {
    /*
    private static final List<String> clients = Arrays.asList("google", "facebook");
    private static final String CLIENT_PROPERTY_KEY
            = "spring.security.oauth2.client.registration.";
    @Autowired
    private Environment env;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(this::getRegistration)
                .filter(Objects::nonNull).toList();
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-secret");

        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId).clientSecret(clientSecret).build();
        }
        if (client.equals("facebook")) {
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(clientId).clientSecret(clientSecret).build();
        }
        return null;
    }
    */
}
