package com.example.demo.dtos;

import com.example.demo.enums.OperationType;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class AccountHistoryDTO {

    private List<AccountOperationDTO> accountOperationDTOS;
    private String accountId ;
    private double balance ;
    private int currentPage;
    private int totalPages ;
    private int pageSize ;
}
