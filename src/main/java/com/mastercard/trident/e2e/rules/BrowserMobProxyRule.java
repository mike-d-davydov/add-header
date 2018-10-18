package com.mastercard.trident.e2e.rules;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.junit.Assert;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Map;

public class BrowserMobProxyRule extends ExternalResource implements CapabilitiesProvider {
    private BrowserMobProxy proxy;

    public BrowserMobProxyRule() {
        proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        proxy.start();
    }


    private Proxy getSeleniumProxy() {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Assert.fail("Invalid Host Address");
        }
        return seleniumProxy;
    }

    public Capabilities getCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        Proxy seleniumProxy = getSeleniumProxy();

        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        return capabilities;
    }

    public void addHeaders(Map<String, String> headers) {
        proxy.addHeaders(headers);
    }


    @Override
    public void after() {
        if (proxy != null) {
            proxy.abort();
        }
    }

}
