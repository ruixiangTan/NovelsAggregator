import com.mongodb.client.AggregateIterable;
import crawler.novelCrawler;
import dao.DAO;
import org.bson.Document;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//import org.jsoup.nodes.Document;

/**
 * Created by Ruixiang on 4/13/2016.
 */
public class Launcher {

    public static void main(String[] args) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        AggregateIterable<Document> booksNeededUpdate = DAO.getInstance().getUpdateList();
        for (Document bookInfo : booksNeededUpdate) {
            scheduler.scheduleWithFixedDelay(new novelCrawler(bookInfo), 0, 12, TimeUnit.MINUTES);
        }
    }
}
