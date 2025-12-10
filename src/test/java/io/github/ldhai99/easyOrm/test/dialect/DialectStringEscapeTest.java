package io.github.ldhai99.easyOrm.test.dialect;



import io.github.ldhai99.easyOrm.dialect.BaseDialect;
import io.github.ldhai99.easyOrm.dialect.Dialect;
import io.github.ldhai99.easyOrm.dialect.DialectManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 完整的Dialect字符串转义测试类
 */
@DisplayName("Dialect 字符串转义测试")
public class DialectStringEscapeTest {

    // 模拟 LikeType 枚举
    enum TestLikeType {
        CONTAINS,
        STARTS_WITH,
        ENDS_WITH,
        CUSTOM
    }

    @BeforeAll
    static void setup() {
        System.out.println("=====================================");
        System.out.println("开始 Dialect 字符串转义测试");
        System.out.println("=====================================");
    }





    @Nested
    @DisplayName("3. LIKE 相关转义测试")
    class LikeEscapeTest {

        @Test
        @DisplayName("测试 escapeLikeValue 方法")
        void testEscapeLikeValue() {
            System.out.println("\n" + "=====================================");
            System.out.println("测试 LIKE 值转义");
            System.out.println("规则：转义 % _ \\ 字符");
            System.out.println("=====================================");

            // 使用BaseDialect（因为escapeLikeValue在BaseDialect中实现）
            BaseDialect dialect = (BaseDialect) DialectManager.FALLBACK_DIALECT;

            String[][] testCases = {
                    {"test%value", "test\\%value", "包含百分号"},
                    {"test_value", "test\\_value", "包含下划线"},
                    {"test\\value", "test\\\\value", "包含反斜杠"},
                    {"normal value", "normal value", "普通字符串"},
                    {"%test_value\\%", "\\%test\\_value\\\\\\%", "混合特殊字符"},
                    {"", "", "空字符串"},
                    {"100%", "100\\%", "以百分号结尾"},
                    {"_start", "\\_start", "以下划线开头"},
                    {"test\\_escape", "test\\\\\\_escape", "复杂转义"},
                    {null, null, "null输入"}
            };

            System.out.printf("%-30s %-30s %-20s %s\n",
                    "输入值", "输出值", "期望值", "结果");
            System.out.printf("%-30s %-30s %-20s %s\n",
                    "-----", "-----", "-----", "----");

            for (String[] testCase : testCases) {
                String input = testCase[0];
                String expected = testCase[1];
                String description = testCase[2];

                String result = dialect.escapeLikeValue(input);
                String status;

                if (input == null) {
                    status = (result == null) ? "✅" : "❌";
                    assertNull(result);
                } else {
                    status = expected.equals(result) ? "✅" : "❌";
                    assertEquals(expected, result, description);
                }

                System.out.printf("%-30s %-30s %-20s %s\n",
                        formatForDisplay(input, 25),
                        formatForDisplay(result, 25),
                        formatForDisplay(expected, 25),
                        status);
            }

            System.out.println("\n✅ LIKE转义测试完成！");
        }

