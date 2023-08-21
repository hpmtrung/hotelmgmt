package vn.lotusviet.hotelmgmt.repository.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.lotusviet.hotelmgmt.model.entity.person.Department;

@Repository
public interface DepartmentRepository
    extends JpaRepository<Department, Integer>, DepartmentCustomRepository {}