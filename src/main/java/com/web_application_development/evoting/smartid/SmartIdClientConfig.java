package com.web_application_development.evoting.smartid;

import ee.sk.smartid.SmartIdClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmartIdClientConfig {
    
    @Bean
    SmartIdClient smartIdClient(@Value("${smartid.uuid}") String uuid, @Value("${smartid.name}") String name, @Value("${smartid.hosturl}") String hosturl) {
        SmartIdClient client = new SmartIdClient();
        client.setRelyingPartyUUID(uuid);
        client.setRelyingPartyName(name);
        client.setHostUrl(hosturl);
        return client;
    }
}