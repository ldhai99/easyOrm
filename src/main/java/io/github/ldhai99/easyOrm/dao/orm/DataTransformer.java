package io.github.ldhai99.easyOrm.dao.orm;

import io.github.ldhai99.easyOrm.dao.except.TypeConvertException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataTransformer {
    // region 配置参数
    private static ZoneId zoneId = ZoneId.systemDefault();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    // endregion

    // region 自定义转换器注册
    private static final Map<ClassPair, Converter<?, ?>> CUSTOM_CONVERTERS = new ConcurrentHashMap<>();

    public static <S, T> void registerConverter(Class<S> sourceType,
                                                Class<T> targetType,
                                                Converter<S, T> converter) {
        CUSTOM_CONVERTERS.put(new ClassPair(sourceType, targetType), converter);
    }
    // endregion

    // region 核心转换方法
    public static Object toJavaValue(Class<?> targetType, Object value) {
        if (value == null) return handleNull(targetType);

        try {
            // 1. 检查自定义转换器
            Object customConverted = tryCustomConvert(value, targetType);
            if (customConverted != null) return customConverted;

            // 2. 处理布尔类型
            if (isBooleanType(targetType)) {
                return convertToBoolean(value);
            }

            // 3. 按数据类型分发处理
            if (value instanceof Number) {
                return handleNumber(targetType, (Number) value);
            } else if (value instanceof String) {
                return handleString(targetType, (String) value);
            } else if (value instanceof Temporal) {
                return handleTemporal(targetType, (Temporal) value);
            } else if (value instanceof java.util.Date) {
                return handleUtilDate(targetType, (java.util.Date) value);
            } else if (value instanceof byte[]) {
                return handleByteArray(targetType, (byte[]) value);
            }

            // 4. 默认处理
            return handleDefault(targetType, value);
        } catch (Exception e) {
            throw new TypeConvertException(buildErrorMessage(targetType, value), e);
        }
    }
    // endregion

    // region 类型处理模块
    private static Object handleNull(Class<?> targetType) {
        if (targetType.isPrimitive()) {
            throw new TypeConvertException("Cannot assign null to primitive type: " + targetType);
        }
        return null;
    }

    private static boolean isBooleanType(Class<?> type) {
        return boolean.class.equals(type) || Boolean.class.equals(type);
    }

    private static Object convertToBoolean(Object value) {
        if (value instanceof Boolean) return value;

        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        } else if (value instanceof String) {
            String s = ((String) value).trim().toLowerCase(Locale.ROOT);
            if (s.isEmpty()) return false;

            if (s.equals("true") || s.equals("1") || s.equals("t") || s.equals("y") || s.equals("yes")) {
                return true;
            }
            if (s.equals("false") || s.equals("0") || s.equals("f") || s.equals("n") || s.equals("no")) {
                return false;
            }

            try {
                return Integer.parseInt(s) != 0;
            } catch (NumberFormatException e) {
                throw new TypeConvertException("Invalid boolean string: " + value);
            }
        }

        throw new TypeConvertException("Unsupported boolean conversion from: " + value.getClass().getName());
    }

    private static Object handleNumber(Class<?> targetType, Number number) {
        if (targetType.isAssignableFrom(number.getClass())) {
            return number;
        }

        if (BigDecimal.class.equals(targetType)) {
            return new BigDecimal(number.toString());
        } else if (BigInteger.class.equals(targetType)) {
            return BigInteger.valueOf(number.longValue());
        }

        return convertNumeric(targetType, number);
    }

    private static Object convertNumeric(Class<?> targetType, Number number) {
        if (targetType.isPrimitive()) {
            if (int.class.equals(targetType)) return number.intValue();
            if (long.class.equals(targetType)) return number.longValue();
            if (double.class.equals(targetType)) return number.doubleValue();
            if (float.class.equals(targetType)) return number.floatValue();
            if (short.class.equals(targetType)) return number.shortValue();
            if (byte.class.equals(targetType)) return number.byteValue();
            if (boolean.class.equals(targetType)) return number.intValue() != 0;
        } else {
            if (Integer.class.equals(targetType)) return number.intValue();
            if (Long.class.equals(targetType)) return number.longValue();
            if (Double.class.equals(targetType)) return number.doubleValue();
            if (Float.class.equals(targetType)) return number.floatValue();
            if (Short.class.equals(targetType)) return number.shortValue();
            if (Byte.class.equals(targetType)) return number.byteValue();
        }

        return number;
    }

    private static Object handleString(Class<?> targetType, String value) {
        if (String.class.equals(targetType)) return value;

        try {
            if (UUID.class.equals(targetType)) {
                return UUID.fromString(value);
            } else if (LocalDateTime.class.equals(targetType)) {
                return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
            } else if (LocalDate.class.equals(targetType)) {
                return LocalDate.parse(value, DATE_FORMATTER);
            } else if (LocalTime.class.equals(targetType)) {
                return LocalTime.parse(value, TIME_FORMATTER);
            } else if (targetType.isEnum()) {
                return Enum.valueOf((Class<Enum>) targetType, value);
            }

            return parseNumberString(targetType, value);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new TypeConvertException("String conversion failed: " + value, e);
        }
    }

    private static Object parseNumberString(Class<?> targetType, String value) {
        try {
            if (Integer.class.equals(targetType) || int.class.equals(targetType)) {
                return Integer.parseInt(value);
            } else if (Long.class.equals(targetType) || long.class.equals(targetType)) {
                return Long.parseLong(value);
            } else if (Double.class.equals(targetType) || double.class.equals(targetType)) {
                return Double.parseDouble(value);
            } else if (BigDecimal.class.equals(targetType)) {
                return new BigDecimal(value);
            }
        } catch (NumberFormatException e) {
            throw new TypeConvertException("Numeric conversion failed: " + value, e);
        }
        return value;
    }

    private static Object handleTemporal(Class<?> targetType, Temporal temporal) {
        if (temporal instanceof LocalDateTime) {
            LocalDateTime ldt = (LocalDateTime) temporal;
            if (LocalDateTime.class.equals(targetType)) return ldt;
            if (Timestamp.class.equals(targetType)) return Timestamp.valueOf(ldt);
        }
        return temporal;
    }

    private static Object handleUtilDate(Class<?> targetType, java.util.Date date) {
        Instant instant = date.toInstant();
        if (LocalDateTime.class.equals(targetType)) {
            return LocalDateTime.ofInstant(instant, zoneId);
        } else if (LocalDate.class.equals(targetType)) {
            return instant.atZone(zoneId).toLocalDate();
        }
        return date;
    }

    private static Object handleByteArray(Class<?> targetType, byte[] bytes) {
        if (UUID.class.equals(targetType) && bytes.length == 16) {
            ByteBuffer bb = ByteBuffer.wrap(bytes);
            return new UUID(bb.getLong(), bb.getLong());
        }
        return bytes;
    }

    private static Object handleDefault(Class<?> targetType, Object value) {
        if (targetType.isInstance(value)) {
            return value;
        }
        throw new TypeConvertException("Cannot convert "
                + value.getClass().getName() + " to " + targetType.getName());
    }
    // endregion

    // region 工具方法
    private static Object tryCustomConvert(Object value, Class<?> targetType) {
        Converter<?, ?> converter = CUSTOM_CONVERTERS.get(new ClassPair(value.getClass(), targetType));
        if (converter != null) {
            return ((Converter<Object, Object>) converter).convert(value);
        }
        return null;
    }

    private static String buildErrorMessage(Class<?> targetType, Object value) {
        return String.format("Convert [%s] to %s failed. Value: %s",
                value.getClass().getSimpleName(),
                targetType.getSimpleName(),
                redactSensitive(value));
    }

    private static String redactSensitive(Object value) {
        if (value instanceof CharSequence) return "<STRING>";
        if (value instanceof byte[]) return "<BINARY>";
        return value.toString();
    }
    // endregion

    // region 辅助类
    private static final class ClassPair {
        private final Class<?> source;
        private final Class<?> target;

        ClassPair(Class<?> source, Class<?> target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClassPair classPair = (ClassPair) o;
            return source.equals(classPair.source) && target.equals(classPair.target);
        }

        @Override
        public int hashCode() {
            int result = source.hashCode();
            result = 31 * result + target.hashCode();
            return result;
        }
    }

    public interface Converter<S, T> {
        T convert(S source) throws TypeConvertException;
    }
    // endregion

    // region 配置方法
    public static void setZoneId(ZoneId zone) {
        zoneId = zone;
    }

    public static ZoneId getZoneId() {
        return zoneId;
    }
    // endregion
}