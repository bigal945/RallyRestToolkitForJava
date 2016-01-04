## Description

In the process of developing an application that utilizes the Rally Rest API, I came across a bad dependency with Apache HTTPClient in my app verses the older Apache HTTP v4.2 bundled with RallyRestToolkitForJava.  In looking at this problem, I could upgrade their version to 4.3 to be more compatible with my application - or, not.  I discovered that a lot of what I would change to make 4.3 work would eventually be changed when upgrading to 5.x.  So I decided to strip out Apache HTTP Client and replace it with [OkHTTP](https://github.com/square/okhttp) to avoid any further dependency overlap.  This project is built with OkHttp v2.7.

## License

Original code which was forked and mofidied here carried: Copyright (c) Rally Software Development Corp. 2013 Distributed under the MIT License.  See the original stream [here](https://github.com/RallyTools/RallyRestToolkitForJava)

## Support

As-is.  I modified the original due to class conflicts with Apache HTTP.
No continual work is planned.  This project as-is allows me to focus on my original project (no more dependency hell, yay)

## User Guide

Notable Changes from the original
* Proxy support not ported
* API connectivity requires an API key (user/pass was previously deprecated and I did not port it)
* Newly added gzip support not currently ported (may change if my project needs it)

Refer to the original project for [documentation](https://github.com/RallyTools/RallyRestToolkitForJava/wiki/User-Guide)

[Web Services API documentation](https://rally1.rallydev.com/slm/doc/webservice)
