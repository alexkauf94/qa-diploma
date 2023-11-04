package models;

import lombok.Data;

@Data
public class CreditRequestEntity {
    private String id;
    private String bank_id;
    private String status;
    private String created;
}
