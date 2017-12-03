package cn.edu.tju.scs.service;

import cn.edu.tju.scs.IService.ILoginService;
import cn.edu.tju.scs.dao.UserRepo;
import cn.edu.tju.scs.dto.ErrorReporter;
import cn.edu.tju.scs.dto.ResponseNameData;
import cn.edu.tju.scs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class LoginService implements ILoginService {

    @Autowired
    protected UserRepo userRepo;




    public boolean isLogin(HttpSession httpSession){
        if (httpSession.getAttribute("user")!=null){
            return true;
        }else {
            return false;
        }
    }



    public boolean exists(String username){
        return userRepo.exists(username);
    }

    public User findOne(String username){
        return userRepo.findOne(username);
    }

    public User save(User user){
        return userRepo.save(user);
    }


}
