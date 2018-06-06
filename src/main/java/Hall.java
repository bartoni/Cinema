import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HALLS")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Integer id;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "SEATS")
    private String seats;

    @Column(name = "TYPE")
    private String type;

    @ManyToMany(mappedBy = "halls")
    private Set<Show> shows = new HashSet<Show>();

    public Set<Show> getShows() {
        return shows;
    }

    public void setShows(Set<Show> shows) {
        this.shows = shows;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
