package io.github.jaizendranetizen.gtmtoolfinder;

/** One page of the OneGTMLab tool directory. */
public final class Entry {
    /** Page name. */
    public final String name;
    /** Tool, Category, Comparison or Glossary. */
    public final String kind;
    /** Public URL of the page on onegtmlab.dev. */
    public final String url;
    /** Short description of the page. */
    public final String description;

    Entry(String name, String kind, String url, String description) {
        this.name = name;
        this.kind = kind;
        this.url = url;
        this.description = description;
    }

    @Override
    public String toString() {
        return "[" + kind + "] " + name + " - " + url;
    }
}
