package task.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.model.Status;


//@RequestMapping(HealthController.MAPPING)
@RestController
public class HealthController {
    public final static String MAPPING  = "/ping";

    @GetMapping(value = "/ping", produces = "application/json")
    public String ping() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        Status status = new Status();
        status.setStatus("Up");
        String jsonStatus = mapper.writeValueAsString(status);
        return jsonStatus;
    }
}

