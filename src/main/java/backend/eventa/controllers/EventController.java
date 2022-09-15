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

    /**
     * @return
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

    /**
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/user/{id}")
    public List<Event> retriveAllUsersEvent(@PathVariable Long id) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new Exception("id: " + id);
        }
        return userOptional.get().getMyEvents();
    }

    /**
     * @param addEventRequest
     * @param authentication
     * @return
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

    /**
     * @param updateEventRequest
     * @param authentication
     * @return
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

    @DeleteMapping("/{someID}")
    public @ResponseBody MessageResponse deleteEvent(@PathVariable(value="someID") Long id,  Authentication authentication) {

        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        eventRepository.deleteById(id);

        return new MessageResponse("Delete successfully completed.");
    }

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
