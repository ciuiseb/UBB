package triathlon.model;
import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
@Table(name = "Events")
public class EventEntity extends triathlon.model.Entity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INTEGER")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "location", nullable = false)
    private String location;
    @Column(name = "description")
    private String description;

    public EventEntity(Long aLong, String name, String date, String location, String description) {
        this.id = aLong;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }
    public EventEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
