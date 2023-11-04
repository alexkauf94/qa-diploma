package models;

import lombok.Data;

@Data
public class PaymentEntity {
    private String id;
    private String transaction_id;
    private String amount;
    private String status;
    private String created;

}
