import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HALLS")
public class Hall {

    private Integer id;
    private String number;
    private String seats;
    private String type;
    private Set<Show> showSet = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HALL_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NUMBER")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "SEATS")
    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @OneToMany
    public Set<Show> getShowSet() {
        return showSet;
    }

    public void setShowSet(Set<Show> showSet) {
        this.showSet = showSet;
    }
}
