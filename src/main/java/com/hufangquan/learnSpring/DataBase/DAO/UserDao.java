package com.hufangquan.learnSpring.DataBase.DAO;

import com.hufangquan.learnSpring.DataBase.userbean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public void reset();

    public List<User> getUserList();

    public void updateData(int userId, double amount);
}
