package com.hufangquan.learnSpring.DataBase.service;

import com.hufangquan.learnSpring.DataBase.DAO.MyBatisUserDao;
import com.hufangquan.learnSpring.DataBase.DAO.UserDao;
import com.hufangquan.learnSpring.DataBase.userbean.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MyBatisUserService {
    @Resource(name="MyBatisUserDao")
    private MyBatisUserDao userDao;

    public void reset() {
        userDao.reset();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void transferMoney(int srcUserId, int tgtUserId, double count) {
        userDao.updateData(srcUserId, -count);
        this.throwException();
        userDao.updateData(tgtUserId, count);
    }

    public List<User> getUserList() {
        return userDao.getUserList();
    }

    public void printUserList() {
        List<User> userList = userDao.getUserList();
        for (User user: userList) {
            System.out.println(user);
        }
    }

    public void throwException() {
        throw new RuntimeException("ERROR");
    }
}
