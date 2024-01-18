package com.example.demo.web;


import com.example.demo.dtos.CustomerDTO;
import com.example.demo.entities.Customer;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@AllArgsConstructor
@Slf4j
public class CustomerRestController {

        @Autowired
    private BankAccountService bankAccountService ;

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomer();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);

    }
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO request)  {

        return  bankAccountService.saveCustomer(request);


    }

    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable(name = "id") Long id ,@RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(id);
        return bankAccountService.updateCustomer(customerDTO);

    }
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long id) {
        bankAccountService.deleteCustomer(id);

    }

}
