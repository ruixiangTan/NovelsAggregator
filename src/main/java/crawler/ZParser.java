package crawler;

import entities.Book;
import entities.Chapter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Ruixiang on 4/16/2016.
 */
public class ZParser implements Parser {

    private static ZParser ourInstance = new ZParser();

    private ZParser() {
    }

    public static ZParser getInstance() {
        return ourInstance;
    }

    @Override
    public Elements parseLatestChapterList(Document doc) {
        return null;
    }

    @Override
    public Book parseBookInfo(Document doc, Book newBook) {
        return null;
    }

    @Override
    public boolean tryParseChapterContent(Document chapterDoc, Chapter chapter) {
        return false;
    }

    @Override
    public boolean tryParseChapterInfo(Element eChapter, Chapter chapter) {
        return false;
    }
}
