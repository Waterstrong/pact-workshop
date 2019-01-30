package au.com.pact.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ApplicationController {

    @GetMapping(value = "/addresses")
    @ResponseStatus(OK)
    public ResponseEntity<List<String>> retrieveAddresses(@PathParam("keyword") String keyword) {

        List<String> addresses = asList("1304/7 Riverside Quay, VIC 3006", "2305/8 Riverside Quay, VIC 3006", "3306/9 Riverside Quay, VIC 3006");

        return new ResponseEntity<>(addresses, OK);
    }
}
