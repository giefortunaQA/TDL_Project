package com.qa.main.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.main.persistence.domain.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long>{

	@Query(value="select * from item where td_list_id=?",nativeQuery=true)
	List<Item> findItemsInList(Long id);
}