        @Test
        @DisplayName("测试 processLikeValue 方法")
        void testProcessLikeValue() {
            System.out.println("\n" + "=====================================");
            System.out.println("测试 processLikeValue 方法");
            System.out.println("=====================================");

            BaseDialect dialect = (BaseDialect) DialectManager.FALLBACK_DIALECT;

            Object[][] testCases = {
                    {"test", TestLikeType.CONTAINS, "%test%", "包含匹配"},
                    {"test", TestLikeType.STARTS_WITH, "test%", "开头匹配"},
                    {"test", TestLikeType.ENDS_WITH, "%test", "结尾匹配"},
                    {"test", TestLikeType.CUSTOM, "test", "自定义匹配"},
                    {"test%value", TestLikeType.CONTAINS, "%test\\%value%", "包含特殊字符"},
                    {"test_value", TestLikeType.STARTS_WITH, "test\\_value%", "下划线开头匹配"},
                    {"test\\value", TestLikeType.ENDS_WITH, "%test\\\\value", "反斜杠结尾匹配"},
                    {null, TestLikeType.CONTAINS, null, "null输入"}
            };

            System.out.printf("%-20s %-15s %-30s %-20s %s\n",
                    "输入值", "类型", "输出值", "期望值", "结果");
            System.out.printf("%-20s %-15s %-30s %-20s %s\n",
                    "-----", "----", "-----", "-----", "----");

            for (Object[] testCase : testCases) {
                String input = (String) testCase[0];
                TestLikeType likeType = (TestLikeType) testCase[1];
                String expected = (String) testCase[2];
                String description = (String) testCase[3];

                String result = "";//dialect.processLikeValue(input, likeType);
                String status;

                if (input == null) {
                    status = (result == null) ? "✅" : "❌";
                    assertNull(result);
                } else {
                    status = expected.equals(result) ? "✅" : "❌";
                    assertEquals(expected, result, description);
                }

                System.out.printf("%-20s %-15s %-30s %-20s %s\n",
                        formatForDisplay(input, 15),
                        likeType,
                        formatForDisplay(result, 25),
                        formatForDisplay(expected, 25),
                        status);
            }

            System.out.println("\n✅ LIKE处理测试完成！");
        }
    }

    @Nested
    @DisplayName("4. 边界和性能测试")
    class EdgeCaseAndPerformanceTest {

        @Test
        @DisplayName("测试边界情况")
        void testEdgeCases() {
            System.out.println("\n" + "=====================================");
            System.out.println("边界情况测试");
            System.out.println("=====================================");

            Dialect dialect = DialectManager.FALLBACK_DIALECT;

            // 测试非常长的字符串
            System.out.println("1. 长字符串测试:");
            StringBuilder longString = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longString.append("O'Reilly's Book").append(i).append("', ");
            }
            String longInput = longString.toString();

            long startTime = System.nanoTime();
            String escaped = dialect.escapeSingleQuotes(longInput);
            long endTime = System.nanoTime();

            System.out.printf("   长度: %d 字符\n", longInput.length());
            System.out.printf("   耗时: %.3f 毫秒\n", (endTime - startTime) / 1_000_000.0);

            int originalQuotes = countChar(longInput, '\'');
            int escapedQuotes = countChar(escaped, '\'');
            System.out.printf("   引号数量: %d → %d %s\n\n",
                    originalQuotes, escapedQuotes,
                    escapedQuotes == originalQuotes * 2 ? "✅" : "❌"
            );

            // 测试Unicode字符
            System.out.println("2. Unicode字符测试:");
            String unicodeInput = "测试'中文'引号'和'emoji🚀'";
            String unicodeEscaped = dialect.escapeSingleQuotes(unicodeInput);
            System.out.println("   原始: " + unicodeInput);
            System.out.println("   转义: " + unicodeEscaped);
            System.out.println("   结果: " +
                    (unicodeEscaped.equals("测试''中文''引号''和''emoji🚀''") ? "✅" : "❌") + "\n");

            // 测试特殊字符组合
            System.out.println("3. 特殊字符组合测试:");
            String[] specialCases = {
                    "\t制表符'测试'",
                    "换行符\n'测试'",
                    "回车符\r'测试'",
                    "混合\t\n\r'测试'"
            };

            for (String special : specialCases) {
                String result = dialect.escapeSingleQuotes(special);
                System.out.printf("   输入: %s\n", escapeSpecialChars(special));
                System.out.printf("   输出: %s\n", escapeSpecialChars(result));
                System.out.println();
            }
        }

