package crawler;

import entities.Book;
import entities.Chapter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Ruixiang on 4/18/2016.
 */
public interface Parser {

    Elements parseLatestChapterList(Document doc);

    Book parseBookInfo(Document doc, Book newBook);

    boolean tryParseChapterContent(Document chapterDoc, Chapter chapter);

    boolean tryParseChapterInfo(Element eChapter, Chapter chapter);

    default Elements parseVIPChaptersFromBing(Document bingDoc) {
        return bingDoc.getElementById("b_results").getElementsByTag("cite");
    }

    default boolean tryVIPChapterContent(Document contentDoc, Chapter chapter, String cssSelector) {

        if (contentDoc.select(cssSelector).isEmpty())
            return false;
        String innerHTML = contentDoc.select(cssSelector).first().html();
        chapter.setContent(Utilities.sanitizeChapterContent(innerHTML));
        chapter.setWordCount(chapter.getContent().length() - 100);
        return true;
    }
}
