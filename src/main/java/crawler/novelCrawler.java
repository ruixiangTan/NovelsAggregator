package crawler;

import dao.DAO;
import entities.Chapter;
import org.bson.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static crawler.Utilities.isVIPChapter;
import static crawler.Utilities.sanitizeBingLink;

/**
 * Created by Ruixiang on 4/14/2016.
 */
public class novelCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(novelCrawler.class);
    private final Fetcher fetcher;
    private final Parser parser;
    private final Document bookInfo;

    public novelCrawler(Document bookInfo) {
        this.bookInfo = bookInfo;
        fetcher = Fetcher.getInstance();
        parser = bookInfo.getString("url").contains("qidian") ? QParser.getInstance() : ZParser.getInstance();
    }

    @Override
    public void run() {
        int chaptersInDB = DAO.getInstance().getChapterNumsByID(bookInfo.getObjectId("_id"));
        String catalogUrl = bookInfo.getString("catalogUrl");
        org.jsoup.nodes.Document catalogDoc = fetcher.fetchHTMLDocument(catalogUrl);
        if (catalogDoc == null)
            return;

        Elements latestChapterList = parser.parseLatestChapterList(catalogDoc);
        if (latestChapterList.isEmpty()) {
            logger.debug("website schema changed, cannot get chapter list");
            return;
        }
        if (latestChapterList.size() > chaptersInDB) {
            fetchNewChapters(latestChapterList, latestChapterList.size() - chaptersInDB);
        } else if (latestChapterList.size() == chaptersInDB) {
            //TODO means the book is up to date!
        } else logger.debug("latest chapter list has less elements than in the database!");
    }

    private void fetchNewChapters(Elements latestChapterList, int howMany) {
        for (int i = latestChapterList.size() - howMany; i < latestChapterList.size(); i++) {
            if (isVIPChapter(latestChapterList.get(i))) {
                if (!tryFetchVIPChapter(latestChapterList.get(i)))
                    break;
            } else {
                if (!tryFetchFreeChapter(latestChapterList.get(i)))
                    break;
            }
        }
    }

    private boolean tryFetchFreeChapter(Element eChapter) {
        Chapter newChapter = Chapter.createNewChapter();
        String chapterLink = eChapter.attr("href");
        org.jsoup.nodes.Document chapterDoc = fetcher.fetchHTMLDocument(chapterLink);
        if (chapterDoc == null)
            return false;
        if (parser.tryParseChapterContent(chapterDoc, newChapter) && parser.tryParseChapterInfo(eChapter, newChapter)) {
            DAO.getInstance().insertNewChapter(bookInfo.getObjectId("_id"), newChapter);
            return true;
        }
        return false;
    }

    private boolean tryFetchVIPChapter(Element eChapter) {
        Chapter newChapter = Chapter.createNewChapter();
        if (!parser.tryParseChapterInfo(eChapter, newChapter))
            return false;
        org.jsoup.nodes.Document searchResultDoc = fetcher.fetchBingSearchResult(bookInfo.getString("title"), eChapter.text());
        if (searchResultDoc == null)
            return false;
        Elements bingElements = parser.parseVIPChaptersFromBing(searchResultDoc);
        if (bingElements.isEmpty()) {
            logger.debug("schema changed: searchResultDoc");
            return false;
        }
        for (Element bingLink : bingElements) {
            if (Utilities.isOriginalSite(bingLink))
                continue;
            String domain = Utilities.getDomainByURL(bingLink.text());
            String cssSelector = Utilities.getCssSelectorBySite(domain);
            if (cssSelector == null)
                continue;
            org.jsoup.nodes.Document contentDoc = fetcher.fetchHTMLDocument(sanitizeBingLink(bingLink.text()));
            if (contentDoc == null)
                continue;
            if (parser.tryVIPChapterContent(contentDoc, newChapter, cssSelector)) {
                DAO.getInstance().insertNewChapter(bookInfo.getObjectId("_id"), newChapter);
                return true;
            }
            logger.error("parsing vip chapter failed, websites schema changed: " + bingLink.text());
        }
        return false;
    }

}
