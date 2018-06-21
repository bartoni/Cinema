import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MOVIES")
public class Movie implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private String length;
    private String minAge;
    private Set<Show> showSet = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "LENGTH")
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Column(name = "MINAGE")
    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    @OneToMany
    public Set<Show> getShowSet() {
        return showSet;
    }

    public void setShowSet(Set<Show> showSet) {
        this.showSet = showSet;
    }
}