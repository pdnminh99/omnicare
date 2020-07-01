package com.thcntt3.omnicare.module;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.Timestamp;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/module")
public class ModuleController {

    private final ModuleService service;

    @Autowired
    public ModuleController(ModuleService service) {
        this.service = service;
    }

//    @GetMapping("{uid}")
//    public List<Module> getAll() throws ExecutionException, InterruptedException {
//        return service.getAll();
//    }

//    @GetMapping("{uid}/{MAC}")
//    public Module connect(@PathVariable String uid,
//                          @PathVariable String MAC) {
//        return service.connect(uid, MAC);
//    }
//
//    @DeleteMapping("{uid}/{MAC}")
//    public void disconnect(@PathVariable String uid,
//                           @PathVariable String MAC) throws ExecutionException, InterruptedException {
//        service.disconnect(uid, MAC);
//    }

    @GetMapping(value = "create/{MAC}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String create(@PathVariable String MAC,
                         @Nullable
                         @RequestParam String name) throws FirebaseAuthException {
        return service.create(MAC, name);
    }

    @GetMapping(value = "token/{MAC}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String refreshToken(@PathVariable String MAC,
                               @Nullable
                               @RequestParam String name) throws ExecutionException, InterruptedException, FirebaseAuthException {
        return service.getNewToken(MAC, name);
    }

    private void transformComponent(String raw) {
        String[] rawData = raw.split(" ");

        if (rawData.length < 5) {
            throw new IllegalArgumentException("Not enough input data.");
        }
        token = rawData[0];
        pinNumber = Integer.parseInt(rawData[1]);
        long epoch = Long.parseLong(rawData[2]);
        ComponentType type = ComponentType.fromText(rawData[3]);
        String stat = rawData[4];
        Timestamp createdAt = Timestamp.ofTimeMicroseconds(epoch * 1_000_000);
//        Timestamp createdAt = Timestamp.now();
        component = new RawData(stat, type, createdAt);
    }

    private String token;

    private int pinNumber;

    private RawData component;

    // token | pin epoch component data (temp-humid)
    @PostMapping("{MAC}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String MAC,
                       @NotNull
                       @NotEmpty
                       @RequestBody String arduinoRaw,
                       @RequestParam(defaultValue = "10") int maximumDataCount) throws ExecutionException, InterruptedException, FirebaseMessagingException, JsonProcessingException {
        transformComponent(arduinoRaw);
        service.updateComponent(token, MAC, pinNumber, maximumDataCount, component);
        service.commit();
//        service.publishMessage();
        service.flush();
        token = null;
    }

    private static class GeneralResponse {
        @JsonIgnore
        public Timestamp executedAt;

        @JsonGetter("executedAt")
        public Long getExecutedAt() {
            return executedAt == null ?
                    null :
                    executedAt.toDate().getTime();
        }

        public String message;

        private GeneralResponse(Timestamp executedAt, String message) {
            this.executedAt = executedAt;
            this.message = message;
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public GeneralResponse handleIllegalArgument(IllegalArgumentException exception) {
        return new GeneralResponse(Timestamp.now(), exception.getMessage());
    }

    @ExceptionHandler({ExecutionException.class, InterruptedException.class, FirebaseAuthException.class, InterruptedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralResponse handleGeneralExceptions() {
        return new GeneralResponse(Timestamp.now(), "Server error!");
    }
}
