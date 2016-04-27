package entities;

import org.bson.types.ObjectId;

import java.time.LocalDate;

/**
 * Created by Ruixiang on 4/12/2016.
 */
public class Chapter {

    private final ObjectId id;
    private String chapterTitle;
    private String content;
    private int wordCount;
    private LocalDate updateDate;

    private Chapter(ObjectId id) {
        this.id = id;
    }

    public static Chapter createNewChapter() {
        return new Chapter(null);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public ObjectId getId() {
        return id;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }
}
