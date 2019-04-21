# add-header

## Overview

This set of libraries is a proof of concept (PoC) for integration of WebDriver end-to-end (E2E) tests with [Apache Synapse Enterprise Service Bus](https://synapse.apache.org/). The idea is that we can use additional request headers to tell Synapse how to route particular requests from E2E tests (use case: Blue/Green deployment).

For Web Server (SUT) being tested with E2E test framework it will look like all the requests from Browser have some extra headers.

This PoC demonstrate 2 different approachees to this task:

### Use Proxy server between Browser and SUT (Web Server) 

We start Proxy Server and tell Selenium to configure browser to use that proxy. In our case we use BrowserModProxy. Proxy can be easily configured to add headers or manupulate requests in any other way.


*Advantages:*
* The approach is browser-agnostic. We can use it with any browser. 

*Drawbacks:*
* It slows down system considerably \
* It is a "MiTM" approach, which means that it is considered as a compromise to security by Browser (and possibly, SUT). This may lead to some problems.

### Use relevant Browser plug-in that will add headers for us on the Browser side. 

We configure browser (Chrome) to use particular plugin, and then configure that plugin to add headers to all the requests by executing JavaScript script against plugin page.

*Advantages:* 
* Much faster
* No SSL problems 

*Drawbacks:* 
* It is browser-specific. Need different plugins and integration for every browser.

## Framework

PoC implemented in form of simple E2E test framework, based on JUnit rules which is all you need to have in order to get a fully configured webdriver instance.

### Main concepts:

- Tests use JUnit rules or rules chain instead of inheriting from particular base class
- Rules are chained using constructor parameters and Java "system" variables: e.g., we first create BrowserMobProxy server, then passing relevant DesiredCapabilites (with proxy details) to WebDriver rule that actually instantiate the WebDriver.
- All rules can be used either as test-method-level rules, or class-level rules in different combinations. E.g., you can create WebDriver for every test, or for all tests within the class. Same with BrowserModProxy (though, if you create BrowserModProxy every for every test, you will have to have webdriver-per-test as well).

### Examples of framework rules usage

Please refer to tests (https://github.com/mike-d-davydov/add-header/tree/master/src/test/java/com/mastercard/trident/addheader)

#### 1. WebDriver per test class with ModExtension

    public class Test1 {

      // This is important to have access to rule methods
      
      private static LocalChromeDriverServerRule chromeDriverServerRule = new LocalChromeDriverServerRule();
      private static ChromeDriverWithModHeadersExtensionRule mhRule = new ChromeDriverWithModHeadersExtensionRule(
              ImmutableMap.of("myHeader", "blah-blah"));

      // All rules are being run on class level (mind static modifier for fields above)
      
      @ClassRule
      public static TestRule rule = RuleChain.outerRule(chromeDriverServerRule)
              .around(mhRule);
      
      // Tests code here... 
    }
    

#### 2. WebDriver per test with BrowserMobProxy


    public class Test2 {

      // First start Proxy
      private static BrowserMobProxyRule bmpRule = new BrowserMobProxyRule(ImmutableMap.of(
              "myHeader", "blah-blah"));
      // Next start ChromeDriverService
      private static LocalChromeDriverServerRule chromeDriverServerRule = new LocalChromeDriverServerRule();

      @ClassRule
      public static TestRule rule = RuleChain.outerRule(bmpRule)
              .around(chromeDriverServerRule);

      // Browser is started (and disposed) individually for each test
      @Rule
      public BaseWebDriverRule webDriverRule = new BaseWebDriverRule(bmpRule);

      // Test code here...

    }
