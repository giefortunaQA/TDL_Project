package com.qa.main.persistence.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
//
//import com.qa.main.persistence.domain.Item;
import com.qa.main.persistence.domain.ToDoList;

@Repository
public interface ToDoListRepo extends JpaRepository<ToDoList, Long>{

	@Query(value="select * from to_do_list order by id desc limit 1",nativeQuery=true)
	ToDoList findLatestId();

}
