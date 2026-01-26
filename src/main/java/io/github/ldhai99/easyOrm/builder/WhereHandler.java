package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.context.sql.constants.SqlKeywords;

import io.github.ldhai99.easyOrm.context.validation.EmptyChecker;
import io.github.ldhai99.easyOrm.dao.core.TableNameResolver;
import io.github.ldhai99.easyOrm.dialect.LikeType;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.ldhai99.easyOrm.context.sql.constants.SqlKeywords.*;

public abstract class WhereHandler<T extends WhereHandler<T>> extends SetHandler<T> {
//0、通用查询-----------age=18----------------------------------------------------

    //----------多表连接------------------------------
    public T join(String join) {
        this.builder.join(join);
        return self();
    }
    //inner join---

    public T join(String table, String on) {
        this.join(INNER_JOIN, table, on);
        return self();
    }

    public T join(Class clazz, String on) {
        return join(TableNameResolver.getTableName(clazz), on);
    }

    public T join(String table, BaseSQL subSql) {
        return this.join( table, jdbcModel.processSqlName(subSql));

    }
    public T join(Class clazz, BaseSQL subSql) {

        return self().join(TableNameResolver.getTableName(clazz),subSql);
    }
    //leftJoin---
    public T leftJoin(String table, String on) {
        this.join(LEFT_JOIN, table, on);
        return self();
    }

    public T leftJoin(Class<?> clazz, String on) {
        return leftJoin(TableNameResolver.getTableName(clazz), on);
    }


    public T leftJoin(String table, BaseSQL subSql) {
        return this.leftJoin( table, jdbcModel.processSqlName(subSql));

    }
    public T leftJoin(Class clazz, BaseSQL subSql) {

        return self().leftJoin(TableNameResolver.getTableName(clazz),subSql);
    }

    //rightJoin---
    public T rightJoin(String table, String on) {
        this.join(RIGHT_JOIN, table, on);
        return self();
    }

    public T rightJoin(Class<?> clazz, String on) {
        return rightJoin(TableNameResolver.getTableName(clazz), on);
    }


    public T rightJoin(String table, BaseSQL subSql) {
        return this.rightJoin( table, jdbcModel.processSqlName(subSql));

    }
    public T rightJoin(Class clazz, BaseSQL subSql) {

        return self().rightJoin(TableNameResolver.getTableName(clazz),subSql);
    }
    //fullJoin---
    public T fullJoin(String table, String on) {
        this.join(FULL_JOIN, table, on);
        return self();
    }

    public T fullJoin(Class<?> clazz, String on) {
        return fullJoin(TableNameResolver.getTableName(clazz), on);
    }



    public T fullJoin(String table, BaseSQL subSql) {
        return this.fullJoin( table, jdbcModel.processSqlName(subSql));

    }
    public T fullJoin(Class clazz, BaseSQL subSql) {

        return self().fullJoin(TableNameResolver.getTableName(clazz),subSql);
    }
//------
    public T join(String join, String table, String on) {
        if (on != null && on.trim().length() != 0) {
            this.builder.join(join + SPACE + table + SPACE + ON + SPACE + on);
        } else
            this.builder.join(join + SPACE + table);
        return self();
    }

    //一、比较谓词----------------------------------------------------
    public T func(Object name, Object value) {
        return nameOperatorValue(name, EQ, value);
    }

    //一、比较谓词----------------------------------------------------
    public T where(Object name, Object value) {
        return nameOperatorValue(name, EQ, value);
    }

    public <E> T where(PropertyGetter<E> getter, Object value) {

        return where(resolveColumn(getter), value);
    }

    public T eqMap(Map<String, Object> columnMap) {
        if(EmptyChecker.isEmpty(columnMap))
            return self();
        for (String key : columnMap.keySet()) {
            eq(key, columnMap.get(key));
        }
        return self();
    }

    public T eq(Object name, Object value) {
        return nameOperatorValue(name, EQ, value);
    }

    // 新增支持Lambda的eq方法
// 支持泛型的 eq 方法<E> String field(PropertyGetter<E> getter)
    public <E> T eq(PropertyGetter<E> getter, Object value) {
        return eq(resolveColumn(getter), value);
    }

    public T eqIfNotNull(Object name, Object value) {
        if(value==null)
            return self();
        return nameOperatorValue(name, EQ, value);
    }

    public <E> T eqIfNotNull(PropertyGetter<E> getter, Object value) {
        return eqIfNotNull(resolveColumn(getter), value);
    }
    public T eqIfNotEmpty(Object name, Object value) {
        if(EmptyChecker.isEmpty(value))
            return self();
        return nameOperatorValue(name,  EQ, value);
    }

