package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
    }

    @Test
    void testSaveAndFindById() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("1", "Voucher Code", paymentData);

        paymentRepository.save(payment);
        Optional<Payment> retrievedPayment = paymentRepository.findById("1");

        assertTrue(retrievedPayment.isPresent());
        assertEquals("1", retrievedPayment.get().getId());
        assertEquals("Voucher Code", retrievedPayment.get().getMethod());
        assertEquals("SUCCESS", retrievedPayment.get().getStatus());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Payment> payment = paymentRepository.findById("999");

        assertFalse(payment.isPresent());
    }

    @Test
    void testFindAll() {
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("address", "123 Main St");
        paymentData2.put("deliveryFee", "5.00");

        Payment payment1 = new Payment("1", "Voucher Code", paymentData1);
        Payment payment2 = new Payment("2", "Cash on Delivery", paymentData2);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        List<Payment> payments = paymentRepository.findAll();

        assertEquals(2, payments.size());
        assertEquals("1", payments.get(0).getId());
        assertEquals("2", payments.get(1).getId());
    }

    @Test
    void testFindAll_EmptyRepository() {
        List<Payment> payments = paymentRepository.findAll();

        assertTrue(payments.isEmpty());
    }

    @Test
    void testPaymentRejectionForInvalidVoucher() {
        Map<String, String> invalidVoucherData = new HashMap<>();
        invalidVoucherData.put("voucherCode", "INVALID12345678");

        Payment payment = new Payment("3", "Voucher Code", invalidVoucherData);
        paymentRepository.save(payment);

        Optional<Payment> retrievedPayment = paymentRepository.findById("3");

        assertTrue(retrievedPayment.isPresent());
        assertEquals("REJECTED", retrievedPayment.get().getStatus());
    }

    @Test
    void testPaymentRejectionForCashOnDeliveryWithMissingData() {
        Map<String, String> invalidCodData = new HashMap<>();
        invalidCodData.put("address", "");

        Payment payment = new Payment("4", "Cash on Delivery", invalidCodData);
        paymentRepository.save(payment);

        Optional<Payment> retrievedPayment = paymentRepository.findById("4");

        assertTrue(retrievedPayment.isPresent());
        assertEquals("REJECTED", retrievedPayment.get().getStatus());
    }
}