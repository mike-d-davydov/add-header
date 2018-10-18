package com.mastercard.trident.e2e.rules;

import org.junit.rules.ExternalResource;

public class SeleniumGridRule extends ExternalResource implements SeleniumServerUrlProvider {

    private String serverUrl;

    public SeleniumGridRule(String seleniumGridServerUrl){
        this.serverUrl = seleniumGridServerUrl;
    }


    @Override
    public String getServerURL() {
        return serverUrl;
    }
}
