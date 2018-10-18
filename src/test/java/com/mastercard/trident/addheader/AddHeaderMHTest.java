package com.mastercard.trident.addheader;

import com.google.common.collect.ImmutableMap;
import com.mastercard.trident.e2e.rules.LocalChromeDriverServerRule;
import com.mastercard.trident.e2e.rules.ModHeadersExtensionRule;
import org.junit.BeforeClass;
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
public class AddHeaderMHTest {

    private static ModHeadersExtensionRule mhRule = new ModHeadersExtensionRule();
    private static LocalChromeDriverServerRule chromeDriverServerRule = new LocalChromeDriverServerRule();

    @ClassRule
    public static TestRule rule = RuleChain.outerRule(chromeDriverServerRule)
            .around(mhRule);

    final private static int numRuns = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return IntStream.range(0, numRuns).mapToObj(i -> new Object[]{}).collect(Collectors.toList());
    }


    @BeforeClass
    public static void addHeaders() {
        mhRule.addHeaders(
                ImmutableMap.of("myHeader", "blah-blah")
        );
    }

    @Test
    public void test() {
        WebDriver wd = mhRule.getWebDriver();
        wd.get("https://www.google.com");
        WebElement q = wd.findElement(By.name("q"));
        q.sendKeys("qwerty");
        q.sendKeys(Keys.RETURN);
        wd.findElement(By.partialLinkText("qwerty"));
    }
}
