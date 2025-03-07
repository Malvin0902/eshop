package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.method = method;
        this.paymentData = paymentData;
        this.status = validatePayment(method, paymentData);
    }

    private String validatePayment(String method, Map<String, String> paymentData) {
        if ("Voucher Code".equals(method)) {
            String voucherCode = paymentData.get("voucherCode");

            if (voucherCode != null && voucherCode.length() == 16 &&
                    voucherCode.startsWith("ESHOP") && voucherCode.replaceAll("\\D", "").length() == 8) {
                return "SUCCESS";
            } else {
                return "REJECTED";
            }

        } else if ("Cash on Delivery".equals(method)) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");

            if (address == null || address.isEmpty() || deliveryFee == null || deliveryFee.isEmpty()) {
                return "REJECTED";
            }
            return "SUCCESS";
        }
        return "REJECTED";
    }

    public void setStatus(String status) {
        if ("SUCCESS".equals(status) || "REJECTED".equals(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status");
        }
    }
}