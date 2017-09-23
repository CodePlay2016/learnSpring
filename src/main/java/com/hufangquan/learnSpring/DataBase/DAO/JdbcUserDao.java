package com.hufangquan.learnSpring.DataBase.DAO;

import com.hufangquan.learnSpring.DataBase.userbean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("JdbcUserDao")
public class JdbcUserDao implements UserDao{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void reset(){
        this.jdbcTemplate.update("UPDATE UserBalance SET balance = 10000");
    }

    public void insertData() {
        this.jdbcTemplate.update("INSERT INTO UserBalance VALUES (1, 10000)");
        this.jdbcTemplate.update("INSERT INTO UserBalance VALUES (2, 10000)");
    }

    public List<User> getUserList() {
        return this.jdbcTemplate.query("select * from UserBalance", new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setUserId(rs.getLong("userId"));
                user.setBalance(rs.getDouble("balance"));
                return user;
            }
        });
    }



    public void updateData(int UserId, double amount){
        this.jdbcTemplate.update("UPDATE UserBalance SET balance = balance + ? WHERE userId = ?", amount, Long.valueOf(UserId));
    }


}
