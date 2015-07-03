package Client.Core;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Алексей on 26.03.2015.
 */
public class SimpleElement {// implements Comparable<ClientB.SimpleElement>

    Calendar calendar = Calendar.getInstance();

    public Calendar getCalendar() {
        return calendar;
    }

    public Calendar setCalendar(int year, int month, int day, int hour, int min) {
        calendar.set(year, month, day, hour, min);
        return calendar;
    }

    private String title;
    private String content;
    private String date;

    public Date getCurrentDate() {
        return currentdate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentdate = currentDate;
    }

    private Date currentdate;
    private String index;

//    @Override
//    public int compareTo(Client.Core.SimpleElement o) {
//        return title.compareTo(o.getTitle());
//    }

    public SimpleElement(String ... allArgs) {

        String args [] = new String [4];
        for (int i = 0; i < allArgs.length; i++) {
            args [i] = allArgs[i];
        }
        this.title = args[0];
        this.content = args[1];
        this.currentdate = new Date();
        this.date = args[2];
        this.index = args[3];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean equals (Object simpleElement) {
        SimpleElement s = (SimpleElement) simpleElement;
        return getTitle().equals(s.getTitle());
    }

    public int hashCode(){
        return title.hashCode();
    }
}
