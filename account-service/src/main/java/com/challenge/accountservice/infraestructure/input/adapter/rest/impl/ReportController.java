package com.challenge.accountservice.infraestructure.input.adapter.rest.impl;

import com.challenge.accountservice.application.input.port.ReportUseCase;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.AccountStatementDTO;
import com.challenge.accountservice.infraestructure.input.adapter.rest.mapper.AccountMapperRest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportUseCase reportUseCase;
    private final AccountMapperRest mapperRest;

    @GetMapping("/{customerId}")
    public Flux<AccountStatementDTO> getReport(@PathVariable Long customerId,
                                               @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                               @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.info("Received request to get report for customer {} from {} to {}", customerId, from, to);
        return Mono.fromCallable(() -> reportUseCase.generateReport(customerId, from, to))
                .doOnNext(list -> log.info("Returning {} report entries", list.size()))
                .flatMapMany(list -> Flux.fromStream(list.stream().map(mapperRest::toDtoFromReport)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
