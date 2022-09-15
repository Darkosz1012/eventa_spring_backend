package backend.eventa.repository;

import backend.eventa.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Override
    List<Event> findAll();

    @Query("SELECT e FROM Event e inner join e.participants p where p.id = ?1")
    List<Event> findJoinEvents(long user_id);

    @Query("SELECT e FROM Event e inner join e.owner o where o.id = ?1")
    List<Event> findEventsToEdit(long user_id);
}
