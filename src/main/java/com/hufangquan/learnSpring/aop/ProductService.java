package com.hufangquan.learnSpring.aop;

import org.springframework.stereotype.Component;

@Component
public class ProductService {
    public void add(String username) throws Exception {
        System.out.println("添加产品:" + username);
        throw new Exception("测试");
    }
    public void edit(String username) {
        System.out.println("添加产品:" + username);
    }
    public void del(String username) {
        System.out.println("删除产品:" + username);
    }
    public void find(String username) {
        System.out.println("查询产品:" + username);
    }
}
