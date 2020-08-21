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
        initDB();
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

    private void initDB() {
        List<DeliveryTask> deliveryTasks = new ArrayList<>();

        DeliveryTask deliveryTask = new DeliveryTask();
        deliveryTask.setCourierId(UUID.fromString("fe2310aa-e141-11ea-87d0-0242ac130003"));
        deliveryTask.setOrderId(UUID.fromString("0f79f206-e142-11ea-87d0-0242ac130003"));
        deliveryTask.setStatus("Open");
        deliveryTask.setComment("Some text");
        deliveryTasks.add(deliveryTask);

        deliveryTask = new DeliveryTask();
        deliveryTask.setCourierId(UUID.fromString("fe2310aa-e141-11ea-87d0-0242ac130003"));
        deliveryTask.setOrderId(UUID.fromString("7b7685a2-4a39-4e0b-a86f-159b67e03287"));
        deliveryTask.setStatus("Open");
        deliveryTask.setComment("Some text");
        deliveryTasks.add(deliveryTask);

        deliveryTask = new DeliveryTask();
        deliveryTask.setCourierId(UUID.fromString("fe2310aa-e141-11ea-87d0-0242ac130003"));
        deliveryTask.setOrderId(UUID.fromString("599a13ca-e142-11ea-87d0-0242ac130003"));
        deliveryTask.setStatus("Open");
        deliveryTask.setComment("Some text");
        deliveryTasks.add(deliveryTask);

        deliveryTask = new DeliveryTask();
        deliveryTask.setCourierId(UUID.fromString("6070ff9c-e142-11ea-87d0-0242ac130003"));
        deliveryTask.setOrderId(UUID.fromString("646d54b0-e142-11ea-87d0-0242ac130003"));
        deliveryTask.setStatus("Open");
        deliveryTask.setComment("Some text");
        deliveryTasks.add(deliveryTask);

        deliveryTasksRepository.saveAll(deliveryTasks);
    }

}
