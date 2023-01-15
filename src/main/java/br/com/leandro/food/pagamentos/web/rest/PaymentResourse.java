package br.com.leandro.food.pagamentos.web.rest;

import br.com.leandro.food.pagamentos.dto.PaymentDto;
import br.com.leandro.food.pagamentos.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class PaymentResourse {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payments")
    public Page<PaymentDto> payments(@PageableDefault(size = 10) Pageable page) {
        return paymentService.allPayments(page);
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDto> payment(@PathVariable @NotNull Long id) {
        PaymentDto dto = paymentService.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentDto> create(@RequestBody @Valid PaymentDto dto, UriComponentsBuilder uriBuilder) {
        PaymentDto payment = paymentService.createPayment(dto);
        URI address = uriBuilder.path("/payments/{id}").buildAndExpand(payment.getId()).toUri();

        return ResponseEntity.created(address).body(payment);
    }

    @PutMapping("/payments/{id}")
    public ResponseEntity<PaymentDto> update(@PathVariable @NotNull Long id, @RequestBody @Valid PaymentDto dto) {
        PaymentDto updatedPayment = paymentService.updatePayment(id, dto);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<PaymentDto> remover(@PathVariable @NotNull Long id) {
        paymentService.DeletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/payments/{id}/confirm")
    @CircuitBreaker(name = "updateOrder", fallbackMethod = "confirmedPaymentWithIntegrationPending")
    public void confirmPayment(@PathVariable @NotNull Long id){
        paymentService.confirmPayment(id);
    }

    public void confirmedPaymentWithIntegrationPending(@PathVariable @NotNull Long id){
        paymentService.changeStatus(id);
    }

}
