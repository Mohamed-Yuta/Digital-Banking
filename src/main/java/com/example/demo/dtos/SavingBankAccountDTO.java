package com.example.demo.dtos;


import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.Customer;
import com.example.demo.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Data @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class SavingBankAccountDTO extends BankAccountDTO {



    private String id ;
    private double balance  ;
    private Date createdAt ;

    private AccountStatus status ;

    private CustomerDTO customerDTO ;
    private double interestRate ;
}
