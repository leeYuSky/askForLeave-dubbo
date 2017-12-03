package cn.edu.tju.scs.service;

import cn.edu.tju.scs.IService.IApplyService;
import cn.edu.tju.scs.dao.LeaveAppRepo;
import cn.edu.tju.scs.dao.StaffRepo;
import cn.edu.tju.scs.dto.ErrorReporter;
import cn.edu.tju.scs.dto.ResponseInfoData;
import cn.edu.tju.scs.dto.ResponseLeaveApplication;
import cn.edu.tju.scs.dto.ResponseListData;
import cn.edu.tju.scs.model.LeaveApplication;
import cn.edu.tju.scs.model.Staff;
import cn.edu.tju.scs.model.User;
import com.google.gson.Gson;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 上午12:21 17/12/3.
 */
@Service
public class ApplyService implements IApplyService {


    @Autowired
    protected LoginService loginService;

    @Autowired
    protected LeaveAppRepo leaveAppRepo;

    @Autowired
    protected StaffRepo staffRepo;

    public ErrorReporter add(User curUser,String username, int startTime, int endTime, int type, String reason, int submitStatus){
        Staff curStaff = staffRepo.findOne( curUser.getId() );

        if (submitStatus == 2) {
            Gson gson = new Gson();
            int[] leaveDetail = gson.fromJson(curStaff.getLeaveDetail(), int[].class);
            LocalDate initialDay = new LocalDate(2016, 1, 1);
            LocalDate startDay = new LocalDate(startTime * 1000L);
            LocalDate endDay = new LocalDate(endTime * 1000L);
            int startDayIndex = Days.daysBetween(initialDay, startDay).getDays();
            int endDayIndex = Days.daysBetween(initialDay, endDay).getDays();

            for (int i = startDayIndex; i <= endDayIndex; i++) {
                if (leaveDetail[i] != 0 && leaveDetail[i] != 9 && leaveDetail[i] != 8) {
                    return new ErrorReporter(15, "invalid period for leave application, please check your start time and end time");
                }
            }

            if (type == 1) {
                int annualLeft = curStaff.getAnnualLeft();
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        annualLeft--;
                    }
                }
                if (annualLeft < 0) {
                    return new ErrorReporter(16, "your left annual leave is not enough");
                }
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        leaveDetail[i] += 100;
                    }
                }
                curStaff.setAnnualLeft(annualLeft);
            } else if (Arrays.asList(2, 3, 4, 5, 6, 7).contains(type)) {
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        leaveDetail[i] += 100;
                    }
                }
            } else {
                if (startDayIndex != endDayIndex || (leaveDetail[startDayIndex] != 8 && leaveDetail[startDayIndex] != 9))
                    return new ErrorReporter(17, "can not apply overtime");
                else
                    leaveDetail[startDayIndex] += 100;
            }

            curStaff.setLeaveDetail(gson.toJson(leaveDetail));
            staffRepo.save(curStaff);
        }
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication la = leaveAppRepo.save(new LeaveApplication(username , curStaff.getName() , startTime , endTime , curTime , reason, type, submitStatus, ""+ curStaff.getDepartment(), curStaff.getManagerId(), curStaff.getManagerName(), 0 , ""));
        return new ErrorReporter(0, "success");
    }

    public ErrorReporter modify(User curUser,String username, int startTime, int endTime, int type, String reason, int submitStatus, int id){
        Staff curStaff = staffRepo.findOne( curUser.getId() );
        if ( !curUser.getId().equals(username)) {
            return new ErrorReporter(18, "should only modify leave applications for yourself");
        }

        LeaveApplication la;
        if (leaveAppRepo.exists(id)){
            la = leaveAppRepo.findOne(id);
        } else {
            return new ErrorReporter(19, "application not exist");
        }

        if (la.getStatus() != 1 || !la.getApplicantId().equals(curStaff.getId()) ) {
            return new ErrorReporter(20, "can not modify");
        }

        if (submitStatus == 2) {    // then change the leave details of the staff
            Gson gson = new Gson();
            int[] leaveDetail =  gson.fromJson(curStaff.getLeaveDetail(), int[].class);

            LocalDate initialDay = new LocalDate(2016,1,1);
            LocalDate startDay = new LocalDate(startTime*1000L);
            LocalDate endDay = new LocalDate(endTime*1000L);
            int startDayIndex = Days.daysBetween(initialDay, startDay).getDays();
            int endDayIndex = Days.daysBetween(initialDay, endDay).getDays();

            for (int i = startDayIndex; i <= endDayIndex; i++) {
                if ( leaveDetail[i] != 0 && leaveDetail[i] != 8 && leaveDetail[i] != 9) {
                    return new ErrorReporter(15, "invalid period for leave application, please check your start time and end time");
                }
            }

            if (type == 1) {
                int annualLeft = curStaff.getAnnualLeft();
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        annualLeft --;
                    }
                }
                if (annualLeft < 0){
                    return new ErrorReporter(16, "your left annual leave is not enough");
                }
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        leaveDetail[i] += 100;
                    }
                }
                curStaff.setAnnualLeft(annualLeft);
            } else if (Arrays.asList(2,3,4,5,6,7).contains(type)){
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        leaveDetail[i] += 100;
                    }
                }
            } else {
                if(startDayIndex != endDayIndex || (leaveDetail[startDayIndex] != 8 && leaveDetail[startDayIndex] != 9))
                    return new ErrorReporter(17, "can not apply overtime");
                else
                    leaveDetail[startDayIndex] += 100;
            }

            curStaff.setLeaveDetail(gson.toJson(leaveDetail));
            staffRepo.save(curStaff);
        }

        la.setStartTime(startTime);
        la.setEndTime(endTime);
        la.setType(type);
        la.setApplyReason(reason);
        la.setStatus(submitStatus);

        int curTime = (int) (System.currentTimeMillis() / 1000L);
        la.setApplyTime(curTime);

        la.setApplicantName(curStaff.getName());
        la.setManagerId(curStaff.getManagerId());
        la.setManagerName(curStaff.getManagerName());
        la = leaveAppRepo.save(la);

        return new ErrorReporter(0, "success");
    }

    public ErrorReporter info(User curUser){
        Staff curStaff = staffRepo.findOne( curUser.getId() );
        ResponseInfoData rd = new ResponseInfoData(curStaff.getId(), curStaff.getName(), curStaff.getManagerId(), curStaff.getManagerName(), curStaff.getDepartment(), curStaff.getAnnualTotal(), curStaff.getAnnualLeft());

        return new ErrorReporter(0, "success", rd);
    }

    public ErrorReporter delete(User curUser,int id){
        Staff curStaff = staffRepo.findOne( curUser.getId() );

        LeaveApplication la;
        if (leaveAppRepo.exists(id)){
            la = leaveAppRepo.findOne(id);
        } else {
            return new ErrorReporter(19, "application not exist");
        }

        if (la.getStatus() != 1 || !la.getApplicantId().equals(curStaff.getId()) ) {
            return new ErrorReporter(21, "can not delete");
        }

        leaveAppRepo.delete(la);

        return new ErrorReporter(0, "success");
    }

    public ErrorReporter draftList(User curUser,int page, int pageSize){

        Staff curStaff = staffRepo.findOne( curUser.getId() );

        int total = leaveAppRepo.countByApplicantIdAndStatusInAndTypeIn(curStaff.getId(), Collections.singletonList(1), Arrays.asList(1,2,3,4,5,6,7));

        Pageable pageable = new PageRequest(page - 1, pageSize);

        List<LeaveApplication> las = leaveAppRepo.findByApplicantIdAndStatusInAndTypeInOrderByApplyTimeDesc(curStaff.getId(), Collections.singletonList(1), Arrays.asList(1,2,3,4,5,6,7), pageable);


        // parse to format for transfer, that is caused by not strictly follow the agreement with front side when develop.
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : las){
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseListData responseData = new ResponseListData(page, pageSize, total, curStaff.getId(), list);

        return new ErrorReporter(0, "success", responseData);
    }

    public ErrorReporter publishList(User curUser,int page, int pageSize){
        Staff curStaff = staffRepo.findOne( curUser.getId() );

        int total = leaveAppRepo.countByApplicantIdAndStatusInAndTypeIn(curStaff.getId(), Arrays.asList(2,3,4), Arrays.asList(1,2,3,4,5,6,7));

        Pageable pageable = new PageRequest(page - 1, pageSize);

        List<LeaveApplication> las = leaveAppRepo.findByApplicantIdAndStatusInAndTypeInOrderByApplyTimeDesc(curStaff.getId(), Arrays.asList(2,3,4), Arrays.asList(1,2,3,4,5,6,7), pageable);


        // parse to format for transfer, that is caused by not strictly follow the agreement with front side when develop.
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : las){
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseListData responseData = new ResponseListData(page, pageSize, total, curStaff.getId(), list);

        return new ErrorReporter(0, "success", responseData);
    }

    public ErrorReporter overtimeDraftList(User curUser,int page, int pageSize){
        Staff curStaff = staffRepo.findOne( curUser.getId() );

        int total = leaveAppRepo.countByApplicantIdAndStatusInAndTypeIn(curStaff.getId(), Collections.singletonList(1), Collections.singletonList(10));

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByApplicantIdAndStatusInAndTypeInOrderByApplyTimeDesc(curStaff.getId(), Collections.singletonList(1), Collections.singletonList(10), pageable);

        // parse to format for transfer, that is caused by not strictly follow the agreement with front side when develop.
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : las){
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseListData responseData = new ResponseListData(page, pageSize, total, curStaff.getId(), list);

        return new ErrorReporter(0, "success", responseData);
    }

    public ErrorReporter overtimePublishList(User curUser,int page, int pageSize){
        Staff curStaff = staffRepo.findOne( curUser.getId() );

        int total = leaveAppRepo.countByApplicantIdAndStatusInAndTypeIn(curStaff.getId(), Arrays.asList(2,3,4), Collections.singletonList(10));

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByApplicantIdAndStatusInAndTypeInOrderByApplyTimeDesc(curStaff.getId(), Arrays.asList(2,3,4), Collections.singletonList(10), pageable);

        // parse to format for transfer, that is caused by not strictly follow the agreement with front side when develop.
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : las){
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseListData responseData = new ResponseListData(page, pageSize, total, curStaff.getId(), list);

        return new ErrorReporter(0, "success", responseData);
    }


}
