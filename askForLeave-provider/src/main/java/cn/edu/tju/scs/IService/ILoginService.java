package cn.edu.tju.scs.IService;

import cn.edu.tju.scs.dto.ErrorReporter;
import cn.edu.tju.scs.model.User;

import javax.servlet.http.HttpSession;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 上午12:15 17/12/3.
 */
public interface ILoginService {


    boolean isLogin(HttpSession httpSession);


    boolean exists(String username);

    User findOne(String username);

    User save(User user);
}
