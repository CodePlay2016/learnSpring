package com.hufangquan.learnSpring.DataBase.applications;

import com.hufangquan.learnSpring.DataBase.DAO.MyBatisUserDao;
import com.hufangquan.learnSpring.DataBase.service.JdbcUserService;
import com.hufangquan.learnSpring.DataBase.service.MyBatisUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyBatisApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        MyBatisUserService service = context.getBean(MyBatisUserService.class);
        service.reset();
        service.printUserList();
        System.out.println("----------after-----------");
        try {
            service.transferMoney(1,2,200.0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        service.printUserList();

        ((ConfigurableApplicationContext)context).close();
    }

}
