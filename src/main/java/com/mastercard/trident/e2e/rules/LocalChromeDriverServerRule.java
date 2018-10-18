package com.mastercard.trident.e2e.rules;

import org.junit.Assert;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LocalChromeDriverServerRule extends ExternalResource implements SeleniumServerUrlProvider {
    private Logger logger = LoggerFactory.getLogger(LocalChromeDriverServerRule.class);
    private ChromeDriverService service = null;
    private String serverUrl = "";


    public LocalChromeDriverServerRule() {
        try {
            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(
                            new File(ClassLoader.getSystemResource(
                                    "bin/mac/chromedriver"
                            ).toURI()))
                    .usingAnyFreePort()
                    .build();
            service.start();

            String serviceUrl = service.getUrl().toString();
            serverUrl = serviceUrl;
            System.setProperty("webdriver.remote.server", serviceUrl);
        } catch (Exception e) {
            logger.error("", e);
            Assert.fail("Failed to start server:" + e);
        }
    }


    @Override
    public void after() {
        if (service != null) {
            service.stop();
        }
    }

    @Override
    public String getServerURL() {
        return serverUrl;
    }
}
