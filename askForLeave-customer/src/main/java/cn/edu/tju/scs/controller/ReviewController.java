package cn.edu.tju.scs.controller;

import cn.edu.tju.scs.IService.IReviewService;
import cn.edu.tju.scs.dto.ErrorReporter;
import cn.edu.tju.scs.dto.ResponseLeaveApplication;
import cn.edu.tju.scs.dto.ResponseListData;
import cn.edu.tju.scs.model.LeaveApplication;
import cn.edu.tju.scs.model.Staff;
import cn.edu.tju.scs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 上午1:17 17/12/3.
 */
@RestController
public class ReviewController {

    @Autowired
    protected HttpSession httpSession;

    @Autowired
    protected IReviewService iReviewService;

    @RequestMapping("/leave/review/todoList")
    public ErrorReporter todoList (String username, int page, int pageSize) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");

        return iReviewService.todoList(curUser,page,pageSize);
    }

    @RequestMapping("/leave/review/doneList")
    public ErrorReporter doneList(String username, int page, int pageSize) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");

        return iReviewService.doneList(curUser,page,pageSize);
    }

    @RequestMapping("/leave/review/action")
    public ErrorReporter action(int id, int status, String reviewReason) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");

        return iReviewService.action(curUser,id,status,reviewReason);
    }

    public boolean isLogin(HttpSession httpSession){
        if (httpSession.getAttribute("user")!=null){
            return true;
        }else {
            return false;
        }
    }

}
