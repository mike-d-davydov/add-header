package com.mastercard.trident.e2e.rules;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseWebDriverRule extends ExternalResource implements
        WebDriverProvider {

    protected Capabilities capabilities;
    protected WebDriver webDriver = null;

    public BaseWebDriverRule(CapabilitiesProvider... providers){
        capabilities = new DesiredCapabilities();
        for (CapabilitiesProvider provider:providers){
            capabilities.merge(provider.getCapabilities());
        }
    }

    public WebDriver getWebDriver(){
        return webDriver;
    }

    @Override
    public void before(){
        webDriver = new RemoteWebDriver(capabilities);
    }

    @Override
    public void after(){
        if (webDriver!=null){
            webDriver.quit();
        }
    }
}
