package com.example.demo.entities;


import com.example.demo.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.LifecycleState;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE" ,length =4 , discriminatorType = DiscriminatorType.STRING)
@Data @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class BankAccount {


    @Id
    private String id ;
    private double balance  ;
    private Date createdAt ;
    @Enumerated(EnumType.STRING)
    private AccountStatus status ;
    @ManyToOne
    private Customer customer ;
    @OneToMany(mappedBy = "bankAccount" ,fetch=FetchType.LAZY)
    private List<AccountOperation> accountOperations ;
}
