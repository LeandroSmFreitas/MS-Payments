package br.com.leandro.food.pagamentos.service;

import br.com.leandro.food.pagamentos.dto.PaymentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    Page<PaymentDto> allPayments(Pageable page);

    PaymentDto findById(Long id);

    PaymentDto createPayment(PaymentDto dto);

    PaymentDto updatePayment(Long id, PaymentDto dto);

    void DeletePayment(Long id);

}
