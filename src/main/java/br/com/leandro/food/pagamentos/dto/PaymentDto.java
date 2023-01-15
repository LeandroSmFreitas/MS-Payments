package br.com.leandro.food.pagamentos.dto;

import br.com.leandro.food.pagamentos.domain.enumaration.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {

    private Long id;


    private Double value;


    private String name;


    private String number;


    private String expiration;


    private String code;


    private Status status;


    private Long orderId;


    private Long paymentMethodId;
}
