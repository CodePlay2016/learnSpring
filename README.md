# Spring-数据库操作作业
## 题目
> 根据本单介绍的Spring JDBC，事务管理，MyBatis等内容，分别使用Spring JDBC及MyBatis提供一个转帐服务（保证事务），提供一个transferMoney接口：
transferMoney(Long srcUserId, Long targetUserId, double count)；
// srcUserId及targetUserId为转帐用户标识

## 解答
> 本题将分为Application层，service层，DAO以及userBean层来进行解答，对于SpringJDBC与整合MyBatis的区别主要体现在DAO上。

> 项目的源码在[这里](https://github.com/CodePlay2016/learnSpring.git)


1. application-context.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:p="http://www.springframework.org/schema/p" xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop-2.0.xsd
        http://mybatis.org/schema/mybatis-spring
        http://mybatis.org/schema/mybatis-spring.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd ">

    <!-- 数据库连接配置文件导入 -->
    <context:property-placeholder location="db.properties" />
    <!-- 使用注解声明事务 -->
    <tx:annotation-driven transaction-manager="txManager" />
    <!-- MyBatis的自动扫描 -->
    <mybatis:scan base-package="com.hufangquan.learnSpring.DataBase" />

    <!-- 数据源配置 -->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
          destroy-method="close">
        <property name="driverClassName" value="${jdbc.driverClassName}" />
        <property name="url" value="${jdbc.url}" />
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
    </bean>
    <!-- 事务管理器 -->
    <bean id="txManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <!-- 使用AOP-XML声明事务 -->
    <tx:advice id="txAdvice" transaction-manager="txManager"> <tx:attributes>
        <tx:method name="transfer*" /> </tx:attributes> </tx:advice> <aop:config>
        <aop:pointcut id="daoOperation" expression="execution(* com.hufangquan.learnSpring.DataBase.*.*(..))"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="daoOperation" /> </aop:config>

    <!-- 配置myBatis的sqlSessionFactory -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!-- 引入AOP -->
    <aop:aspectj-autoproxy />
    <context:component-scan base-package="com.hufangquan.learnSpring.DataBase"/>
```
2. UserDao实现，分为Jdbc和Mybatis实现
	1. JdbcUserDao实现UserDao接口
	```java
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
	```
	2. MyBatis实现
	```java
    @Component("MyBatisUserDao")
public interface MyBatisUserDao {

    @Update("UPDATE UserBalance SET balance = 10000")
    public void reset();

    @Select("SELECT * FROM UserBalance")
    public List<User> getUserList();

    @Update("UPDATE UserBalance SET balance = balance + #{balance} WHERE userId = #{userId}")
    public void updateData(@Param("userId") int userId, @Param("balance") double balance);
}
```
3. UserService
```java
@Component
public class UserService {
    // jdbc实现
//    @Resource(name = "JdbcUserDao")
    // MyBatis实现
    @Resource(name = "MyBatisUserDao")
    private UserDao userDao;

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
```
4. Application
```java
    public class Application {
        public static void main(String[] args) throws Exception {
            ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

            UserService service = context.getBean(UserService.class);
            service.reset();
            service.printUserList();
            System.out.println("----------after-----------");
            try {
                service.transferMoney(1,2,200.0);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
            service.printUserList();
            ((ConfigurableApplicationContext)context).close();
        }

    }
```
5. 结果
	1. jdbc-未产生异常
	![](/home/codeplay2017/研究生/学习/网易/web入门/Spring框架/作业/jdbc转账/SpringJDBC结果-未发生异常.png)
    2. jdbc-产生异常
	![](/home/codeplay2017/研究生/学习/网易/web入门/Spring框架/作业/jdbc转账/SpringJDBC结果-发生异常.png)
    3. MyBatis-产生异常
    ![](/home/codeplay2017/研究生/学习/网易/web入门/Spring框架/作业/jdbc转账/SpringMyBatis结果-发生异常.png)
