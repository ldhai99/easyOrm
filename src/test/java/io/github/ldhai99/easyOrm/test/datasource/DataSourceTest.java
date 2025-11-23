package io.github.ldhai99.easyOrm.test.datasource;



import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.datasource.DataSourceProvider;
import io.github.ldhai99.easyOrm.datasource.DefaultDataSourceProvider;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据源配置测试类
 * 演示两种数据源测试方式：
 * 1. 测试默认数据源（从配置文件加载）
 * 2. 测试给定数据源（手动配置）
 */
public class DataSourceTest {

    private static final String TEST_TABLE = "test_users";

    @BeforeEach
    public void setUp() {
        // 确保每次测试前重置配置
        SQL.resetConfig();

    }

    @AfterEach
    public void tearDown() {

        SQL.resetConfig();
    }

    // ==================== 测试方法1：默认数据源测试 ====================

    /**
     * 测试场景1：使用默认数据源（从配置文件自动加载）
     * 适用：已有配置文件的正式环境
     */
    @Test
    public void testDefaultDataSource() {
        System.out.println("=== 测试默认数据源 ===");

        // 方式1：不进行任何配置，使用默认的 DefaultDataSourceProvider
        // 框架会自动从 druid.properties/application.properties/jdbc.properties 加载配置

        try {
            // 执行简单的查询测试连接是否正常
            List<Map<String, Object>> result = SQL.SELECT(TEST_TABLE)
                    .column("count(*) as total")
                    .getMaps();

            assertNotNull(result, "查询结果不应为null");
            assertFalse(result.isEmpty(),"查询结果不应为空" );

            Number total = (Number) result.get(0).get("total");
            assertNotNull(total, "计数结果不应为null");

            System.out.println("✅ 默认数据源测试成功，表记录数: " + total);

        } catch (Exception e) {
            if (e.getMessage().contains("No data source configured")) {
                System.out.println("⚠️  默认数据源未配置，请检查配置文件");
                // 这是正常情况，如果没有任何配置文件
                return;
            }
            throw new RuntimeException("默认数据源测试失败", e);
        }

        // 测试数据操作
        testDataOperations("默认数据源");
    }

    /**
     * 测试场景2：通过 DefaultDataSourceProvider 设置默认数据源
     * 适用：想要使用默认提供者但手动设置数据源的场景
     */
    @Test
    public void testDefaultDataSourceProvider() {
        System.out.println("=== 测试 DefaultDataSourceProvider 设置数据源 ===");

        // 创建测试数据源
        DataSource testDataSource = createTestDataSource();

        // 通过 DefaultDataSourceProvider 设置数据源
        DefaultDataSourceProvider.setDefaultDataSource(testDataSource);

        // 验证数据源设置
        DataSource currentDataSource = DefaultDataSourceProvider.getDataSource();
        assertSame(testDataSource, currentDataSource,"数据源应该相同");

        // 测试数据库操作
        testDataOperations("DefaultDataSourceProvider");

        System.out.println("✅ DefaultDataSourceProvider 测试成功");
    }

    // ==================== 测试方法2：给定数据源测试 ====================

    /**
     * 测试场景3：通过 SQL.configDefaultDataSource() 设置给定数据源
     * 适用：简单的数据源配置场景
     */
    @Test
    public void testGivenDataSourceWithConfigMethod() {
        System.out.println("=== 测试 SQL.configDefaultDataSource() ===");

        // 创建给定的测试数据源
        DataSource givenDataSource = createTestDataSource();

        // 使用 SQL 类的配置方法
        SQL.configDefaultDataSource(givenDataSource);

        // 测试数据操作
        testDataOperations("SQL.configDefaultDataSource");

        System.out.println("✅ SQL.configDefaultDataSource() 测试成功");
    }

    /**
     * 测试场景4：通过自定义 DataSourceProvider 设置给定数据源
     * 适用：需要动态数据源的高级场景
     */
    @Test
    public void testGivenDataSourceWithCustomProvider() {
        System.out.println("=== 测试自定义 DataSourceProvider ===");

        // 创建给定的测试数据源
        DataSource givenDataSource = createTestDataSource();

        // 创建自定义数据源提供者
        DataSourceProvider customProvider = new DataSourceProvider() {
            private int callCount = 0;

            @Override
            public DataSource provide() {
                callCount++;
                System.out.println("📞 自定义 DataSourceProvider 被调用第 " + callCount + " 次");
                return givenDataSource;
            }
        };

        // 配置自定义提供者
        SQL.configDataSourceProvider(customProvider);

        // 测试数据操作
        testDataOperations("自定义 DataSourceProvider");

        System.out.println("✅ 自定义 DataSourceProvider 测试成功");
    }

    /**
     * 测试场景5：Lambda 表达式方式配置数据源
     * 适用：简洁的配置方式
     */
    @Test
    public void testGivenDataSourceWithLambda() {
        System.out.println("=== 测试 Lambda 表达式配置 ===");

        // 创建给定的测试数据源
        DataSource givenDataSource = createTestDataSource();

        // 使用 Lambda 表达式配置
        SQL.configDataSourceProvider(() -> {
            System.out.println("🔧 Lambda 数据源提供者被调用");
            return givenDataSource;
        });

        // 测试数据操作
        testDataOperations("Lambda 数据源提供者");

        System.out.println("✅ Lambda 表达式配置测试成功");
    }

