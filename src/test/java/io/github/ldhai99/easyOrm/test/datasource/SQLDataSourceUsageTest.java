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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SQLDataSourceUsageTest {

    private static DataSource dataSource1;
    private static DataSource dataSource2;
    private static NamedParameterJdbcTemplate npjt1;
    private static NamedParameterJdbcTemplate npjt2;

    @BeforeAll
    static void setupDataSources() throws Exception {
        // 1. 创建第一个数据源（testdb1）
        com.alibaba.druid.pool.DruidDataSource ds1 = new com.alibaba.druid.pool.DruidDataSource();
        ds1.setDriverClassName("org.h2.Driver");
        ds1.setUrl("jdbc:h2:mem:testdb1;DB_CLOSE_DELAY=-1;MODE=MySQL");
        ds1.setUsername("sa");
        ds1.setPassword("");
        dataSource1 = ds1;
        npjt1 = new NamedParameterJdbcTemplate(dataSource1);

        // 2. 创建第二个数据源（testdb2）
        com.alibaba.druid.pool.DruidDataSource ds2 = new com.alibaba.druid.pool.DruidDataSource();
        ds2.setDriverClassName("org.h2.Driver");
        ds2.setUrl("jdbc:h2:mem:testdb2;DB_CLOSE_DELAY=-1;MODE=MySQL");
        ds2.setUsername("sa");
        ds2.setPassword("");
        dataSource2 = ds2;
        npjt2 = new NamedParameterJdbcTemplate(dataSource2);

        // 3. 初始化两张表
        initializeSchema(npjt1, "users"); // testdb1.users
        initializeSchema(npjt2, "users"); // testdb2.users

        // 4. 使用 initDataSources 初始化多数据源（default = testdb1）
        Map<String, DataSource> extraDataSources = new HashMap<>();
        extraDataSources.put("slave", dataSource2); // slave -> testdb2

        DataSourceManager.initDataSources(dataSource1, extraDataSources);
    }

    private static void initializeSchema(NamedParameterJdbcTemplate npjt, String tableName) throws Exception {
        npjt.getJdbcTemplate().execute(
                "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                        "name VARCHAR(100) NOT NULL," +
                        "email VARCHAR(100) UNIQUE" +
                        ")"
        );
    }

    @AfterAll
    static void tearDown() {
        if (dataSource1 instanceof com.alibaba.druid.pool.DruidDataSource) {
            ((com.alibaba.druid.pool.DruidDataSource) dataSource1).close();
        }
        if (dataSource2 instanceof com.alibaba.druid.pool.DruidDataSource) {
            ((com.alibaba.druid.pool.DruidDataSource) dataSource2).close();
        }
    }

    // ==================== 测试模型 ====================
    @Table("users")
    public static class User {
        private Long id;
        private String name;
        private String email;

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

    // ==================== 方式一：使用默认数据源（全局自动绑定） ====================
    @Test
    @DisplayName("方式一：使用默认数据源（无需传参）")
    void testDefaultDataSource() {
        // 插入数据（使用默认数据源 testdb1）
        SQL
                .INSERT("users")
                .set("name", "DefaultUser")
                .set("email", "default@example.com")
                .execute();

        // 查询验证（应从 testdb1 读取）
        User user = SQL.SELECT("users").eq("email", "default@example.com").one(User.class);
        assertNotNull(user);
        assertEquals("DefaultUser", user.getName());
    }

    // ==================== 方式二：构造时指定数据源 ====================
    @Test
    @DisplayName("方式二：在 SQL 构造时传入指定数据源")
    void testSpecifiedDataSource() {
        // 使用 dataSource2（testdb2）构造 SQL
        SQL sql = new SQL(dataSource2); // 显式指定数据源

        sql.insert("users")
                .set("name", "SpecifiedUser")
                .set("email", "specified@example.com")
                .execute();

        // 验证数据写入 testdb2
        User user = new SQL(dataSource2) // 再次指定 dataSource2 查询
                .select("users")
                .eq("email", "specified@example.com")
                .one(User.class);

        assertNotNull(user);
        assertEquals("SpecifiedUser", user.getName());

        // 验证 testdb1 没有这条数据
        User userInDb1 = new SQL() // 使用默认数据源（testdb1）
                .select("users")
                .eq("email", "specified@example.com")
                .one(User.class);
        assertNull(userInDb1);
    }

    // ==================== 方式三：动态切换数据源（ThreadLocal 上下文） ====================
    @Test
    @DisplayName("方式三：通过 DataSourceManager 动态切换数据源")
    void testDynamicDataSourceSwitch() {
        try {
            // 设置当前线程使用 "slave" 数据源（即 testdb2）
            DataSourceManager.setCurrentDataSourceName("slave");

            // 创建 SQL（不传数据源，自动使用当前上下文）
            SQL sql = new SQL();

            sql.insert("users")
                    .set("name", "DynamicUser")
                    .set("email", "dynamic@example.com")
                    .execute();

            // 查询验证（应从 testdb2 读取）
            User user = sql.select("users")
                    .eq("email", "dynamic@example.com")
                    .one(User.class);

            assertNotNull(user);
            assertEquals("DynamicUser", user.getName());

            // 验证 testdb1 没有这条数据
            User userInDefault = new SQL(dataSource1) // 默认数据源（testdb1）
                    .select("users")
                    .eq("email", "dynamic@example.com")
                    .one(User.class);
            assertNull(userInDefault);

        } finally {
            // 清理上下文，防止影响其他测试
            DataSourceManager.clearCurrentDataSourceName();
        }
    }

    @Test
    @DisplayName("10.2.3.4 优先级验证：构造传参 > 动态上下文 > 全局默认")
    void testDataSourcePriorityChain() {
        // 准备数据
        String email = "prioritytest@example.com";

        try {
            // === 1️⃣ 阶段一：验证【动态上下文】>【全局默认】 ===
            DataSourceManager.setCurrentDataSourceName("slave"); // 指向 testdb2

            // 不传参，应使用上下文（slave/testdb2）
            SQL.DELETE("users").eq("email", email).execute(); // 清理
            SQL.INSERT("users")
                    .set("name", "FromContext")
                    .set("email", email)
                    .execute();

            // 验证：数据写入了 testdb2（slave）
            User fromSlave = new SQL(dataSource2).select("users").eq("email", email).one(User.class);
            assertNotNull(fromSlave, "动态上下文应写入 slave 数据源");
            assertEquals("FromContext", fromSlave.getName());

            // 验证：testdb1（default）没有这条数据
            User fromDefault = new SQL(dataSource1).select("users").eq("email", email).one(User.class);
            assertNull(fromDefault, "全局默认数据源不应包含上下文写入的数据");
            SQL.DELETE("users").eq("email", email).execute(); // 清理

            // === 2️⃣ 阶段二：验证【构造传参】>【动态上下文】 ===
            // 强制使用 testdb1
            new SQL(dataSource1).delete("users").eq("email", email).execute(); // 确保干净
            new SQL(dataSource1).insert("users")
                    .set("name", "FromConstructor")
                    .set("email", email)
                    .execute();

            // 验证：数据写入了 testdb1（default），即使上下文是 slave
            User fromDefault2 = new SQL(dataSource1).select("users").eq("email", email).one(User.class);
            assertNotNull(fromDefault2, "构造传参应优先于动态上下文");
            assertEquals("FromConstructor", fromDefault2.getName());

            // 验证：testdb2（slave）没有这条数据
            User fromSlave2 = new SQL(dataSource2).select("users").eq("email", email).one(User.class);
            assertNull(fromSlave2, "构造传参不应影响动态上下文数据源");
            new SQL(dataSource1).delete("users").eq("email", email).execute(); // 确保干净

            // === 3️⃣ 阶段三：验证【全局默认】在无上下文、无传参时生效 ===
            DataSourceManager.clearCurrentDataSourceName(); // 清除上下文

            // 无上下文、无传参 → 应使用 default (testdb1)
            SQL.DELETE("users").eq("email", email).execute();
            SQL.INSERT("users")
                    .set("name", "FromDefault")
                    .set("email", email)
                    .execute();

            // 验证：写入了 testdb1
            User fromDefault3 = new SQL(dataSource1).select("users").eq("email", email).one(User.class);
            assertNotNull(fromDefault3, "无上下文无传参时应使用全局默认数据源");
            assertEquals("FromDefault", fromDefault3.getName());

            // 验证：testdb2 没有
            User fromSlave3 = new SQL(dataSource2).select("users").eq("email", email).one(User.class);
            assertNull(fromSlave3);
            new SQL(dataSource1).delete("users").eq("email", email).execute(); // 确保干净
        } finally {
            DataSourceManager.clearCurrentDataSourceName();
        }
    }
}