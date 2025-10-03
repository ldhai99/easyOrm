package io.github.ldhai99.easyOrm.test.datasource;


import io.github.ldhai99.easyOrm.annotation.Table;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import io.github.ldhai99.easyOrm.SQL;

import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataSourceManagerSpringIntegrationTest {

    private static DataSource dataSource;
    private static NamedParameterJdbcTemplate npjt;

    @BeforeAll
    static void setupDataSource() throws Exception {
        // 1. 创建 H2 内存数据库数据源
        com.alibaba.druid.pool.DruidDataSource ds = new com.alibaba.druid.pool.DruidDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        ds.setUsername("sa");
        ds.setPassword("");

        dataSource = ds;

        // 2. 初始化 NamedParameterJdbcTemplate
        npjt = new NamedParameterJdbcTemplate(dataSource);

        // 3. 设置全局数据源（模拟 Spring 自动配置）
        DataSourceManager.setDataSourceForSpring(dataSource);

        // 4. 初始化数据库表
        initializeSchema();
    }

    private static void initializeSchema() throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS users ("+
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT,"+
                    "name VARCHAR(100) NOT NULL,"+
                    "email VARCHAR(100) UNIQUE)"

                );


        }
    }
    @BeforeEach
    void setUpData() {
        SQL sql = new SQL();
        // 清空表
        sql.delete("users").execute();

        // 重新插入初始数据（保证 id=1, id=2）
        sql.insert("users").set("id", 1L).set("name", "Alice").set("email", "alice@example.com").execute();
        sql.insert("users").set("id", 2L).set("name", "Bob").set("email", "bob@example.com").execute();
    }
    @AfterAll
    static void tearDown() {
        if (dataSource instanceof com.alibaba.druid.pool.DruidDataSource) {
            ((com.alibaba.druid.pool.DruidDataSource) dataSource).close();
        }
    }

    // ==================== 测试模型 ====================
    @Table("users")
    public static class User {
        private Long id;
        private String name;
        private String email;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

    // ==================== 实际测试 ====================

    @Test
    @DisplayName("测试：查询所有用户")
    void testSelectAllUsers() {
        SQL sql = new SQL();
        List<User> users = sql.select("users").list(User.class);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> "Alice".equals(u.getName())));
        assertTrue(users.stream().anyMatch(u -> "Bob".equals(u.getName())));
    }

    @Test
    @DisplayName("测试：根据 ID 查询用户")
    void testSelectUserById() {
        SQL sql = new SQL();
        User user = sql.select("users").eq("id", 1L).one(User.class);

        assertNotNull(user);
        assertEquals("Alice", user.getName());
        assertEquals("alice@example.com", user.getEmail());
    }

    @Test
    @DisplayName("测试：插入新用户")
    void testInsertUser() {
        SQL sql = new SQL();
        int rows = sql.insert("users")
                .set("name", "Charlie")
                .set("email", "charlie@example.com")
                .execute();

        assertEquals(1, rows);

        // 查询验证
        User user = sql.select("users").eq("email", "charlie@example.com").one(User.class);
        assertNotNull(user);
        assertEquals("Charlie", user.getName());
    }

    @Test
    @DisplayName("测试：更新用户")
    void testUpdateUser() {
        SQL sql = new SQL();
        int rows = sql.update("users")
                .set("name", "Alice Smith")
                .eq("id", 1L)
                .execute();

        assertEquals(1, rows);

        User user = sql.select("users").eq("id", 1L).one(User.class);
        assertNotNull(user);
        assertEquals("Alice Smith", user.getName());
    }

    @Test
    @DisplayName("测试：删除用户")
    void testDeleteUser() {
        SQL sql = new SQL();
        int rows = sql.delete("users").eq("id", 2L).execute();

        assertEquals(1, rows);

        User user = sql.select("users").eq("id", 2L).one(User.class);
        assertNull(user); // 应该被删除了
    }

    @Test
    @DisplayName("测试：事务性操作（手动连接）")
    void testTransactionWithConnection() throws Exception {
        Connection conn = null;
        try {
            conn = DataSourceManager.getConnection();
            conn.setAutoCommit(false);

            SQL sql = new SQL(conn); // 使用传入连接的构造

            sql.insert("users").set("name", "TxUser").set("email", "tx@example.com").execute();
            sql.update("users").set("name", "Updated").eq("email", "tx@example.com").execute();

            conn.commit();

            // 验证提交成功
            User user = new SQL().select("users").eq("email", "tx@example.com").one(User.class);
            assertNotNull(user);
            assertEquals("Updated", user.getName());

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            DataSourceManager.close(conn);
        }
    }
}