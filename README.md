# add-header

## Overview

This set of libraries is a proof of concept (PoC) for E2E -> Synapse integration. The idea is that we can use additional request headers to tell Synapse how to route particular requests from E2E tests (use case: Blue/Green deployment).

For Web Server (SUT) being tested with E2E test framework it will look like all the requests from Browser have some extra headers.

This PoC demonstrate 2 different approachees to this task:

### Use Proxy server between Browser and SUT (Web Server) 

We start Proxy Server and tell Selenium to configure browser to use that proxy. In our case we use BrowserModProxy. Proxy can be easily configured to add headers or manupulate requests in any other way.

Main advantage of this approach is that it is browser-agnostic. We can use it with any browser. Main drawbacks are: 1) It slows down system considerably 2) It is a "MiTM" approach, which means that it is considered as a compromise to security by Browser (and possibly, SUT). This may lead to some problems.

### Use relevant Browser plug-in that will add headers for us on the Browser side. 

We configure browser (Chrome) to use particular plugin, and then configure that plugin to add headers to all the requests by executing JavaScript script against plugin page.

Advantage: much faster, no SSL problems \
Drawback: it is browser-specific. Need different plugins and integration for every browser

