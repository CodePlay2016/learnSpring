package com.hufangquan.learnSpring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args){
        //加载Spring相关配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");

        //获取UserService实例
        UserServices userService = applicationContext.getBean(UserServices.class);
        //调用方法
        try {
            userService.add("ZhangSan");
        } catch (Exception e) {

        }
        //获取ProductService实例
        ProductService productService =
                applicationContext.getBean(ProductService.class);
        //调用方法
        productService.del("Pen");

        ((ConfigurableApplicationContext)applicationContext).close();
    }
}
