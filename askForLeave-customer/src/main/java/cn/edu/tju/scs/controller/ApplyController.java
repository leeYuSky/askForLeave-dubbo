package cn.edu.tju.scs.controller;

import cn.edu.tju.scs.IService.IApplyService;
import cn.edu.tju.scs.dto.ErrorReporter;
import cn.edu.tju.scs.dto.ResponseInfoData;
import cn.edu.tju.scs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 上午12:58 17/12/3.
 */

@RestController
public class ApplyController {

    @Autowired
    protected HttpSession httpSession;


    @Autowired
    protected IApplyService iApplyService;




    @RequestMapping("/leave/apply/add")
    public ErrorReporter add(String username, int startTime, int endTime, int type, String reason, int submitStatus) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        if (startTime > endTime) {
            return new ErrorReporter(11, "invalid start time and end time");
        }

        if (submitStatus != 1 && submitStatus != 2) {
            return new ErrorReporter(12, "invalid submit status");
        }

        if ( !Arrays.asList(1,2,3,4,5,6,7,10).contains(type)) {
            return new ErrorReporter(13, "unknown type");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        if ( !curUser.getId().equals(username)) {
            return new ErrorReporter(14, "should only apply leave for yourself");
        }

        return iApplyService.add(curUser,username,startTime,endTime,type,reason,submitStatus);
    }

    @RequestMapping("/leave/apply/modify")
    public ErrorReporter modify(String username, int startTime, int endTime, int type, String reason, int submitStatus, int id) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }
        if (startTime > endTime) {
            return new ErrorReporter(11, "invalid start time and end time");
        }

        if (submitStatus != 1 && submitStatus != 2) {
            return new ErrorReporter(12, "invalid submit status");
        }

        if ( !Arrays.asList(1,2,3,4,5,6,7,10).contains(type)) {
            return new ErrorReporter(13, "unknown type");
        }

        User curUser = ((User)httpSession.getAttribute("user"));

        return iApplyService.modify(curUser,username,startTime,endTime,type,reason,submitStatus,id);
    }

    @RequestMapping("/leave/apply/info")
    public ErrorReporter info(String username) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));

        return iApplyService.info(curUser);
    }

    @RequestMapping("/leave/apply/delete")
    public ErrorReporter info(int id) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));

        return iApplyService.delete(curUser,id);
    }

    @RequestMapping("/leave/apply/draftList")
    public ErrorReporter draftList(String username, int page, int pageSize) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));

        return iApplyService.draftList(curUser,page,pageSize);
    }

    @RequestMapping("/leave/apply/publishList")
    public ErrorReporter publishList(String username, int page, int pageSize) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));

        return iApplyService.publishList(curUser,page,pageSize);
    }

    @RequestMapping("/leave/apply/overtimeDraftList")
    public ErrorReporter overtimeDraftList(String username, int page, int pageSize) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        return iApplyService.overtimeDraftList(curUser,page,pageSize);
    }

    @RequestMapping("/leave/apply/overtimePublishList")
    public ErrorReporter overtimePublishList(String username, int page, int pageSize) {
        if (!isLogin(httpSession)) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));

        return iApplyService.overtimePublishList(curUser,page,pageSize);
    }

    public boolean isLogin(HttpSession httpSession){
        if (httpSession.getAttribute("user")!=null){
            return true;
        }else {
            return false;
        }
    }
}
