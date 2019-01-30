package au.com.pact.demo.service;

import au.com.pact.demo.model.AddressResponse;

public interface AddressService {
    AddressResponse searchAddresses(String keyword);
}
