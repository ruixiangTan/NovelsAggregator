package entities;

/**
 * Created by Ruixiang on 4/12/2016.
 */
public class Statistics {

    private int totalVisits;
    private int monthlyVisits;
    private int totalRecommends;
    private int monthlyRecommends;
    private int totalWords;

    public int getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(int totalVisits) {
        this.totalVisits = totalVisits;
    }

    public int getMonthlyVisits() {
        return monthlyVisits;
    }

    public void setMonthlyVisits(int monthlyVisits) {
        this.monthlyVisits = monthlyVisits;
    }

    public int getTotalRecommends() {
        return totalRecommends;
    }

    public void setTotalRecommends(int totalRecommends) {
        this.totalRecommends = totalRecommends;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getMonthlyRecommends() {
        return monthlyRecommends;
    }

    public void setMonthlyRecommends(int monthlyRecommends) {
        this.monthlyRecommends = monthlyRecommends;
    }
}
