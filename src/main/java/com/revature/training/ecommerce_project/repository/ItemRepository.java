package com.revature.training.ecommerce_project.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.revature.training.ecommerce_project.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
