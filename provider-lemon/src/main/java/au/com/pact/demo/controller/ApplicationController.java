package au.com.pact.demo.controller;

import au.com.pact.demo.model.Addresses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static au.com.pact.demo.constant.DefaultValues.CONTENT_TYPE;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ApplicationController {

    @GetMapping(value = "/addresses", produces = CONTENT_TYPE)
    @ResponseStatus(OK)
    public ResponseEntity<Addresses> retrieveAddresses(@RequestParam("keyword") String keyword) {
        Addresses addresses = new Addresses();
        if (keyword.startsWith("13")) {
            addresses.setAddress(asList("1304/7 Riverside Quay, VIC 3006", "2305/8 Riverside Quay, VIC 3006", "3306/9 Riverside Quay, VIC 3006"));
        }

        return new ResponseEntity<>(addresses, OK);
    }
}
