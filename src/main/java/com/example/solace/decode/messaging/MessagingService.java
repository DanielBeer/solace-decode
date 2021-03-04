package com.example.solace.decode.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.*;
import org.springframework.stereotype.Component;

@Component
public class MessagingService {
    private String url;
    private String vpnName;
    private String userName;
    private String clientName;
    private String password;
    private Integer reconnectRetries;
    private JCSMPSession session;
    private XMLMessageProducer prod;
    private ObjectMapper objectMapper;

    public MessagingService() throws Exception {
        this.url = "wss://mr1rvhmgxn1b0t.messaging.solace.cloud:443";
        this.vpnName = "decode";
        this.userName = "solace-cloud-client";
        this.clientName = "server";
        this.password = "tjn2jlk195ntk213e5idk29929";
        this.reconnectRetries = -1;
        final JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, url);
        properties.setProperty(JCSMPProperties.USERNAME, userName);
        properties.setProperty(JCSMPProperties.VPN_NAME, vpnName );
        properties.setProperty(JCSMPProperties.CLIENT_NAME, clientName);
        properties.setProperty(JCSMPProperties.PASSWORD, password );


        this.session = JCSMPFactory.onlyInstance().createSession(properties);
        this.objectMapper = new ObjectMapper();
        session.connect();
        this.prod = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {

            @Override
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }

            @Override
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n",
                        messageID,timestamp,e);
            }
        });

    }

    public void publish(String topicName, Object content) throws Exception{
        final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        String text = objectMapper.writeValueAsString(content);
        msg.setText(text);
        prod.send(msg,topic);
    }

}