package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static crawler.Utilities.sanitizeChapterTitle;

/**
 * Created by Ruixiang on 4/14/2016.
 */
public class Fetcher {

    private static final String BING_QUERY_STRING = "http://www.bing.com/search?q=@@@@&count=20&go=Submit&qs=n&form=QBLH&scope=web&mkt=zh-CN&pq=@@@@";
    private static final Logger LOGGER = LoggerFactory.getLogger(Fetcher.class);
    private static Fetcher ourInstance = new Fetcher();

    private Fetcher() {
    }

    public static Fetcher getInstance() {
        return ourInstance;
    }

    public Document fetchHTMLDocument(String url) {
        Document doc = null;
        if (!url.startsWith("http") && !url.startsWith("https")) {
            url = "http://" + url;
        }
        try {
            doc = Jsoup.connect(url).timeout(15000).get();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return doc;
    }

    public Document fetchBingSearchResult(String bookTitle, String chapterTitle) {
        String cleanedTitle = sanitizeChapterTitle(chapterTitle);
        String encodedQuery;
        try {
            encodedQuery = URLEncoder.encode(bookTitle + ' ' + cleanedTitle, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.debug(e.getMessage());
            return null;
        }
        return fetchHTMLDocument(BING_QUERY_STRING.replaceAll("@@@@", encodedQuery));
    }
}
