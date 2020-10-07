package com.phoebus.appdemowallet.models;

public class ResponseCredentials {
    private String access_token;
    private Integer expires_in;
    private Integer refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private Integer not_before_policy;
    private String session_state;
    private String scope;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public Integer getExpiresIn() {
        return expires_in;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expires_in = expiresIn;
    }

    public Integer getRefreshExpiresIn() {
        return refresh_expires_in;
    }

    public void setRefreshExpiresIn(Integer refreshExpiresIn) {
        this.refresh_expires_in = refreshExpiresIn;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refresh_token = refreshToken;
    }

    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String tokenType) {
        this.token_type = tokenType;
    }

    public Integer getNotBeforePolicy() {
        return not_before_policy;
    }

    public void setNotBeforePolicy(Integer notBeforePolicy) {
        this.not_before_policy = notBeforePolicy;
    }

    public String getSessionState() {
        return session_state;
    }

    public void setSessionState(String sessionState) {
        this.session_state = sessionState;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return    "\nAccess token: " + getAccessToken()
                + "\nRefresh token: " + getRefreshToken()
                + "\nScope: " + getScope()
                + "\nSession state: " + getSessionState()
                + "\nToken type: " + getTokenType()
                + "\nExpires in: " + getExpiresIn()
                + "\nNot before policy: " + getNotBeforePolicy()
                + "\nRefresh expires in: " + getRefreshExpiresIn();
    }
}
