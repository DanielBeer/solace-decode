package com.example.solace.decode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "solace.java")
public class SolaceJavaProperties {
    private String host;
    private String msgVpn;
    private String userName;
    private String clientUsername;
    private String clientPassword;
    private Integer connectRetries;
    private Integer reconnectRetries;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMsgVpn() {
        return msgVpn;
    }

    public void setMsgVpn(String msgVpn) {
        this.msgVpn = msgVpn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public Integer getConnectRetries() {
        return connectRetries;
    }

    public void setConnectRetries(Integer connectRetries) {
        this.connectRetries = connectRetries;
    }

    public Integer getReconnectRetries() {
        return reconnectRetries;
    }

    public void setReconnectRetries(Integer reconnectRetries) {
        this.reconnectRetries = reconnectRetries;
    }
}
