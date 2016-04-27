package crawler;

import entities.Book;
import entities.Chapter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ruixiang on 4/16/2016.
 */
public class QParser implements Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(QParser.class);
    private static final String CHAPTER_CONTENT_ID = "chaptercontent";
    private static final String VOLUME_TITLE_BLOCK_LIST = "(.)*作品相关(.)*|(.)*公告(.)*|(.)*相关(.)*";
    private static final String CHAPTER_TITLE_BLOCK_LIST = "(.)*新书(.)*|(.)*实体书(.)*|(.)*出版(.)*";
    private static final String TITLE_SELECTOR = "h1[itemprop=\"name\"]";
    private static final String AUTHOR_SELECTOR = "span[itemprop=\"name\"]";
    private static final String CATEGORY_SELECTOR = "span[itemprop=\"genre\"]";
    private static final String COVER_IMAGE_SELECTOR = "img[itemprop=\"image\"]";
    private static final String DESCRIPTION_SELECTOR = "span[itemprop=\"description\"]";
    private static final String CATALOG_SELECTOR = "a[itemprop=\"url\"]";
    private static QParser ourInstance = new QParser();

    private QParser() {
    }

    public static QParser getInstance() {
        return ourInstance;
    }

    @Override
    public Elements parseLatestChapterList(Document doc) {
        Element content = doc.getElementById("content");
        String titleText = content.select("div[class=\"box_title\"]").first().text();
        String contentText = content.select("div[class=\"box_cont\"]").first().text();
        if (titleText.matches(VOLUME_TITLE_BLOCK_LIST) || contentText.matches(CHAPTER_TITLE_BLOCK_LIST))
            content.select("div[class=\"box_cont\"]").first().remove();
        return content.select("div[class=\"box_cont\"]").select("a");
    }

    @Override
    public Book parseBookInfo(Document doc, Book newBook) {
        Elements temp = doc.select(TITLE_SELECTOR);
        if (temp.isEmpty())
            // TODO should email the developer, because schema changed cannot be recover.
            LOGGER.error("website schema changed: " + TITLE_SELECTOR);
        else newBook.setTitle(temp.first().ownText());
        temp = doc.select(AUTHOR_SELECTOR);
        if (temp.isEmpty())
            LOGGER.error("website schema changed: " + AUTHOR_SELECTOR);
        else newBook.setAuthor(temp.first().ownText());
        temp = doc.select(CATEGORY_SELECTOR);
        if (temp.isEmpty())
            LOGGER.error("website schema changed: " + CATEGORY_SELECTOR);
        else newBook.setCategory(temp.first().ownText());
        temp = doc.select(CATALOG_SELECTOR);
        if (temp.isEmpty())
            LOGGER.error("website schema changed: " + CATALOG_SELECTOR);
        else newBook.setCatalogUrl(temp.first().attr("href"));
        temp = doc.select(DESCRIPTION_SELECTOR);
        if (temp.isEmpty())
            LOGGER.error("website schema changed: " + DESCRIPTION_SELECTOR);
        else newBook.setDescription(temp.first().ownText());
        temp = doc.select(COVER_IMAGE_SELECTOR);
        if (temp.isEmpty())
            LOGGER.error("website schema changed: " + COVER_IMAGE_SELECTOR);
        else newBook.setCoverImageLink(temp.first().attr("src"));
        return newBook;
    }

    @Override
    public boolean tryParseChapterContent(Document chapterDoc, Chapter newChapter) {
        Element eContent = chapterDoc.getElementById(CHAPTER_CONTENT_ID);
        if (eContent == null) {
//            TODO should email the developer
            LOGGER.error("cannot find the content by id, schema changed: " + CHAPTER_CONTENT_ID);
            return false;
        }
        Element scriptTab = eContent.child(1);
        String contentLink = scriptTab.attr("src");
        URL contentURL;
        BufferedReader in = null;
        String inputLine;
        StringBuilder chapterContent = new StringBuilder(3500);
        try {
            contentURL = new URL(contentLink);
            in = new BufferedReader(
                    new InputStreamReader(contentURL.openStream(), "gb2312"));
            while ((inputLine = in.readLine()) != null)
                chapterContent.append(inputLine);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return false;
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
        newChapter.setContent(chapterContent.toString().substring(16, chapterContent.toString().length() - 3));
        return true;
    }

    @Override
    public boolean tryParseChapterInfo(Element eChapter, Chapter newChapter) {
        String timeAndCount = eChapter.attr("title");
        if (!timeAndCount.isEmpty()) {
            newChapter.setWordCount(parseWordCounts(timeAndCount));
            newChapter.setUpdateDate(parseUpdateInfo(timeAndCount));
            newChapter.setChapterTitle(eChapter.text());
            return true;
        }
//        TODO should email the developer about the schema changed
        LOGGER.error("schema changed: " + "time and date selector");
        return false;
    }

    private LocalDate parseUpdateInfo(String timeAndCount) {
        String[] date = null;
        Pattern p = Pattern.compile("\\d+-\\d+-\\d+");
        Matcher m = p.matcher(timeAndCount);
        if (m.find())
            date = m.group().split("-");
        int year = Integer.parseInt(date != null ? date[0] : null);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);
        return LocalDate.of(year, month, day);
    }

    private int parseWordCounts(String timeAndCount) {
        Pattern p = Pattern.compile("\\d{2,5}");
        Matcher m = p.matcher(timeAndCount);
        if (m.find())
            return Integer.parseInt(m.group());
        return 0;
    }
}
