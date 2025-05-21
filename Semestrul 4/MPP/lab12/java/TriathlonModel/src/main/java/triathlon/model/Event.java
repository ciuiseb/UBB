package triathlon.model;


import java.io.Serializable;

public class Event extends Entity<Long> implements Serializable, Comparable<Event> {
    private String name;
    private String date;
    private String location;
    private String description;

    public Event() {
        super();
    }
    public Event(Long aLong, String name, String date, String location, String description) {
        super(aLong);
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }
    public Event(String name, String date, String location, String description) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                "} ";
    }

    @Override
    public int compareTo(Event o) {
        if (this.date != null && o.date != null) {
            return this.date.compareTo(o.date);
        } else if (this.date == null && o.date != null) {
            return -1;
        } else if (this.date != null && o.date == null) {
            return 1;
        }

        if (this.name != null && o.name != null) {
            return this.name.compareTo(o.name);
        } else if (this.name == null && o.name != null) {
            return -1;
        } else if (this.name != null && o.name == null) {
            return 1;
        }

        return this.getId() != null && o.getId() != null ?
                this.getId().compareTo(o.getId()) : 0;
    }
}
