package au.com.pact.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class ApplicationController {

    @GetMapping(value = "/addresses")
    @ResponseStatus(OK)
    public ResponseEntity<String> retrieveAddresses(@RequestParam("keyword") String keyword) {
        String addresses = "{\"addresses\": []}";
        if (keyword.startsWith("13")) {
            addresses = "{\"addresses\": [\"1304/7 Riverside Quay, VIC 3006\", \"2305/8 Riverside Quay, VIC 3006\", \"3306/9 Riverside Quay, VIC 3006\"]}";
        }

        return new ResponseEntity<>(addresses, OK);
    }
}
