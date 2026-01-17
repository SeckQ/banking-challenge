package com.challenge.accountservice.infraestructure.output.client;

import com.challenge.accountservice.application.output.port.CustomerClient;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CustomerClientImpl implements CustomerClient {

    private final WebClient customerWebClient;

    @Override
    public String getCustomerNameById(Long clientId) {
        return customerWebClient
                .get()
                .uri("/{id}", clientId)
                .retrieve()
                .bodyToMono(CustomerDTO.class)
                .map(CustomerDTO::getName)
                .onErrorReturn("UNKNOWN")
                .block();
    }
}
