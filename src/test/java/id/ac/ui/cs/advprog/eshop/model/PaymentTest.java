package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    private Map<String, String> voucherData;
    private Map<String, String> codData;

    @BeforeEach
    void setUp() {
        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        codData = new HashMap<>();
        codData.put("address", "123 Main Street");
        codData.put("deliveryFee", "5000");
    }

    @Test
    void testCreatePaymentWithVoucherCode_Success() {
        Payment payment = new Payment("1", "Voucher Code", voucherData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidVoucherCode_Rejected() {
        voucherData.put("voucherCode", "INVALIDCODE1234");
        Payment payment = new Payment("2", "Voucher Code", voucherData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithCashOnDelivery_Success() {
        Payment payment = new Payment("3", "Cash on Delivery", codData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithEmptyAddress_Rejected() {
        codData.put("address", "");
        Payment payment = new Payment("4", "Cash on Delivery", codData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithEmptyDeliveryFee_Rejected() {
        codData.put("deliveryFee", null);
        Payment payment = new Payment("5", "Cash on Delivery", codData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment("6", "Voucher Code", voucherData);
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment("7", "Voucher Code", voucherData);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }
}