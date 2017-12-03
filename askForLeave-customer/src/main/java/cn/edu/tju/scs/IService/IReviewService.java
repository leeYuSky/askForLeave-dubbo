package cn.edu.tju.scs.IService;


import cn.edu.tju.scs.dto.ErrorReporter;
import cn.edu.tju.scs.model.User;

import javax.servlet.http.HttpSession;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 上午12:41 17/12/3.
 */
public interface IReviewService {

    ErrorReporter todoList(User curUser, int page, int pageSize);

    ErrorReporter doneList(User curUser,int page, int pageSize);

    ErrorReporter action(User curUser,int id, int status, String reviewReason);
}
