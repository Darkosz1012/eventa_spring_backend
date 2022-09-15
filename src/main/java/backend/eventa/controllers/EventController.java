package backend.eventa.controllers;


import backend.eventa.models.Event;
import backend.eventa.models.User;
import backend.eventa.payload.request.AddEventRequest;
import backend.eventa.payload.request.UpdateEventRequest;
import backend.eventa.payload.response.MessageResponse;
import backend.eventa.repository.EventRepository;
import backend.eventa.repository.UserRepository;
import backend.eventa.security.jwt.JwtUtils;
import backend.eventa.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * The controller that stores the implementation of all event-related queries.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.PUT,RequestMethod.DELETE,RequestMethod.POST,RequestMethod.OPTIONS,RequestMethod.GET})
@RestController
@RequestMapping("/api/event")
public class EventController {

    Logger logger = LoggerFactory.getLogger(EventController.class);


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**  A GET query that returns all events. Request requiring JWT.
     * @return List<Event>
     */
    @GetMapping("/")
    public List<Event> getAllEvent(Authentication authentication) {
        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        User user = userRepository.getById(user_id);
        List<Event> events=  eventRepository.findAll();
        for(int i =0; i< events.size();i++){
            events.get(i).setJoined(events.get(i).getParticipants().contains(user));
        }
        return events;
    }

    /**  A GET query returning events joined by a user saved in JWT. Request requiring JWT.
     * @param authentication
     * @return List<Event>
     */
    @GetMapping("/joined")
    public List<Event> getJoinedEvent( Authentication authentication) {
        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        User user = userRepository.getById(user_id);
        List<Event> events=  eventRepository.findJoinEvents(((UserDetailsImpl) authentication.getPrincipal()).getId());
        for(int i =0; i< events.size();i++){
            events.get(i).setJoined(events.get(i).getParticipants().contains(user));
        }
        return events;
//        return eventRepository.findJoinEvents(((UserDetailsImpl) authentication.getPrincipal()).getId());
    }

    /** Request requiring JWT. A GET query returning events created by a user saved in JWT.
     * @param authentication
     * @return List<Event>
     */
    @GetMapping("/toedit")
    public List<Event> getEventToEdit( Authentication authentication) {
        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        User user = userRepository.getById(user_id);
        List<Event> events=  eventRepository.findEventsToEdit(((UserDetailsImpl) authentication.getPrincipal()).getId());;
        for(int i =0; i< events.size();i++){
            events.get(i).setJoined(events.get(i).getParticipants().contains(user));
        }
        return events;
//        return eventRepository.findEventsToEdit(((UserDetailsImpl) authentication.getPrincipal()).getId());
    }


    /** POST query that adds an event and assigns an owner to it as a user saved in the JWT. Request requiring JWT.
     * @param addEventRequest
     * @param authentication
     * @return MessageResponse
     */
    @PostMapping("/")
    public MessageResponse addEvent(@Valid @RequestBody AddEventRequest addEventRequest, Authentication authentication) {

        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        User user = userRepository.getById(user_id);

        Event event = new Event(addEventRequest);
        event.setOwner(user);

        eventRepository.save(event);
        eventRepository.flush();
//        return event;
        return new MessageResponse("Add successfully completed.");
    }

    /** PUT query updating the event. Request requiring JWT.
     * @param updateEventRequest
     * @param authentication
     * @return MessageResponse
     */
    @PutMapping("/")
    public MessageResponse updateEvent(@Valid @RequestBody UpdateEventRequest updateEventRequest, Authentication authentication) {

        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        User user = userRepository.getById(user_id);

        Event event = eventRepository.getById(updateEventRequest.getId());

        event.update(updateEventRequest);
        event.setOwner(user);

        eventRepository.save(event);

        return new MessageResponse("Update successfully completed.");
    }

    /** DELETE query to delete an event. Request requiring JWT.
     * @param id
     * @param authentication
     * @return MessageResponse
     */
    @DeleteMapping("/{ID}")
    public @ResponseBody MessageResponse deleteEvent(@PathVariable(value="ID") Long id,  Authentication authentication) {

        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        eventRepository.deleteById(id);

        return new MessageResponse("Delete successfully completed.");
    }

    /**
     * PUT query that adds a participant who is a user registered in JWT to the event.
     * Request requiring JWT.
     * @param updateEventRequest
     * @param authentication
     * @return MessageResponse
     */
    @PutMapping("/join")
    public MessageResponse joinEvent(@Valid @RequestBody UpdateEventRequest updateEventRequest, Authentication authentication) {

        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        User user = userRepository.getById(user_id);

        Event event = eventRepository.getById(updateEventRequest.getId());

        if(event.getParticipantsCount() >= event.getMax())
            return new MessageResponse("Max Participants Number.");

        if(!event.getParticipants().contains(user))
            event.addParticipant(user);

        eventRepository.save(event);

        return new MessageResponse("Update successfully completed.");
    }

    /**
     * Zapytanie typu PUT usuwające z wydarzenia uczestnika, który jest użytkownikiem zapisanym w JWT.
     * Zapytanie wymagające JWT.
     * @param updateEventRequest
     * @param authentication
     * @return MessageResponse
     */
    @PutMapping("/resign")
    public MessageResponse resignEvent(@Valid @RequestBody UpdateEventRequest updateEventRequest, Authentication authentication) {

        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        User user = userRepository.getById(user_id);

        Event event = eventRepository.getById(updateEventRequest.getId());

        event.removeParticipant(user);

        eventRepository.save(event);

        return new MessageResponse("Update successfully completed.");
    }
}
