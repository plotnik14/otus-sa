package com.alexp.controller;

import com.alexp.DeliveryTasksRepository;
import com.alexp.adapter.OrderManagementAdapter;
import com.alexp.adapter.UserManagementAdapter;
import com.alexp.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class DeliveryController {

    private final DeliveryTasksRepository deliveryTasksRepository;
    private final UserManagementAdapter userManagementAdapter;
    private final OrderManagementAdapter orderManagementAdapter;


    public DeliveryController(DeliveryTasksRepository deliveryTasksRepository,
                              UserManagementAdapter userManagementAdapter,
                              OrderManagementAdapter orderManagementAdapter) {
        this.deliveryTasksRepository = deliveryTasksRepository;
        this.userManagementAdapter = userManagementAdapter;
        this.orderManagementAdapter = orderManagementAdapter;
    }

    @GetMapping("deliveryOptions")
    public ResponseEntity<List<DeliveryOption>> getDeliveryOptions(){
        List<DeliveryOption> deliveryOptions = new ArrayList<>();
        deliveryOptions.add(new DeliveryOption("Courier", "Delivery by courier"));
        deliveryOptions.add(new DeliveryOption("Shop", "Delivery to the specific shop"));
        return ResponseEntity.ok(deliveryOptions);
    }

    @GetMapping("deliveryTasks")
    public ResponseEntity<List<DeliveryTask>> getDeliveryTaskForCourier(@RequestParam("courierId") UUID courierId){
        List<DeliveryTask> deliveryTasks = deliveryTasksRepository.findAllByCourierId(courierId);
        return ResponseEntity.ok(deliveryTasks);
    }

    @GetMapping("deliveryTasks/{taskId}")
    public ResponseEntity<DeliveryTaskDetails> getDeliveryTaskDetails(@PathVariable("taskId") UUID taskId){
        DeliveryTask deliveryTask = deliveryTasksRepository.findById(taskId).orElse(null);

        if (deliveryTask == null) {
            return ResponseEntity.notFound().build();
        }

        Order order = orderManagementAdapter.getOrderById(deliveryTask.getOrderId());
        User customer = userManagementAdapter.getUserById(order.getCustomerId());

        DeliveryTaskDetails deliveryTaskDetails = new DeliveryTaskDetails(deliveryTask);
        deliveryTaskDetails.setOrder(order);
        deliveryTaskDetails.setCustomer(customer);

        return ResponseEntity.ok(deliveryTaskDetails);
    }

    @PostMapping("deliveryTasks/{taskId}")
    public ResponseEntity<DeliveryTask> changeDeliveryTaskStatus(@PathVariable("taskId") UUID taskId,
                                                                 @RequestBody ChangeStatusRequest changeStatusRequest){
        DeliveryTask deliveryTask = deliveryTasksRepository.findById(taskId).orElse(null);

        if (deliveryTask == null) {
            return ResponseEntity.notFound().build();
        }

        //ToDo validation

        deliveryTask.setStatus(changeStatusRequest.getStatus());
        deliveryTask = deliveryTasksRepository.save(deliveryTask);

        return ResponseEntity.ok(deliveryTask);
    }
}
