import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SHOWS")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Integer id;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "HALL")
    protected String hall;

    @Column(name = "DATE")
    protected ZonedDateTime date;

    @OneToOne
    protected List<Show> shows;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public ZonedDateTime getDate() { return date; }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
