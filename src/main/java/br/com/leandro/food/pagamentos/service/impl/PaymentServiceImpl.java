package br.com.leandro.food.pagamentos.service.impl;

import br.com.leandro.food.pagamentos.domain.Payment;
import br.com.leandro.food.pagamentos.domain.enumaration.Status;
import br.com.leandro.food.pagamentos.dto.PaymentDto;
import br.com.leandro.food.pagamentos.http.OrderClient;
import br.com.leandro.food.pagamentos.repository.PaymentRepository;
import br.com.leandro.food.pagamentos.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderClient orderClient;

    @Override
    public Page<PaymentDto> allPayments(Pageable page) {
        return repository
                .findAll(page)
                .map(p -> modelMapper.map(p, PaymentDto.class));
    }

    @Override
    public PaymentDto findById(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(payment, PaymentDto.class);
    }

    @Override
    public PaymentDto createPayment(PaymentDto dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setStatus(Status.CREATED);
        repository.save(payment);

        return modelMapper.map(payment, PaymentDto.class);
    }

    @Override
    public PaymentDto updatePayment(Long id, PaymentDto dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setId(id);
        Payment updatedPayment = repository.save(payment);
        return modelMapper.map(updatedPayment, PaymentDto.class);
    }

    @Override
    public void DeletePayment(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void confirmPayment(Long id){
        Optional<Payment> payment = repository.findById(id);

        if (!payment.isPresent()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED);
        repository.save(payment.get());
        orderClient.approvePaymentOrder(payment.get().getOrderId());
    }

    @Override
    public void changeStatus(Long id) {
        Optional<Payment> payment = repository.findById(id);

        if (!payment.isPresent()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED_WITHOUT_INTEGRATION);
        repository.save(payment.get());
    }


}
