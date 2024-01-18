package com.example.demo.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CUR")
@Data @NoArgsConstructor @AllArgsConstructor

public class CurrentAccount extends BankAccount {
    private double overDraft ;
}