    public <E> T eqIfNotEmpty(PropertyGetter<E> getter, Object value) {
        return eqIfNotEmpty(resolveColumn(getter), value);
    }

    public T neq(Object name, Object value) {
        return nameOperatorValue(name, NEQ, value);
    }

    public <E> T neq(PropertyGetter<E> getter, Object value) {
        return neq(resolveColumn(getter), value);
    }

    public T gt(Object name, Object value) {
        return nameOperatorValue(name, GT, value);
    }

    public <E> T gt(PropertyGetter<E> getter, Object value) {
        return gt(resolveColumn(getter), value);
    }

    public T gte(Object name, Object value) {
        return nameOperatorValue(name, GTE, value);
    }

    public <E> T gte(PropertyGetter<E> getter, Object value) {
        return gte(resolveColumn(getter), value);
    }

    public T lt(Object name, Object value) {
        return nameOperatorValue(name, LT, value);
    }

    public <E> T lt(PropertyGetter<E> getter, Object value) {
        return lt(resolveColumn(getter), value);
    }

    public T lte(Object name, Object value) {
        return nameOperatorValue(name, LTE, value);
    }

    public <E> T lte(PropertyGetter<E> getter, Object value) {
        return lte(resolveColumn(getter), value);
    }

    //name+operator+value
    protected T nameOperatorValue(Object name, String operator, Object value) {
        if(value==null)
            return self();

        this.where(String.format(" %s %s %s ", jdbcModel.processSqlName(name), operator, jdbcModel.processSqlValue(value)));
        return self();
    }

    //二、LIKE 谓词——字符串的部分一致查询
    //-----like------
    public T like(Object name, Object value) {
        return likeOperator(name, LIKE, value);
    }
    public T contains(Object name, Object value) {
        return like(name, value);

    }
    public T notLike(Object name, Object value) {
        return likeOperator(name, NOT_LIKE, value);
    }
    public T notContains(Object name, Object value) {
        return notLike(name, value);

    }
    public <E> T like(PropertyGetter<E> getter, Object value) {
        return like(resolveColumn(getter), value);
    }
    public <E> T contains(PropertyGetter<E> getter, Object value) {
        return like(getter, value);

    }
    public <E> T notLike(PropertyGetter<E> getter, Object value) {
        return notLike(resolveColumn(getter), value);
    }
    public <E> T notContains(PropertyGetter<E> getter, Object value) {
        return notLike(getter, value);

    }
    //-----like_------
    public T like_(Object name, Object value) {
        return likeOperator(name, LIKE_, value);
    }

    public <E> T like_(PropertyGetter<E> getter, Object value) {
        return like_(resolveColumn(getter), value);
    }

    public T notLike_(Object name, Object value) {
        return likeOperator(name, NOT_LIKE_, value);
    }

    public <E> T notLike_(PropertyGetter<E> getter, Object value) {
        return notLike_(resolveColumn(getter), value);
    }

    //-----likeLeft------
    public T likeLeft(Object name, Object value) {
        return likeOperator(name, LIKE_LEFT, value);
    }
    public T endsWith(Object name, Object value) {
        return likeLeft(name, value);
    }
    public T notLikeLeft(Object name, Object value) {
        return likeOperator(name, NOT_LIKE_LEFT, value);
    }
    public T notEndsWith(Object name, Object value) {
        return notLikeLeft(name, value);
    }

    public <E> T likeLeft(PropertyGetter<E> getter, Object value) {
        return likeLeft(resolveColumn(getter), value);
    }
    public <E> T endsWith(PropertyGetter<E> getter, Object value) {
        return  likeLeft(getter, value);
    }

    public <E> T notLikeLeft(PropertyGetter<E> getter, Object value) {
        return notLikeLeft(resolveColumn(getter), value);
    }
    public <E> T notEndsWith(PropertyGetter<E> getter, Object value) {
        return  notLikeLeft(getter, value);
    }

    //-----likeRight------
    public T likeRight(Object name, Object value) {
        return likeOperator(name, LIKE_RIGHT, value);
    }
    public T startsWith(Object name, Object value) {
        return likeRight(name, value);
    }

    public T notLikeRight(Object name, Object value) {
        return likeOperator(name, NOT_LIKE_RIGHT, value);
    }
    public T notStartsWith(Object name, Object value) {
        return notLikeRight(name, value);
    }