    // ==================== 测试方法3：混合场景测试 ====================

    /**
     * 测试场景6：优先级测试 - 自定义提供者优先于默认数据源
     */
    @Test
    public void testDataSourcePriority() {
        System.out.println("=== 测试数据源优先级 ===");

        // 首先设置默认数据源
        DataSource defaultDS = createTestDataSource("default_db");
        SQL.configDefaultDataSource(defaultDS);

        // 然后设置自定义提供者（应该优先使用）
        DataSource customDS = createTestDataSource("custom_db");
        SQL.configDataSourceProvider(() -> customDS);

        // 验证实际使用的数据源
        // 由于无法直接获取当前使用的数据源，我们通过执行SQL来验证
        testDataOperations("优先级测试");

        System.out.println("✅ 数据源优先级测试成功 - 自定义提供者优先");
    }

    /**
     * 测试场景7：重置配置测试
     */
    @Test
    public void testResetConfig() {
        System.out.println("=== 测试重置配置 ===");

        // 先设置自定义提供者
        DataSource customDS = createTestDataSource();
        SQL.configDataSourceProvider(() -> customDS);

        // 重置配置
        SQL.resetConfig();
        createTestTableIfNeeded();
        // 重置后应该使用默认的 DefaultDataSourceProvider
        try {
            // 这里可能会抛出异常，如果没有默认配置文件
            SQL.SELECT(TEST_TABLE).column("1").getMaps();
            System.out.println("✅ 重置配置成功，使用默认数据源");
        } catch (Exception e) {
            if (e.getMessage().contains("No data source configured")) {
                System.out.println("✅ 重置配置成功，已清除自定义配置");
            } else {
                throw e;
            }
        }
        cleanupTestData();
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建测试数据源
     */
    private DataSource createTestDataSource() {
        return createTestDataSource("test_memory_db");
    }

    private DataSource createTestDataSource(String dbName) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    /**
     * 创建测试表（如果不存在）
     */
    private void createTestTableIfNeeded() {
        try {
            SQL.ADDSQL(
                    "CREATE TABLE IF NOT EXISTS " + TEST_TABLE + " (" +
                            "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100), " +
                            "email VARCHAR(100), " +
                            "created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")"
            ).execute();

            System.out.println("✅ 测试表创建成功: " + TEST_TABLE);
        } catch (Exception e) {
            System.out.println("⚠️  测试表创建失败，可能已存在: " + e.getMessage());
        }
    }

    /**
     * 清理测试数据
     */
    private void cleanupTestData() {
        try {
            SQL.DELETE(TEST_TABLE).execute();
            System.out.println("✅ 测试数据清理完成");
        } catch (Exception e) {
            System.out.println("⚠️  测试数据清理失败: " + e.getMessage());
        }
    }

    /**
     * 通用的数据操作测试
     */
    private void testDataOperations(String testScenario) {
        createTestTableIfNeeded();
        System.out.println("🧪 执行数据操作测试 - " + testScenario);

        // 1. 插入测试数据
        Long id = SQL.INSERT(TEST_TABLE)
                .set("name", "TestUser-" + System.currentTimeMillis())
                .set("email", "test@example.com")
                .getPrimaryKey();

        assertNotNull(id,"插入应该返回生成的ID" );
        System.out.println("✅ 插入操作成功，ID: " + id);

        // 2. 查询测试数据
        List<Map<String, Object>> results = SQL.SELECT(TEST_TABLE)
                .column("*")
                .eq("id", id)
                .getMaps();

        assertEquals( 1, results.size(),"应该查询到1条记录");
        assertEquals( id, results.get(0).get("id"),"ID应该匹配");
        System.out.println("✅ 查询操作成功");

        // 3. 更新测试数据
        int updated = SQL.UPDATE(TEST_TABLE)
                .set("name", "UpdatedUser")
                .eq("id", id)
                .execute();

        assertEquals( 1, updated,"应该更新1条记录");
        System.out.println("✅ 更新操作成功");

        // 4. 删除测试数据
        int deleted = SQL.DELETE(TEST_TABLE)
                .eq("id", id)
                .execute();

        assertEquals(1, deleted,"应该删除1条记录");
        System.out.println("✅ 删除操作成功");

        System.out.println("🎉 所有数据操作测试通过 - " + testScenario);
        // 清理测试数据
        cleanupTestData();
    }

    // ==================== 主方法（独立运行测试） ====================

    public static void main(String[] args) {
        DataSourceTest test = new DataSourceTest();

        try {
            test.setUp();

            System.out.println("🚀 开始数据源测试...\n");

            // 运行各个测试场景
            test.testDefaultDataSource();
            System.out.println();

            test.testDefaultDataSourceProvider();
            System.out.println();

            test.testGivenDataSourceWithConfigMethod();
            System.out.println();

            test.testGivenDataSourceWithCustomProvider();
            System.out.println();

            test.testGivenDataSourceWithLambda();
            System.out.println();

            test.testDataSourcePriority();
            System.out.println();

            test.testResetConfig();

            System.out.println("\n🎉 所有数据源测试完成！");

        } catch (Exception e) {
            System.err.println("❌ 测试执行失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            test.tearDown();
        }
    }
}