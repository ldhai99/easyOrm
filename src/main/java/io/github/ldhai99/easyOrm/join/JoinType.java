package io.github.ldhai99.easyOrm.join;


public enum JoinType {
    INNER("INNER JOIN"),
    LEFT("LEFT JOIN"),
    RIGHT("RIGHT JOIN"),
    FULL("FULL OUTER JOIN");

    private final String sql;

    JoinType(String sql) {
        this.sql = sql;
    }

}