    public <E> T likeRight(PropertyGetter<E> getter, Object value) {
        return likeRight(resolveColumn(getter), value);
    }
    public <E> T startsWith(PropertyGetter<E> getter, Object value) {
        return likeRight(getter, value);

    }

    public <E> T notLikeRight(PropertyGetter<E> getter, Object value) {
        return notLikeRight(resolveColumn(getter), value);
    }
    public <E> T notStartsWith(PropertyGetter<E> getter, Object value) {
        return notLikeRight(getter, value);

    }

    //----------------基础-------------------------

    protected T likeOperator(Object name, String operator, Object value) {

        Object newValue = value;
        boolean isLikeOperation = false;
        //对值预处理
        if (!(value instanceof BaseSQL)) {
            String oldValue = value.toString(); // 更安全：避免强制 (String)
            LikeType likeType = LikeType.CONTAINS; // 默认

            // 判断操作符类型并设置 LikeType
            if (isLikeOperator(operator, LIKE, NOT_LIKE)) {
                likeType = LikeType.CONTAINS;
                isLikeOperation = true;
            } else if (isLikeOperator(operator, LIKE_RIGHT, NOT_LIKE_RIGHT)) {
                likeType = LikeType.STARTS_WITH;
                isLikeOperation = true;
            } else if (isLikeOperator(operator, LIKE_LEFT, NOT_LIKE_LEFT)) {
                likeType = LikeType.ENDS_WITH;
                isLikeOperation = true;
            } else if (isLikeOperator(operator, LIKE_, NOT_LIKE_)) {
                likeType = LikeType.CUSTOM;
                isLikeOperation = true;
            }

            if (isLikeOperation) {
                newValue = jdbcModel.processLikeValue(oldValue, likeType);
            } else {
                newValue = oldValue; // 非 LIKE 操作，直接使用原值
            }

        }
        String namePlaceholder = jdbcModel.processSqlName(name);
        String valuePlaceholder = jdbcModel.processSqlValue(newValue);

        // 构建 LIKE / NOT LIKE 条件
        StringBuilder sqlCondition = new StringBuilder();
        if (operator.startsWith(NOT_LIKE)) {
            sqlCondition.append(namePlaceholder).append(SPACE).append(NOT_LIKE).append(SPACE).append(valuePlaceholder);
        } else {
            sqlCondition.append(namePlaceholder).append(SPACE).append(LIKE).append(SPACE).append(valuePlaceholder);
        }

        // ✅ 关键：如果是 LIKE 操作，追加 ESCAPE 子句
        if (isLikeOperation && !(value instanceof BaseSQL)) {
            String escapeClause = jdbcModel.getDialect().getLikeEscapeClause();
            if (escapeClause != null && !escapeClause.isEmpty()) {
                sqlCondition.append(escapeClause);
            }
        }
        this.where(sqlCondition.toString());

        return self();
    }
    // 辅助方法：判断 operator 是否匹配任一目标
    private boolean isLikeOperator(String operator, String... targets) {
        if (operator == null) return false;
        String opLower = operator.toLowerCase();
        for (String target : targets) {
            if (opLower.equals(target.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    //三、BETWEEN 谓词——范围查询
    public T between(Object name, Object value1, Object value2) {
        this.between(name, BETWEEN, value1, value2);

        return self();
    }

    public <E> T between(PropertyGetter<E> getter, Object value1, Object value2) {
        return between(resolveColumn(getter), value1, value2);
    }

    public T notBetween(Object name, Object value1, Object value2) {

        this.between(name, NOT_BETWEEN, value1, value2);

        return self();
    }

    public <E> T notBetween(PropertyGetter<E> getter, Object value1, Object value2) {
        return notBetween(resolveColumn(getter), value1, value2);
    }

    protected T between(Object name, String between, Object value1, Object value2) {
        String placehoder1 = jdbcModel.processSqlValue(value1);
        String placehoder2 = jdbcModel.processSqlValue(value2);
        String namePlacehoder = jdbcModel.processSqlName(name);
        this.where(namePlacehoder +SPACE+ between +SPACE+ placehoder1 +SPACE+ AND +SPACE+ placehoder2);

        return self();
    }

    //四、IS NULL、IS NOT NULL——判断是否为 NULL
    //-------4.1 IS NULL-------------------------------
    public T isNullElseEq(Object name, Object value) {
        if(value==null)
            return isNull(name);
        else
            return eq(name, value);
    }
    public <E> T isNullElseEq(PropertyGetter<E> getter, Object value) {
        return isNullElseEq(resolveColumn(getter),value);
    }
    public T isNull(Object name,Object value) {
        if(value==null)
            return this.isNull(name);
        else
            return self();
    }
    public <E> T isNull(PropertyGetter<E> getter,Object value) {
        return isNull(resolveColumn(getter),value);
    }
    public T isNull(Object name) {

        this.isNull(name, IS_NULL);
        return self();
    }

    public <E> T isNull(PropertyGetter<E> getter) {
        return isNull(resolveColumn(getter));
    }
    //-------4.2 IS not NULL----------------------------
    public T isNotNull(Object name,Object value) {
        if(value!=null)
            return this.isNotNull(name);
        else
            return self();
    }
    public <E> T isNotNull(PropertyGetter<E> getter,Object value) {
        return isNotNull(resolveColumn(getter),value);
    }
    public T isNotNull(Object name) {

        this.isNull(name, IS_NOT_NULL);
        return self();
    }

    public <E> T isNotNull(PropertyGetter<E> getter) {
        return isNotNull(resolveColumn(getter));
    }
    //-------4.0 IS  NULL
    protected T isNull(Object name, String isNull) {
        String namePlacehoder = jdbcModel.processSqlName(name);
        this.where(namePlacehoder + SPACE + isNull);
        return self();
    }
    //-------4.3 IS  empty
    public T isEmptyElseEq(Object name, Object value) {
        if(EmptyChecker.isEmpty(value))
            return isEmpty(name);
        else
            return eq(name, value);
    }
    public <E> T isEmptyElseEq(PropertyGetter<E> getter, Object value) {
        return isEmptyElseEq(resolveColumn(getter),value);
    }
    public T isEmpty(Object name, Object value) {
        if(EmptyChecker.isEmpty(value))
            return isEmpty(name);
        else
            return self();
    }
    public <E> T isEmpty(PropertyGetter<E> getter, Object value) {
        return isEmpty(resolveColumn(getter),value);
    }
    public <E> T isEmpty(PropertyGetter<E> getter) {
        return isEmpty(resolveColumn(getter));
    }
    public T isEmpty(Object name) {
        this.begin().eq(name, "").or().isNull(name).end();
        return self();
    }

    //-------4.3 IS  not empty
    public T isNotEmpty(Object name, Object value   ) {
        if(EmptyChecker.notEmpty(value))
            return isNotEmpty(name);
        else
            return self();
    }
    public <E> T isNotEmpty(PropertyGetter<E> getter, Object value) {
        return isNotEmpty(resolveColumn(getter),  value);
    }

    //不为空，不为null，并且不为''
    public T isNotEmpty(Object name) {

        this.neq(name, "").isNotNull(name);
        return self();
    }

    public <E> T isNotEmpty(PropertyGetter<E> getter) {
        return isNotEmpty(resolveColumn(getter));
    }


    //五、IN 谓词——OR 的简便用法
    public T or() {

        this.where(SPACE+OR+SPACE);
        return self();
    }
    public T and() {

        this.where(SPACE+AND+SPACE);
        return self();
    }
    //in() / notIn()------------------------------------------------
    //支持列表、数组、子查询：
    public T in(Object name, List<?> values) {
        return nameOperatorValue(name, IN, values);

    }

    public <E> T in(PropertyGetter<E> getter, List<?> values) {
        return in(resolveColumn(getter), values);
    }

    public T in(Object name, Object... values) {

        return nameOperatorValue(name, IN, new ArrayList<>(Arrays.asList(values)));
    }

    public <E> T in(PropertyGetter<E> getter, Object... values) {
        return in(resolveColumn(getter), values);
    }

    public T in(Object name, BaseSQL subSql) {

        return nameOperatorValue(name, IN, subSql);
    }

    public <E> T in(PropertyGetter<E> getter, BaseSQL subSql) {
        return in(resolveColumn(getter), subSql);
    }

    public T notIn(Object name, List<?> values) {
        return nameOperatorValue(name, NOT_IN, values);
    }

    public <E> T notIn(PropertyGetter<E> getter, List<?> values) {
        return notIn(resolveColumn(getter), values);
    }

    public T notIn(Object name, BaseSQL subSql) {

        return nameOperatorValue(name, NOT_IN, subSql);
    }

    public <E> T notIn(PropertyGetter<E> getter, BaseSQL subSql) {
        return notIn(resolveColumn(getter), subSql);
    }

    public T notIn(Object name, Object... values) {
        return nameOperatorValue(name, NOT_IN, new ArrayList<>(Arrays.asList(values)));

    }

    public <E> T notIn(PropertyGetter<E> getter, Object... values) {
        return notIn(resolveColumn(getter), values);
    }

    //---------------------------------------
    public T in(Object name, Object values) {
        if(EmptyChecker.isEmpty( values))
            return self();

        // 新增字符串处理逻辑
        if (values instanceof String) {
            String str = ((String) values).trim();
            if (str.isEmpty()) return self();

            // 处理逗号分隔的字符串
            if (str.contains(",")) {
                String[] parts = str.split(",");
                List<String> list = new ArrayList<>();
                for (String part : parts) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) {
                        list.add(trimmed);
                    }
                }
                return in(name, list);
            }
            // 单个值直接处理
            else {
                return eq(name, str);
            }
        }
        // 原有逻辑保持不变
        else if (values instanceof List) {
            return in(name, (List<?>) values);
        }
        else if (values instanceof BaseSQL) {
            return in(name, (BaseSQL) values);
        }
        // 处理数组类型
        else if (values != null && values.getClass().isArray()) {
            return in(name, Arrays.asList((Object[]) values));
        }
        // 单个值直接处理
        else if (values != null) {
            return eq(name, values);
        }
        return self();
    }

    public <E> T in(PropertyGetter<E> getter, Object values) {
        return in(resolveColumn(getter), values);
    }

    public T notIn(Object name, Object values) {
        // 新增字符串处理逻辑
        if (values instanceof String) {
            String str = ((String) values).trim();
            if (str.isEmpty()) return self();

            if (str.contains(",")) {
                String[] parts = str.split(",");
                List<String> list = new ArrayList<>();
                for (String part : parts) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) {
                        list.add(trimmed);
                    }
                }
                return notIn(name, list);
            }
            // 单个值直接处理
            else {
                return neq(name, str);
            }
        }
        // 原有逻辑保持不变
        else if (values instanceof List) {
            return notIn(name, (List<?>) values);
        }
        else if (values instanceof BaseSQL) {
            return notIn(name, (BaseSQL) values);
        }
        // 处理数组类型
        else if (values != null && values.getClass().isArray()) {
            return notIn(name, Arrays.asList((Object[]) values));
        }
        // 单个值直接处理
        else if (values != null) {
            return neq(name, values);
        }
        return self();
    }

