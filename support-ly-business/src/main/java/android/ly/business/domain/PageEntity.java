package android.ly.business.domain;

/**
 * Create by LingYan on 2017-10-12
 */

public class PageEntity<T> extends ListEntity<T> {
    public int nextPage;

    public boolean hasNoMore(int limit) {
        return results != null && !results.isEmpty() && results.size() < limit;
    }
}
