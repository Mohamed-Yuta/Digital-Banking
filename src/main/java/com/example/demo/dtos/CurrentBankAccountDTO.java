package com.example.demo.dtos;


import com.example.demo.enums.AccountStatus;
import lombok.*;

import java.util.Date;


@Data @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class CurrentBankAccountDTO extends BankAccountDTO {



    private String id ;
    private double balance  ;
    private Date createdAt ;

    private AccountStatus status ;

    private CustomerDTO customerDTO ;
    private double overDraft ;
}