    public <E> T notIn(PropertyGetter<E> getter, Object values) {
        return notIn(resolveColumn(getter), values);
    }
    //七、EXIST 谓词
    //
    public T exists(BaseSQL subSQL) {
        return operatorValue(EXISTS, subSQL);
    }

    public T notExists(BaseSQL subSQL) {
        return operatorValue(NOT_EXISTS, subSQL);
    }

    //operator+value------exists（value）-------
    protected T operatorValue(String operator, BaseSQL subSQL) {
        this.where(String.format("  %s %s ", operator, jdbcModel.processSqlValue(subSQL)));
        return self();
    }

    //八、括号
    public T begin() {

        this.where(" ( ");
        return self();
    }

    public T end() {

        this.where(" ) ");
        return self();
    }

    // 通用条件方法
    public T when(boolean apply, Consumer<T> conditionBuilder) {
        if (apply) {
            conditionBuilder.accept(self());
        }
        return self();
    }
    public ConditionalBuilder<T> when(boolean condition) {
        return new ConditionalBuilder<>(self(), condition);
    }
    // 增强1：支持 Lambda 条件表达式
    public T when(BooleanSupplier condition, Consumer<T> action) {
        return when(condition.getAsBoolean(), action);
    }
    // 增强3：链式构建器支持 Lambda 条件
    public ConditionalBuilder<T> when(BooleanSupplier condition) {
        return new ConditionalBuilder<>(self(), condition.getAsBoolean());
    }
    // 增强2：支持带上下文的 Lambda 条件
    /**
     * 增强的上下文感知条件方法
     *
     * @param condition 条件函数
     * @param context 上下文对象
     * @param action 条件成立时执行的操作
     * @return 当前构建器
     */
    public <C> T when(Function<C, Boolean> condition, C context, Consumer<T> action) {
        return when(condition.apply(context), action);
    }
    // 增强4：支持上下文，链式构建器支持 Lambda 条件
    /**
     * 上下文感知的条件构建器入口
     *
     * @param condition 条件函数，接受上下文参数
     * @param context 上下文对象
     * @return 条件构建器
     */
    public <C> ConditionalBuilder<T> when(Function<C, Boolean> condition, C context) {
        return new ConditionalBuilder<>(self(), condition.apply(context));
    }

}