        @Test
        @DisplayName("性能基准测试")
        void testPerformance() {
            System.out.println("\n" + "=====================================");
            System.out.println("性能基准测试");
            System.out.println("=====================================");

            Dialect dialect = DialectManager.FALLBACK_DIALECT;

            // 准备测试数据
            int[] sizes = {100, 1000, 10000, 100000};

            for (int size : sizes) {
                System.out.printf("\n测试大小: %,d 字符\n", size);

                // 构建测试字符串
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    if (i % 10 == 0) {
                        builder.append("'");  // 每10个字符加一个单引号
                    } else {
                        builder.append("a");
                    }
                }
                String testString = builder.toString();

                // 预热
                for (int i = 0; i < 10; i++) {
                    dialect.escapeSingleQuotes(testString);
                }

                // 正式测试
                int iterations = Math.max(1000, 1000000 / size);
                long totalTime = 0;

                for (int i = 0; i < iterations; i++) {
                    long start = System.nanoTime();
                    dialect.escapeSingleQuotes(testString);
                    long end = System.nanoTime();
                    totalTime += (end - start);
                }

                double avgTime = totalTime / (double) iterations;
                double throughput = size / (avgTime / 1_000_000_000.0);

                System.out.printf("   迭代次数: %,d\n", iterations);
                System.out.printf("   平均耗时: %.3f ns\n", avgTime);
                System.out.printf("   吞吐量: %.2f 字符/秒\n", throughput);
            }
        }
    }

    // ===================== 辅助方法 =====================

    public  static String formatForDisplay(String str) {
        return formatForDisplay(str, 30);
    }

    public  static String formatForDisplay(String str, int maxLength) {
        if (str == null) return "null";
        if (str.length() > maxLength) {
            return "\"" + str.substring(0, maxLength - 3) + "...\"";
        }
        return "\"" + str + "\"";
    }

    public  static String highlightQuotes(String str) {
        if (str == null) return "null";
        return str.replace("'", "[']");
    }

    public  static String showEscapeProcess(String str) {
        if (str == null) return "null";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\'') {
                result.append("['] → [''][']");
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public  static int countChar(String str, char ch) {
        if (str == null) return 0;
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) count++;
        }
        return count;
    }

    public  static void analyzeDifference(String input, String expected, String actual) {
        if (expected == null || actual == null) {
            System.out.println("    其中一个为null");
            return;
        }

        if (expected.length() != actual.length()) {
            System.out.printf("    长度不同: 期望=%d, 实际=%d\n",
                    expected.length(), actual.length());
        }

        int minLength = Math.min(expected.length(), actual.length());
        int differences = 0;

        for (int i = 0; i < minLength; i++) {
            char expChar = expected.charAt(i);
            char actChar = actual.charAt(i);

            if (expChar != actChar) {
                differences++;
                System.out.printf("    位置 %d: 期望 '%c'(%d), 实际 '%c'(%d)\n",
                        i, expChar, (int)expChar, actChar, (int)actChar);
                if (differences >= 3) {
                    System.out.println("    ... (更多差异已省略)");
                    break;
                }
            }
        }

        if (differences == 0 && expected.length() == actual.length()) {
            System.out.println("    无字符差异，但字符串不相等（可能是不可见字符）");
        }
    }

    public static String escapeSpecialChars(String str) {
        if (str == null) return "null";
        return str.replace("\t", "\\t")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("'", "\\'");
    }

    // ===================== 主方法（用于独立运行） =====================
    public static void main(String[] args) {
        System.out.println("开始运行完整的 Dialect 字符串转义测试...");

        DialectStringEscapeTest test = new DialectStringEscapeTest();

        // 依次运行各个测试
        try {
//            new EscapeSingleQuotesTest().testEscapeSingleQuotesAllDialects();
//            new EscapeSingleQuotesTest().testDialectConsistency();
//            new EscapeSingleQuotesTest().testVisualEscape();
//
//            new EscapeStringLiteralsInSqlTest().testEscapeStringLiteralsInSql();
//            new EscapeStringLiteralsInSqlTest().testSqlInjectionPrevention();
//
//            new LikeEscapeTest().testEscapeLikeValue();
//            new LikeEscapeTest().testProcessLikeValue();
//
//            new EdgeCaseAndPerformanceTest().testEdgeCases();
//            new EdgeCaseAndPerformanceTest().testPerformance();

            System.out.println("\n" + "=====================================");
            System.out.println("✅ 所有测试完成！");
            System.out.println("=====================================");

        } catch (AssertionError e) {
            System.err.println("\n❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("\n❌ 发生异常: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}