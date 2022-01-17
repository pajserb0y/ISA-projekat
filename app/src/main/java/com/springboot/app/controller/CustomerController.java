package com.springboot.app.controller;

import com.springboot.app.model.Customer;
import com.springboot.app.model.dto.CustomerDTO;
import com.springboot.app.model.dto.CustomerLoyalty;
import com.springboot.app.model.dto.DeleteDTO;
import com.springboot.app.model.dto.EntityIdAndCustomerUsername;
import com.springboot.app.service.CustomerService;
import com.springboot.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")  //sluzi za povezivanje sa frontom
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final EmailService emailService;

    @Autowired
    public CustomerController(CustomerService customerService, EmailService emailService) {
        this.customerService = customerService;
        this.emailService = emailService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createCustomer(@RequestBody @Valid CustomerDTO customerDto, BindingResult result) throws Exception {
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Customer newCustomer = customerService.saveCustomer(new Customer(customerDto));
        emailService.sendActivationMailAsync(newCustomer);

        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @GetMapping(path = "/activate")
    public ResponseEntity<?> activateCustomerAccount(WebRequest request, @RequestParam("hashCode") String hashCode) {
        Customer verificationCustomer = customerService.findByHashCode(hashCode);
        if (verificationCustomer == null || verificationCustomer.isActivated()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        verificationCustomer.setActivated(true);
        customerService.saveCustomer(verificationCustomer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost:4200");
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(path = "/{username}")
    public CustomerLoyalty getCustomerByUsername(@PathVariable String username) {
        Customer customer = customerService.findByUsername(username);
        return new CustomerLoyalty(customer);
    }

//    @PreAuthorize("hasRole('CUSTOMER')")
//    @PutMapping(path = "/edit")
//    public CustomerDTO editCustomer(@RequestBody CustomerDTO customerDto) {
//        Customer editedCustomer = customerService.changeCustomer(customerDto);
//        return new CustomerDTO(editedCustomer);
//    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/edit")
    public CustomerDTO editCustomer(@RequestBody @Valid CustomerDTO customerDto, BindingResult result) throws Exception {
        if(result.hasErrors())
            return null;
        Customer editedCustomer = customerService.changeCustomer(customerDto);
        return new CustomerDTO(editedCustomer);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/delete")
    public ResponseEntity<?> proccessCustomerDeleting(@RequestBody DeleteDTO dto) {
        if(!customerService.findById(dto.id).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        customerService.setWantedToDelete(dto.id);
        emailService.sendNotificationForDeletingToAdmin(dto.note, dto.id);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/subscribeWeekendHouse")
    public ResponseEntity<?> subscribeWeekendHouse(@RequestBody EntityIdAndCustomerUsername dto) {
        customerService.subscribeWeekendHouse(dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/subscribeBoat")
    public ResponseEntity<?> subscribeBoat(@RequestBody EntityIdAndCustomerUsername dto) {
        customerService.subscribeBoat(dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/subscribeFishingLesson")
    public ResponseEntity<?> subscribeFishingLesson(@RequestBody EntityIdAndCustomerUsername dto) {
        customerService.subscribeFishingLesson(dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/unsubscribeWeekendHouse")
    public ResponseEntity<?> unsubscribeWeekendHouse(@RequestBody EntityIdAndCustomerUsername dto) {
        customerService.unsubscribeWeekendHouse(dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/unsubscribeBoat")
    public ResponseEntity<?> unsubscribeBoat(@RequestBody EntityIdAndCustomerUsername dto) {
        customerService.unsubscribeBoat(dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/unsubscribeFishingLesson")
    public ResponseEntity<?> unsubscribeFishingLesson(@RequestBody EntityIdAndCustomerUsername dto) {
        customerService.unsubscribeFishingLesson(dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
