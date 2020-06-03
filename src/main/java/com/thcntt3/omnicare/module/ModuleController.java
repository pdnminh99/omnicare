package com.thcntt3.omnicare.module;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;
import com.google.firebase.auth.FirebaseAuthException;
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

    // token pin component data (temp-humid)
    @PostMapping("{MAC}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String MAC,
                       @NotNull
                       @NotEmpty
                       @RequestBody String arduinoRaw) throws ExecutionException, InterruptedException {
        String[] data = arduinoRaw.split(" ");

        if (data.length != 4) {
            throw new IllegalArgumentException("Not enough input data.");
        }
        String token = data[0];
        Integer pinNumber = Integer.valueOf(data[1]);
        ComponentType type = ComponentType.fromText(data[2]);

        Component component = Component.newBuilder(type)
                .setComponentId(pinNumber + "_" + MAC)
                .setPinNumber(pinNumber)
                .setData(data[3])
                .build();
        service.updateComponent(token, MAC, component);
        // pin: 0, 1, 2
        // { "key":
        // token temp humidity light "SMOKE" | "FIRE" | "SAFE" | "OFF"
//        System.out.println(content);
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
