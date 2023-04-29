package com.example.restaurant.repositories;

import com.example.restaurant.models.MenuOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuOrderRepository extends JpaRepository<MenuOrder, Integer> {

}
