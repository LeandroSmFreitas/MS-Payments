package br.com.leandro.food.pagamentos.repository;

import br.com.leandro.food.pagamentos.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
