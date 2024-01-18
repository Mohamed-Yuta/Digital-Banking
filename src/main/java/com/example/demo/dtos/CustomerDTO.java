package com.example.demo.dtos;

import com.example.demo.entities.BankAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data @NoArgsConstructor @AllArgsConstructor
public class CustomerDTO {

    private Long id ;
    private String name ;
    private String email ;

}
