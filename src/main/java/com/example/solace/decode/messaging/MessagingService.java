package com.example.solace.decode.messaging;

import com.example.solace.decode.config.SolaceJavaProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {
    private String url;
    private String vpnName;
    private String userName;
    private String clientName;
    private String password;
    private Integer reconnectRetries;
    private JCSMPSession session;
    private XMLMessageProducer prod;
    private XMLMessageConsumer cons;
    private ObjectMapper objectMapper;

    private SolaceJavaProperties solaceJavaProperties;

    @Autowired
    public MessagingService(SolaceJavaProperties solaceJavaProperties) throws Exception {
        this.solaceJavaProperties = solaceJavaProperties;

        this.url = solaceJavaProperties.getHost();
        this.vpnName = solaceJavaProperties.getMsgVpn();
        this.userName = solaceJavaProperties.getUserName();
        this.clientName = solaceJavaProperties.getClientUsername();
        this.password = solaceJavaProperties.getClientPassword();
        this.reconnectRetries = solaceJavaProperties.getReconnectRetries();
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
        this.cons = session.getMessageConsumer(new XMLMessageListener() {

            @Override
            public void onReceive(BytesXMLMessage msg) {
                if (msg instanceof TextMessage) {
                    System.out.printf("TextMessage received: '%s'%n",
                            ((TextMessage)msg).getText());
                } else {
                    System.out.println("Message received.");
                }
                System.out.printf("Message Dump:%n%s%n",msg.dump());
            }

            @Override
            public void onException(JCSMPException e) {
                System.out.printf("Consumer received exception: %s%n",e);
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

    public void subscribe(String topicName) throws Exception {
        final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
        session.addSubscription(topic);
        cons.start();
    }
}
