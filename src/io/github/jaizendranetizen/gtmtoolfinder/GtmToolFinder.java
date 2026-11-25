package io.github.jaizendranetizen.gtmtoolfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Offline search over the public OneGTMLab tool directory
 * (https://www.onegtmlab.dev/tool-directory): API reviews of sales and marketing tools,
 * head-to-head comparisons and GTM glossary terms. The index ships inside the jar and no
 * data is sent anywhere.
 */
public final class GtmToolFinder {
    private static List<Entry> all;

    private GtmToolFinder() {}

    /** Every entry of the directory. */
    public static synchronized List<Entry> all() {
        if (all == null) {
            List<Entry> out = new ArrayList<>();
            try (InputStream in = GtmToolFinder.class.getResourceAsStream("/gtm-tool-finder/index.tsv");
                 BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) {
                    String[] p = line.split("\t", 4);
                    if (p.length == 4) {
                        out.add(new Entry(p[0], p[1], p[2], p[3]));
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            all = Collections.unmodifiableList(out);
        }
        return all;
    }

    private static int kindOrder(String kind) {
        switch (kind) {
            case "Tool": return 0;
            case "Category": return 1;
            case "Comparison": return 2;
            default: return 3;
        }
    }

    /**
     * Entries whose name or description contains every word of the query.
     *
     * @param query words to look for
     * @param kind  Tool, Category, Comparison or Glossary (case-insensitive); null or empty means all
     * @param limit maximum number of results; 0 or less means 20
     */
    public static List<Entry> search(String query, String kind, int limit) {
        int cap = limit <= 0 ? 20 : limit;
        String[] words = query == null ? new String[0] : query.toLowerCase(Locale.ROOT).trim().split("\\s+");
        List<Entry> hits = new ArrayList<>();
        for (Entry e : all()) {
            if (kind != null && !kind.isEmpty() && !e.kind.equalsIgnoreCase(kind)) {
                continue;
            }
            String text = (e.name + " " + e.description).toLowerCase(Locale.ROOT);
            boolean ok = true;
            for (String w : words) {
                if (!w.isEmpty() && !text.contains(w)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                hits.add(e);
            }
        }
        final String first = words.length > 0 ? words[0] : "";
        hits.sort(Comparator
                .comparingInt((Entry e) -> !first.isEmpty() && e.name.toLowerCase(Locale.ROOT).startsWith(first) ? 0 : 1)
                .thenComparingInt(e -> kindOrder(e.kind)));
        return hits.size() > cap ? new ArrayList<>(hits.subList(0, cap)) : hits;
    }

    /** Command line: gtm-tool-finder [--kind Tool|Category|Comparison|Glossary] words... */
    public static void main(String[] args) {
        String kind = "";
        StringBuilder q = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if ("--kind".equals(args[i]) && i + 1 < args.length) {
                kind = args[++i];
            } else {
                q.append(args[i]).append(' ');
            }
        }
        List<Entry> hits = search(q.toString(), kind, 20);
        for (Entry e : hits) {
            System.out.println(e);
        }
        if (hits.isEmpty()) {
            System.err.println("no match");
            System.exit(1);
        }
    }
}
