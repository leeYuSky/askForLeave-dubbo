package cn.edu.tju.scs.IService;

import cn.edu.tju.scs.dto.ErrorReporter;
import cn.edu.tju.scs.model.User;

import javax.servlet.http.HttpSession;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 上午12:18 17/12/3.
 */
public interface IApplyService {

    ErrorReporter add(User curUser, String username, int startTime, int endTime, int type, String reason, int submitStatus);

    ErrorReporter modify(User curUser,String username, int startTime, int endTime, int type, String reason, int submitStatus, int id);

    ErrorReporter info(User curUser);

    ErrorReporter delete(User curUser,int id);

    ErrorReporter draftList(User curUser,int page, int pageSize);

    ErrorReporter publishList(User curUser,int page, int pageSize);

    ErrorReporter overtimeDraftList(User curUser,int page, int pageSize);

    ErrorReporter overtimePublishList(User curUser,int page, int pageSize);


}
