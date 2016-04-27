package dao;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoDatabase;
import entities.Book;
import entities.Chapter;
import org.bson.Document;
import org.bson.types.ObjectId;

import static java.util.Arrays.asList;

/**
 * Created by Ruixiang on 4/18/2016.
 */
public class DAO {

    private static DAO ourInstance = new DAO();
    private final MongoDatabase db;
    private final Gson gson;

    private DAO() {
        db = new MongoClient().getDatabase("novelAggregator");
        gson = new Gson();
    }

    public static DAO getInstance() {
        return ourInstance;
    }

    public AggregateIterable<Document> getUpdateList() {
        return db.getCollection("novels").aggregate(asList(
                new Document("$match", new Document("status", new Document("$ne", "COMPLETED"))),
                new Document("$project", new Document("title", 1).append("url", 1).append("catalogUrl", 1).append("lastUpdate", 1).append("status", 1))));
    }

    public void insertNewBook(Book book) {
        String json = gson.toJson(book, Book.class);
        Document document = Document.parse(json);
        db.getCollection("novels").insertOne(document);
    }

    public void insertNewChapter(ObjectId objectId, Chapter chapter) {
        String json = gson.toJson(chapter, Chapter.class);
        Document document = Document.parse(json);
        document.append("_id", new ObjectId());
        db.getCollection("novels").updateOne(new Document("_id", objectId), new Document("$push", new Document("chapterList", document)));
    }

    public int getChapterNumsByID(ObjectId id) {
        AggregateIterable<Document> aggregateIterable = db.getCollection("novels").aggregate(asList(
                new Document("$match", new Document("_id", id)),
                new Document("$project", new Document("chaptersInDB", new Document("$size", "$chapterList")))));
        return aggregateIterable.first().getInteger("chaptersInDB");
    }
}
