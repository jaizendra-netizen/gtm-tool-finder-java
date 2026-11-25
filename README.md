# gtm-tool-finder (Java)

Search the [OneGTMLab](https://www.onegtmlab.dev) GTM tool directory from Java or your terminal:

- API reviews for 66 sales and marketing tools: pricing, rate limits, keys and MCP support
- 99 head-to-head tool comparisons, such as Apollo vs Clay
- 110 plain-English definitions of GTM terms

```xml
<dependency>
  <groupId>io.github.jaizendra-netizen</groupId>
  <artifactId>gtm-tool-finder</artifactId>
  <version>1.0.0</version>
</dependency>
```

```java
import io.github.jaizendranetizen.gtmtoolfinder.*;

for (Entry e : GtmToolFinder.search("enrichment", "Tool", 10)) {
    System.out.println(e.name + " " + e.url);
}
```

Command line: `java -jar gtm-tool-finder-1.0.0.jar --kind comparison clay`

Every result links to the full page on [onegtmlab.dev/tool-directory](https://www.onegtmlab.dev/tool-directory). The list is packaged in the jar, so it works offline and sends no data anywhere.

Built by [OneGTMLab](https://www.onegtmlab.dev), a GTM engineering agency. MIT licensed.
