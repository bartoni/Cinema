import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SHOWS")
public class Show implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Integer id;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "TIME")
    private String time;

    @ManyToMany
    @JoinTable(name = "HALLS_FOR_SHOWS", joinColumns = @JoinColumn(name = "SHOW_ID"), inverseJoinColumns = @JoinColumn(name = "HALL_ID"))
    private Set<Hall> halls = new HashSet<Hall>();

    public Set<Hall> getHalls() {
        return halls;
    }

    public void setHalls(Set<Hall> halls) {
        this.halls = halls;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
