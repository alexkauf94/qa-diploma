package data;

import lombok.Data;

@Data
public class OrderEntity {
    private String id;
    private String credit_id;
    private String payment_id;
    private String created;

}
