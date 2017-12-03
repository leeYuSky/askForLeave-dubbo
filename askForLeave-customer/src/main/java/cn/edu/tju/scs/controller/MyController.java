package cn.edu.tju.scs.controller;

import cn.edu.tju.scs.demo.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 下午8:54 17/11/18.
 */
@Controller
@RequestMapping(value="test")
public class MyController {

//    @Resource(name="demoService")
    @Autowired
    private DemoService demoService;

    @RequestMapping(value = "test",method = RequestMethod.GET)
    @ResponseBody
    public String testSay(@RequestParam(value = "name",defaultValue = "") String name){
        System.out.println("--------------------");
        StringBuffer sb = new StringBuffer();
        sb.append("Dubbo: ").append(demoService.sayHello(name));
        return sb.toString();
    }

}
