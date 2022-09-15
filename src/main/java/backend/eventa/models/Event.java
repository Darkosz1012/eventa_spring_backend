package backend.eventa.models;

import backend.eventa.payload.request.AddEventRequest;
import backend.eventa.payload.request.UpdateEventRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(name = "owner_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User owner;




    @ManyToMany(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(name = "participant_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> participants = new ArrayList<>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;


    @Size(max = 500)
    private String description;


    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 200)
    private String address;

    @NotBlank
    @Size(max = 100)
    private String type;


    @Size(max = 200)
    private String img;

    @NotNull
    private Long max;

    @NotNull
    @Temporal(TemporalType.DATE)
    private java.util.Date start_date;

    @NotNull
    @Temporal(TemporalType.DATE)
    private java.util.Date end_date;

    //@Formula("(select count(o.creation_date) from Events e inner join e.participants p where e.id = this.id)")
//    @Formula("(SELECT COUNT(i.event_id) FROM participant_event i WHERE id = i.event_id)")
    @Formula("(SELECT COUNT(i.event_id) FROM participant_event i WHERE id = i.event_id)")
    private Long participantsCount;

    @JsonInclude()
    @Transient
    private Boolean joined = false;

    public Event() {
    }

    public Event(AddEventRequest req){
        this.name = req.getName();
        this.description = req.getDescription();
        this.city = req.getCity();
        this.address = req.getAddress();
        this.start_date = req.getStart_date();
        this.end_date = req.getEnd_date();
        this.type = req.getType();
        this.img = req.getImg();
        this.max = req.getMax();
    }

    public void update(UpdateEventRequest req){
        this.name = req.getName();
        this.description = req.getDescription();
        this.city = req.getCity();
        this.address = req.getAddress();
        this.start_date = req.getStart_date();
        this.end_date = req.getEnd_date();
        this.type = req.getType();
        this.img = req.getImg();
        this.max = req.getMax();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public void addParticipant(User participants){
        this.participants.add(participants);

    }
    public void removeParticipant(User participants){
        this.participants.remove(participants);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Long getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(Long participantsCount) {
        this.participantsCount = participantsCount;
    }

    public Boolean getJoined() {
        return joined;
    }

    public void setJoined(Boolean joined) {
        this.joined = joined;
    }
}
