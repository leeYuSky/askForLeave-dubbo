package cn.edu.tju.scs.controller;

import cn.edu.tju.scs.IService.ILoginService;

import cn.edu.tju.scs.dto.ErrorReporter;
import cn.edu.tju.scs.dto.ResponseNameData;
import cn.edu.tju.scs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 上午1:10 17/12/3.
 */
@RestController
public class AuthController {

    @Autowired
    protected ILoginService iLoginService;

    @Autowired
    HttpSession httpSession;

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ErrorReporter index(String username, String password){

        String msg;
        if(isLogin(httpSession)) {
            msg = "hello " + ((User)httpSession.getAttribute("user")).getId();
        } else {
            msg = "not log in";
        }

        return new ErrorReporter(100, "index page: " + msg);
    }

    @RequestMapping(value = "/leave/auth/login",method = RequestMethod.POST)
    public ErrorReporter doLogin(String username, String password){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (iLoginService.exists(username)) {
            User user = iLoginService.findOne(username);
            if (passwordEncoder.matches(password, user.getPassword())) {
                httpSession.setAttribute("user", user);
                ResponseNameData responseNameData = new ResponseNameData(username);
                return new ErrorReporter(0, "success",responseNameData);
            }else {
                return new ErrorReporter(1, "password error");
            }
        } else {
            return new ErrorReporter(2, "no account");
        }

    }

    @RequestMapping(value = "/leave/auth/reg",method = RequestMethod.POST)
    public ErrorReporter doReg(String username, String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);
        if ( !iLoginService.exists(username) ) {
            User user = new User(username,password);
            iLoginService.save(user);
            return new ErrorReporter(0, "success");
        } else {
            return new ErrorReporter(3, "duplication error");
        }
    }

    @RequestMapping(value = "/leave/auth/logout",method = RequestMethod.POST)
    public ErrorReporter doLogout(String username){
        if (httpSession.getAttribute("user") == null){
            return  new ErrorReporter(4, "not login");
        }
        httpSession.setAttribute("user", null);
        return new ErrorReporter(0, "success");
    }

    public boolean isLogin(HttpSession httpSession){
        if (httpSession.getAttribute("user")!=null){
            return true;
        }else {
            return false;
        }
    }
}
