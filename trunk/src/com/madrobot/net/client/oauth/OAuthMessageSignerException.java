package com.madrobot.net.client.oauth;

@SuppressWarnings("serial")
public class OAuthMessageSignerException extends OAuthException {

    public OAuthMessageSignerException(String message) {
        super(message);
    }

    public OAuthMessageSignerException(Exception cause) {
        super(cause);
    }

}
