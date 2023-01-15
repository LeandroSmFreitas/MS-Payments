package br.com.leandro.food.pagamentos.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ms-order")
public interface OrderClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/api/orders/{id}/pago")
    void approvePaymentOrder(@PathVariable Long id);
}
