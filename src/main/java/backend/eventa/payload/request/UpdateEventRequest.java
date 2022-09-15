package backend.eventa.payload.request;

import javax.validation.constraints.NotNull;

public class UpdateEventRequest extends AddEventRequest{
    @NotNull
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
