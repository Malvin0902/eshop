package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {
    private final List<Payment> payments = new ArrayList<>();

    public void save(Payment payment) {
        payments.add(payment);
    }

    public Optional<Payment> findById(String paymentId) {
        return payments.stream()
                .filter(payment -> payment.getId().equals(paymentId))
                .findFirst();
    }

    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }
}