package io.github.ldhai99.easyOrm.test.dialect;

import io.github.ldhai99.easyOrm.dialect.Dialect;
import io.github.ldhai99.easyOrm.dialect.DialectManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.ldhai99.easyOrm.test.dialect.DialectStringEscapeTest.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("1. escapeSingleQuotes 方法测试")
public class EscapeSingleQuotesTest {

    @Test
    @DisplayName("测试所有方言的 escapeSingleQuotes")
    void testEscapeSingleQuotesAllDialects() {
        System.out.println("\n" + "=====================================");
        System.out.println("测试 escapeSingleQuotes 方法 - 所有方言");
        System.out.println("规则：将单个单引号 ' 转义为两个单引号 ''");
        System.out.println("=====================================");

        // 测试数据：输入 → 期望输出
        Object[][] testData = {
                // {输入, 期望输出, 说明}
                {"O'Reilly", "O''Reilly", "单个单引号"},
                {"It's a test", "It''s a test", "句子中的单引号"},
                {"No quotes here", "No quotes here", "无单引号"},
                {"''", "''''", "两个单引号（空字符串）"},
                {"'''", "''''''", "三个单引号"},
                {"''''", "''''''''", "四个单引号"},
                {"'''''", "''''''''''", "五个单引号"},
                {"'test'", "''test''", "单引号包围的字符串"},
                {"test with 'multiple' quotes 'inside'", "test with ''multiple'' quotes ''inside''", "多个引号"},
                {"O''Reilly", "O''''Reilly", "已经有两个单引号"},
                {"O'''Reilly", "O''''''Reilly", "已经有三个单引号"},
                {"", "", "空字符串"},
                {"'", "''", "只有一个单引号"},
                {"'a'b'c'd'", "''a''b''c''d''", "交替的单引号"},
                {"'''''test'''''", "''''''''''test''''''''''", "复杂的引号组合"},
                {null, null, "null输入"}
        };

        // 测试各种方言
        Dialect[] dialects = {
                DialectManager.MYSQL_DIALECT,
                DialectManager.ORACLE_DIALECT,
                DialectManager.POSTGRESQL_DIALECT,
                DialectManager.FALLBACK_DIALECT
        };

        boolean allPassed = true;
        int totalTests = 0;
        int passedTests = 0;

        for (Dialect dialect : dialects) {
            System.out.println("\n" + "=====================================");
            System.out.println("方言: " + dialect.getClass().getSimpleName());
            System.out.println("=====================================");

            System.out.printf("%-35s %-35s %-15s %s\n",
                    "输入", "输出", "期望", "结果");
            System.out.printf("%-35s %-35s %-15s %s\n",
                    "----", "----", "----", "----");

            for (Object[] data : testData) {
                totalTests++;
                String input = (String) data[0];
                String expected = (String) data[1];
                String description = (String) data[2];

                String result = dialect.escapeSingleQuotes(input);
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
                    }
                }

                System.out.printf("%-35s %-35s %-15s %s\n",
                        formatForDisplay(input),
                        formatForDisplay(result),
                        formatForDisplay(expected),
                        status);
            }
        }

        System.out.println("\n" + "=====================================");
        System.out.printf("测试统计: %d/%d 通过\n", passedTests, totalTests);
        if (allPassed) {
            System.out.println("✅ 所有方言测试通过！");
        } else {
            System.out.println("❌ 有测试失败！");
            fail("有测试失败，请查看上面的输出");
        }
    }

    @Test
    @DisplayName("测试不同方言的一致性")
    void testDialectConsistency() {
        System.out.println("\n" + "=====================================");
        System.out.println("测试不同方言转义一致性");
        System.out.println("=====================================");

        String[] testStrings = {
                "O'Reilly",
                "It's a test",
                "O''Brien",
                "'''triple'''",
                "test'with'multiple'quotes",
                "''",
                "'''",
                "''''",
                "'single'",
                ""
        };

        System.out.printf("%-30s %-20s %-20s %-20s %-20s\n",
                "原始字符串", "MySQL", "Oracle", "PostgreSQL", "Default");
        System.out.printf("%-30s %-20s %-20s %-20s %-20s\n",
                "------------", "-----", "------", "-----------", "-------");

        boolean allConsistent = true;

        for (String str : testStrings) {
            String mysql = DialectManager.MYSQL_DIALECT.escapeSingleQuotes(str);
            String oracle = DialectManager.ORACLE_DIALECT.escapeSingleQuotes(str);
            String postgres = DialectManager.POSTGRESQL_DIALECT.escapeSingleQuotes(str);
            String defaultDialect = DialectManager.FALLBACK_DIALECT.escapeSingleQuotes(str);

            boolean consistent = mysql.equals(oracle)
                    && mysql.equals(postgres)
                    && mysql.equals(defaultDialect);

            System.out.printf("%-30s %-20s %-20s %-20s %-20s %s\n",
                    formatForDisplay(str, 25),
                    formatForDisplay(mysql, 15),
                    formatForDisplay(oracle, 15),
                    formatForDisplay(postgres, 15),
                    formatForDisplay(defaultDialect, 15),
                    consistent ? "✅" : "❌"
            );

            if (!consistent) {
                allConsistent = false;
            }
        }

        assertTrue(allConsistent, "所有方言的转义结果应该一致");
        System.out.println("\n✅ 所有方言转义一致！");
    }

    @Test
    @DisplayName("视觉化转义过程")
    void testVisualEscape() {
        System.out.println("\n" + "=====================================");
        System.out.println("视觉化转义过程展示");
        System.out.println("=====================================");

        String[] testStrings = {
                "O'Reilly",
                "It's OK",
                "O''Brien",
                "'''triple'''",
                "test'with'multiple'quotes",
                "'a'b'c'"
        };

        Dialect dialect = DialectManager.FALLBACK_DIALECT;

        System.out.println("原始字符串 → 转义过程 → 转义结果");
        System.out.println("=====================================");

        for (String original : testStrings) {
            String escaped = dialect.escapeSingleQuotes(original);

            System.out.println("原始:  " + highlightQuotes(original));
            System.out.println("过程:  " + showEscapeProcess(original));
            System.out.println("结果:  " + highlightQuotes(escaped));

            // 分析
            int originalQuotes = countChar(original, '\'');
            int escapedQuotes = countChar(escaped, '\'');
            System.out.printf("分析:  %d个单引号 → %d个单引号 (应为%d) %s\n\n",
                    originalQuotes, escapedQuotes, originalQuotes * 2,
                    escapedQuotes == originalQuotes * 2 ? "✅" : "❌"
            );
        }
    }
}
