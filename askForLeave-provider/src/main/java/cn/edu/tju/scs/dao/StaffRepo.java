package cn.edu.tju.scs.dao;

import cn.edu.tju.scs.model.Staff;
import org.springframework.data.repository.CrudRepository;


public interface StaffRepo extends CrudRepository<Staff, String> {
}
