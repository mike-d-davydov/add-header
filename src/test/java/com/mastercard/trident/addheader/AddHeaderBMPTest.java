package com.mastercard.trident.addheader;

import com.google.common.collect.ImmutableMap;
import com.mastercard.trident.e2e.rules.BrowserMobProxyRule;
import com.mastercard.trident.e2e.rules.LocalChromeDriverServerRule;
import com.mastercard.trident.e2e.rules.BaseWebDriverRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(Parameterized.class)
public class AddHeaderBMPTest {

    // First start Proxy
    private static BrowserMobProxyRule bmpRule = new BrowserMobProxyRule(ImmutableMap.of(
            "myHeader", "blah-blah"));
    // Next start ChromeDriverService
    private static LocalChromeDriverServerRule chromeDriverServerRule = new LocalChromeDriverServerRule();
    // Finally, start browser
    private static BaseWebDriverRule webDriverRule = new BaseWebDriverRule(bmpRule);

    @ClassRule
    public static TestRule rule = RuleChain.outerRule(bmpRule)
            .around(chromeDriverServerRule)
            .around(webDriverRule);


    final private static int numRuns = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return IntStream.range(0, numRuns).mapToObj(i -> new Object[]{}).collect(Collectors.toList());
    }



    @Test
    public void test() {
        WebDriver wd = webDriverRule.getWebDriver();
        wd.get("https://www.google.com");
        WebElement q = wd.findElement(By.name("q"));
        q.sendKeys("qwerty");
        q.sendKeys(Keys.RETURN);
        wd.findElement(By.partialLinkText("qwerty"));
    }
}
