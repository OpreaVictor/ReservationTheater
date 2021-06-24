package application.models.models;
import java.sql.Date;
import java.sql.Time;

public class TheaterModel {
    private int idevent;
    private String name;
    private Date date;
    private Time hour;

    public TheaterModel(String name, Date date, Time hour) {
        this.name = name;
        this.date = date;
        this.hour = hour;
    }

    public int getId() {
        return idevent;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Time getHour() {
        return hour;
    }

}
