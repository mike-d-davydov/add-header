package com.mastercard.trident.e2e.rules;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChromeDriverWithModHeadersExtensionRule extends BaseWebDriverRule implements AddsHeaders {
    private Map<String, String> headers = new HashMap<>();


    public ChromeDriverWithModHeadersExtensionRule() {
        super(() -> {
            ChromeOptions options = new ChromeOptions();
            try {
                options.addExtensions(
                        new File(ClassLoader.getSystemResource(
                                "bin/ModHeader_v2.2.5.crx"
                        ).toURI()));
            } catch (Exception e) {
                throw new RuntimeException("Invalid extension URL", e);
            }
            return options;
        });
    }



    public ChromeDriverWithModHeadersExtensionRule(Map<String, String> headers, CapabilitiesProvider... providers) {
        this(providers);
        this.headers = headers;
    }


    public ChromeDriverWithModHeadersExtensionRule(CapabilitiesProvider... providers) {
        this();

        for (CapabilitiesProvider p:providers){
            this.capabilities.merge(p.getCapabilities());
        }
    }

    private String generateHeadersPart(Map<String, String> headers) {
        List<String> entries =
                headers.entrySet().stream().map(
                        entry -> String.format(
                                "{enabled: true, name: '%s', value: '%s', comment: ''}"
                                , entry.getKey()
                                , entry.getValue())).collect(Collectors.toList());
        return String.join(",", entries.toArray(new String[headers.size()]));

    }

    public void addHeaders(Map<String, String> headers) {
        ((RemoteWebDriver) webDriver).executeScript("localStorage.setItem('profiles', JSON.stringify([{ " +
                "title: 'Selenium', hideComment: true, appendMode: ''," +
                " headers: [" + generateHeadersPart(headers) +
                "]," +
                "respHeaders: []," +
                " filters: []" +
                "}]));");
    }


    @Override
    public void before() {
        webDriver = new RemoteWebDriver(capabilities);
        webDriver.get("chrome-extension://idgpnmonknjnojddfkpgkljpfnnfcklj/icon.png");
        if (!headers.isEmpty()){
            addHeaders(headers);
        }

    }

}
