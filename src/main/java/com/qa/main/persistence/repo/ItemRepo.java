package com.qa.main.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.main.persistence.domain.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long>{

}
