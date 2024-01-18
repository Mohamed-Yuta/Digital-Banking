package com.example.demo.entities;

import com.example.demo.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Date operationDate ;
    private double amount ;
    @Enumerated(EnumType.STRING)
    private OperationType operationType ;
    @ManyToOne
    private BankAccount bankAccount ;
    private String description ;
}
