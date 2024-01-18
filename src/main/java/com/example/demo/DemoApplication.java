package com.example.demo;

import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.dtos.CurrentBankAccountDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.dtos.SavingBankAccountDTO;
import com.example.demo.entities.*;
import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.repositories.AccountOperationRepository;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.service.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Bean
	CommandLineRunner start(BankAccountService bankAccountService , BankAccountRepository bankAccountRepository) {
		return args -> {
			Stream.of("Hassan", "Mohamed" , "Imane").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name +"gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomer().forEach(cus-> {
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,cus.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*90000,5.5, cus.getId());
					List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountDTOList();
					for(BankAccountDTO bankAccount : bankAccounts) {
						for (int i =0 ; i<10 ; i++) {
							String accountId;
							if(bankAccount instanceof SavingBankAccountDTO){
								accountId=((SavingBankAccountDTO) bankAccount).getId();
							}
							else { accountId = ((CurrentBankAccountDTO)bankAccount).getId();}
							bankAccountService.credit(accountId, 10000+Math.random()*120000, "Credit");
							bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
						}};
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				} catch (BankAccountNotFoundException e) {
					e.printStackTrace();
				} catch (BalanceNotSufficientException e) {
					e.printStackTrace();
				}
			});



		};
	}
}


