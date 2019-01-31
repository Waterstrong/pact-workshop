package au.com.pact.demo.controller;

import au.com.pact.demo.model.AddressResponse;
import au.com.pact.demo.model.DecisionRequest;
import au.com.pact.demo.model.DecisionResponse;
import au.com.pact.demo.service.AddressService;
import au.com.pact.demo.service.DecisionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static au.com.pact.demo.constant.DefaultValues.CONTENT_TYPE;

@RestController
@Api(tags = "Application Controller", description = "Operations on Application Controller")
public class ApplicationController {

    @Autowired
    private DecisionService decisionService;

    @Autowired
    private AddressService addressService;

    @PostMapping(value = "/application", consumes = CONTENT_TYPE, produces = CONTENT_TYPE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Check Application", notes = "This is check application endpoint.", consumes = CONTENT_TYPE, produces = CONTENT_TYPE)
    public ResponseEntity<DecisionResponse> checkApplication(@RequestBody DecisionRequest request) {
        DecisionResponse decisionResponse = decisionService.processDecision(request);
        return new ResponseEntity<>(decisionResponse, HttpStatus.OK);
    }


    @GetMapping(value = "/addresses", produces = CONTENT_TYPE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Search Addresses", notes = "This is search addresses endpoint.", produces = CONTENT_TYPE)
    public ResponseEntity<AddressResponse> searchAddresses(@RequestParam("keyword") String keyword) {
        AddressResponse addressResponse = addressService.searchAddresses(keyword);

        return new ResponseEntity<>(addressResponse, HttpStatus.OK);
    }
}
