package io.github.ldhai99.easyOrm.test.dialect;

import io.github.ldhai99.easyOrm.dialect.Dialect;
import io.github.ldhai99.easyOrm.dialect.DialectManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.ldhai99.easyOrm.test.dialect.DialectStringEscapeTest.analyzeDifference;
import static io.github.ldhai99.easyOrm.test.dialect.DialectStringEscapeTest.formatForDisplay;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public @DisplayName("2. escapeStringLiteralsInSql 方法测试")
class EscapeStringLiteralsInSqlTest {

    @Test
    @DisplayName("测试 SQL 字符串字面量转义")
    void testEscapeStringLiteralsInSql() {
        System.out.println("\n" + "=====================================");
        System.out.println("测试 escapeStringLiteralsInSql 方法");
        System.out.println("规则：处理SQL字符串字面量中的单引号转义");
        System.out.println("=====================================");

        // 测试数据：输入SQL → 期望输出SQL
        Object[][] testData = {
                // {输入SQL, 期望输出SQL, 说明}
                {"'O'Reilly'", "'O''Reilly'", "简单字符串字面量"},
                {"'It''s test'", "'It''''s test'", "已有转义引号"},
                {"''''", "''''''''", "四个单引号字面量"},
                {"'test'", "'test'", "无需要转义的引号"},
                {"SELECT * FROM users WHERE name = 'O'Reilly'",
                        "SELECT * FROM users WHERE name = 'O''Reilly'",
                        "SQL语句中的字符串"},
                {"UPDATE users SET name = 'O''Brien' WHERE id = 1",
                        "UPDATE users SET name = 'O''''Brien' WHERE id = 1",
                        "UPDATE语句"},
                {"INSERT INTO books (title) VALUES ('It''s a book')",
                        "INSERT INTO books (title) VALUES ('It''''s a book')",
                        "INSERT语句"},
                {"SELECT * FROM t WHERE a='x' AND b='y'",
                        "SELECT * FROM t WHERE a='x' AND b='y'",
                        "多个简单字符串"},
                {"SELECT * FROM t WHERE a='O'Reilly' AND b='O''Brien'",
                        "SELECT * FROM t WHERE a='O''Reilly' AND b='O''''Brien'",
                        "多个复杂字符串"},
                {"'test''quotes'''", "'test''''quotes'''''", "嵌套引号"},
                {"column_name", "column_name", "非字符串字面量"},
                {"123", "123", "数字"},
                {"SELECT 1 + 1", "SELECT 1 + 1", "表达式"},
                {"'", "'", "单个单引号"},
                {"''", "''''", "两个单引号"},
                {"'''", "''''''", "三个单引号"},
                {"'test\\'quotes'", "'test\\''quotes'", "包含反斜杠"},
                {"", "", "空字符串"},
                {null, null, "null输入"},
                {"SELECT * FROM users WHERE name = '''test''' AND age > 20",
                        "SELECT * FROM users WHERE name = '''''test''''' AND age > 20",
                        "复杂SQL语句"}
        };

        Dialect dialect = DialectManager.FALLBACK_DIALECT;

        System.out.printf("%-60s %-60s %-20s %s\n",
                "输入SQL", "输出SQL", "期望SQL", "结果");
        System.out.printf("%-60s %-60s %-20s %s\n",
                "------", "------", "------", "----");

        boolean allPassed = true;
        int totalTests = 0;
        int passedTests = 0;

        for (Object[] data : testData) {
            totalTests++;
            String input = (String) data[0];
            String expected = (String) data[1];
            String description = (String) data[2];

            String result = dialect.escapeStringLiteralsInSql(input);
            String status;

            if (input == null) {
                if (result == null) {
                    status = "✅";
                    passedTests++;
                } else {
                    status = "❌";
                    allPassed = false;
                }
            } else {
                if (expected.equals(result)) {
                    status = "✅";
                    passedTests++;
                } else {
                    status = "❌";
                    allPassed = false;

                    // 显示详细错误信息
                    System.out.println("\n⚠️ 测试失败详情：");
                    System.out.println("   说明: " + description);
                    System.out.println("   输入: " + formatForDisplay(input, 100));
                    System.out.println("   输出: " + formatForDisplay(result, 100));
                    System.out.println("   期望: " + formatForDisplay(expected, 100));
                    System.out.println("   差异分析:");
                    analyzeDifference(input, expected, result);
                }
            }

            System.out.printf("%-60s %-60s %-20s %s\n",
                    formatForDisplay(input, 55),
                    formatForDisplay(result, 55),
                    formatForDisplay(expected, 55),
                    status);
        }

        System.out.println("\n" + "=====================================");
        System.out.printf("测试统计: %d/%d 通过\n", passedTests, totalTests);
        if (allPassed) {
            System.out.println("✅ 所有测试通过！");
        } else {
            System.out.println("❌ 有测试失败！");
            fail("有测试失败，请查看上面的输出");
        }
    }

    @Test
    @DisplayName("测试 SQL 注入防护")
    void testSqlInjectionPrevention() {
        System.out.println("\n" + "=====================================");
        System.out.println("SQL注入防护测试");
        System.out.println("=====================================");

        // 常见SQL注入攻击向量
        String[][] injectionTests = {
                {"'; DROP TABLE users; --",
                        "''; DROP TABLE users; --",
                        "经典DROP注入"},
                {"' OR '1'='1",
                        "'' OR ''1''=''1",
                        "永真条件注入"},
                {"admin' --",
                        "admin'' --",
                        "注释绕过"},
                {"x'; UPDATE users SET password = 'hacked' WHERE username = 'admin",
                        "x''; UPDATE users SET password = ''hacked'' WHERE username = ''admin",
                        "UPDATE注入"},
                {"test'; SELECT * FROM users; --",
                        "test''; SELECT * FROM users; --",
                        "SELECT注入"},
                {"'); DELETE FROM users WHERE '1'='1",
                        "''); DELETE FROM users WHERE ''1''=''1",
                        "DELETE注入"}
        };

        Dialect dialect = DialectManager.FALLBACK_DIALECT;

        System.out.printf("%-50s %-50s %-20s %s\n",
                "恶意输入", "安全输出", "攻击类型", "结果");
        System.out.printf("%-50s %-50s %-20s %s\n",
                "--------", "--------", "--------", "----");

        boolean allSafe = true;

        for (String[] test : injectionTests) {
            String malicious = test[0];
            String expectedSafe = test[1];
            String attackType = test[2];

            String actualSafe = dialect.escapeSingleQuotes(malicious);
            boolean isSafe = expectedSafe.equals(actualSafe);

            System.out.printf("%-50s %-50s %-20s %s\n",
                    formatForDisplay(malicious, 45),
                    formatForDisplay(actualSafe, 45),
                    attackType,
                    isSafe ? "✅" : "❌"
            );

            if (!isSafe) {
                allSafe = false;
            }
        }

        assertTrue(allSafe, "所有SQL注入测试应该通过");
        System.out.println("\n✅ SQL注入防护测试通过！");
    }
}