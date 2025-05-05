package io.github.ldhai99.easyOrm.dao;

import io.github.ldhai99.easyOrm.annotation.TableField;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.UUID;

public class DataTransformer {




    /**
     * 将数据库值转换为 Java 字段值
     */
    // 默认日期时间格式
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    // 新增常量
    private static final DateTimeFormatter LEGACY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    /**
     * 将 Java 字段值转换为适合数据库存储的值
     */
    public static Object toDatabaseValue(Field field, Object value) {
        if (value == null) return null;

        Class<?> fieldType = field.getType();

        if (fieldType == LocalDateTime.class) {
            return ((LocalDateTime) value).format(DATE_TIME_FORMATTER);
        }
        // 可扩展更多类型转换逻辑...

        return value;
    }
    /**
     * 将数据库值转换为 Java 字段值
     */
    public static Object toJavaValue(Field field, Object value) {
        if (value == null) return null;

        Class<?> fieldType = field.getType();

        // 通用类型转换（基于 SQL 类型）
        if (value instanceof String) {
            return convertStringToJavaType(fieldType, (String) value);
        } else if (value instanceof Number) {
            return convertNumberToJavaType(fieldType, (Number) value);
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else if (value instanceof Timestamp) {
            return convertTimestampToJavaType(fieldType, (Timestamp) value);
        } else if (value instanceof Date) {
            return convertSqlDateToJavaType(fieldType, (Date) value);
        } else if (value instanceof Time) {
            return convertSqlTimeToJavaType(fieldType, (Time) value);
        } else if (value instanceof java.util.Date) {
            return convertUtilDateToJavaType(fieldType, (java.util.Date) value);
        } else if (value instanceof BigDecimal) {
            return convertBigDecimalToJavaType(fieldType, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            return convertBigIntegerToJavaType(fieldType, (BigInteger) value);
        } else if (value instanceof byte[]) {
            return value; // 直接返回字节数组
        } else if (value instanceof UUID) {
            return value; // 直接返回 UUID
        }

        // 其他类型（如 List、Set、Map 等）需自定义处理
        return value;
    }

    /**
     * 将 String 转换为 Java 字段类型
     */
    private static Object convertStringToJavaType(Class<?> fieldType, String value) {
        if (fieldType == String.class) return value;


        if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.parseInt(value);
        } else if (fieldType == Long.class || fieldType == long.class) {
            return Long.parseLong(value);
        } else if (fieldType == Double.class || fieldType == double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == Float.class || fieldType == float.class) {
            return Float.parseFloat(value);
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (fieldType == Character.class || fieldType == char.class) {
            if (value.length() > 0) return value.charAt(0);
            else return '\0';
        } else if (fieldType == LocalDateTime.class) {
            return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
        } else if (fieldType == LocalDate.class) {
            return LocalDate.parse(value, DATE_FORMATTER);
        } else if (fieldType == LocalTime.class) {
            return LocalTime.parse(value, TIME_FORMATTER);
        } else if (fieldType == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (fieldType == BigInteger.class) {
            return new BigInteger(value);
        } else if (fieldType == UUID.class) {
            return UUID.fromString(value);
        }

        // 其他类型默认返回原字符串
        return value;
    }

    /**
     * 将 Number 转换为 Java 字段类型
     */
    private static Object convertNumberToJavaType(Class<?> fieldType, Number value) {
        if (fieldType == Integer.class || fieldType == int.class) {
            return value.intValue();
        } else if (fieldType == Long.class || fieldType == long.class) {
            return value.longValue();
        } else if (fieldType == Double.class || fieldType == double.class) {
            return value.doubleValue();
        } else if (fieldType == Float.class || fieldType == float.class) {
            return value.floatValue();
        } else if (fieldType == Short.class || fieldType == short.class) {
            return value.shortValue();
        } else if (fieldType == Byte.class || fieldType == byte.class) {
            return value.byteValue();
        } else if (fieldType == BigDecimal.class) {
            return new BigDecimal(value.toString());
        }

        // 其他类型返回原值
        return value;
    }

    /**
     * 将 Timestamp 转换为 Java 字段类型
     */
    private static Object convertTimestampToJavaType(Class<?> fieldType, Timestamp timestamp) {
        if (fieldType == java.util.Date.class) {
            return new java.util.Date(timestamp.getTime());
        } else if (fieldType == LocalDateTime.class) {
            return timestamp.toLocalDateTime();
        } else if (fieldType == LocalDate.class) {
            return timestamp.toLocalDateTime().toLocalDate();
        } else if (fieldType == LocalTime.class) {
            return timestamp.toLocalDateTime().toLocalTime();
        } else if (fieldType == Date.class) {
            return new Date(timestamp.getTime());
        } else if (fieldType == Time.class) {
            return new Time(timestamp.getTime());
        }

        // 其他类型返回原值
        return timestamp;
    }

    /**
     * 将 java.sql.Date 转换为 Java 字段类型
     */
    private static Object convertSqlDateToJavaType(Class<?> fieldType, Date date) {
        if (fieldType == java.util.Date.class) {
            return new java.util.Date(date.getTime());
        } else if (fieldType == LocalDate.class) {
            return date.toLocalDate();
        } else if (fieldType == LocalDateTime.class) {
            return date.toLocalDate().atStartOfDay();
        }

        // 其他类型返回原值
        return date;
    }

    /**
     * 将 java.sql.Time 转换为 Java 字段类型
     */
    private static Object convertSqlTimeToJavaType(Class<?> fieldType, Time time) {
        if (fieldType == java.util.Date.class) {
            return new java.util.Date(time.getTime());
        } else if (fieldType == LocalTime.class) {
            return time.toLocalTime();
        } else if (fieldType == LocalDateTime.class) {
            return LocalDateTime.of(LocalDate.now(), time.toLocalTime());
        }

        // 其他类型返回原值
        return time;
    }

    /**
     * 将 java.util.Date 转换为 Java 字段类型
     */
    private static Object convertUtilDateToJavaType(Class<?> fieldType, java.util.Date date) {
        if (fieldType == Date.class) {
            return new Date(date.getTime());
        } else if (fieldType == LocalDateTime.class) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (fieldType == LocalDate.class) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (fieldType == LocalTime.class) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }

        // 其他类型返回原值
        return date;
    }

    /**
     * 将 BigDecimal 转换为 Java 字段类型
     */
    private static Object convertBigDecimalToJavaType(Class<?> fieldType, BigDecimal value) {
        if (fieldType == Integer.class || fieldType == int.class) {
            return value.intValue();
        } else if (fieldType == Long.class || fieldType == long.class) {
            return value.longValue();
        } else if (fieldType == Double.class || fieldType == double.class) {
            return value.doubleValue();
        } else if (fieldType == Float.class || fieldType == float.class) {
            return value.floatValue();
        } else if (fieldType == Short.class || fieldType == short.class) {
            return value.shortValue();
        } else if (fieldType == Byte.class || fieldType == byte.class) {
            return value.byteValue();
        }

        // 其他类型返回原值
        return value;
    }

    /**
     * 将 BigInteger 转换为 Java 字段类型
     */
    private static Object convertBigIntegerToJavaType(Class<?> fieldType, BigInteger value) {
        if (fieldType == Integer.class || fieldType == int.class) {
            return value.intValue();
        } else if (fieldType == Long.class || fieldType == long.class) {
            return value.longValue();
        } else if (fieldType == BigDecimal.class) {
            return new BigDecimal(value);
        }

        // 其他类型返回原值
        return value;
    }
}
