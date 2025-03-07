package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {
    private final List<Payment> payments = new ArrayList<>();

    public void save(Payment payment) {}

    public Optional<Payment> findById(String paymentId) {return null;}

    public List<Payment> findAll() {return null;}
}