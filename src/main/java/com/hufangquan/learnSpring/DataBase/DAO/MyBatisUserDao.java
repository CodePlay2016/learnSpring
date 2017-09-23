package com.hufangquan.learnSpring.DataBase.DAO;

import com.hufangquan.learnSpring.DataBase.userbean.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("MyBatisUserDao")
public interface MyBatisUserDao {

    @Update("UPDATE UserBalance SET balance = 10000")
    public void reset();

    @Select("SELECT * FROM UserBalance")
    public List<User> getUserList();

    @Update("UPDATE UserBalance SET balance = balance + #{balance} WHERE userId = #{userId}")
    public void updateData(@Param("userId") int userId, @Param("balance") double balance);
}
