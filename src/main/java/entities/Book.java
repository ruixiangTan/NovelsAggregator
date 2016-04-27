package entities;

import enums.Status;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Ruixiang on 4/6/2016.
 */
public class Book {

    private final ObjectId id;
    private String title;
    private String author;
    private String category;
    private String description;
    private String url;
    private String catalogUrl;
    private String coverImageLink;
    private LocalDate lastUpdate;
    private Status status;
    private Statistics statistics;
    private Deque<Chapter> chapterList;

    public Book(ObjectId id) {
        this.id = id;
    }

    public static Book createNewBook(String url) {
        Book newBook = new Book(null);
        newBook.url = url;
        newBook.status = Status.FREE;
        newBook.chapterList = new LinkedList<>();
        return newBook;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Deque<Chapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(Deque<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCoverImageLink() {
        return coverImageLink;
    }

    public void setCoverImageLink(String coverImageLink) {
        this.coverImageLink = coverImageLink;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ObjectId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCatalogUrl() {
        return catalogUrl;
    }

    public void setCatalogUrl(String catalogUrl) {
        this.catalogUrl = catalogUrl;
    }
}
