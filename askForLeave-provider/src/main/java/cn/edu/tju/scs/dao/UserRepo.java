package cn.edu.tju.scs.dao;

import cn.edu.tju.scs.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<User, String>{
}
