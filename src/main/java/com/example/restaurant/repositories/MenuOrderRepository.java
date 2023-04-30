package com.example.restaurant.repositories;

import com.example.restaurant.models.MenuOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuOrderRepository extends JpaRepository<MenuOrder, Integer> {
    List<MenuOrder> findByCustomer_Id(int id);
}
