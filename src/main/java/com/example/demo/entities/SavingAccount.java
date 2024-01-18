package com.example.demo.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SAV")
@Data @NoArgsConstructor @AllArgsConstructor
public class SavingAccount extends BankAccount {

    private double interestRate ;
}
