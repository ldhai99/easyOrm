package com.github.ldhai99.easyOrm.Dialect;

public class OracleDialect {
    // oracle page sql
    public String createPageSqlFromOracle(int curpage, int pagesize,
                                          String fields, String tablename, String sid, String con,
                                          String orderby1) {
        String strsql;

        int startrow = (curpage - 1) * pagesize + 1;
        int endrow = (curpage) * pagesize;

        String where1 = "";
        if (con.trim().length() != 0) {
            where1 = " where " + "(" + con + ")";
        }

        // Oracle: SELECT * FROM (
        // SELECT MY_TABLE.*,ROWNUM AS MY_ROWNUM FROM (
        /** 括号里写实际的需要查询的SQL语句* */
        // ) AS MYTABLE WHERE ROWNUM <=200/**这里是一页中的最后一条记录**/
        // \) WHERE MY_ROWNUM>=10 /**这里是一页中的第一条记录**/
        // 正序列，列出从开始到本页的所有数据
        // 排序
        strsql = "(SELECT  " + sid + " FROM " + tablename + where1
                + " ORDER BY " + orderby1 + " )  ";
        // 取最大
        strsql = "(select " + sid + " ,rownum as rownumb from " + strsql + " a "
                + " where rownum<=" + endrow + ") ";

        // 列出最后一页内容ID
        strsql = "(SELECT  " + sid + "  FROM " + strsql + " b "
                + " where  rownumb>=" + startrow + ")";
        // 列出包含ID的所有记录
        strsql = "select " + fields + "  FROM " + tablename + " where " + sid
                + " in " + strsql+" ORDER BY " + orderby1 ;

        return strsql;
    }
    public String createPageSqlFromOracle2(int curpage, int pagesize,
                                           String fields, String tablename, String sid, String con,
                                           String orderby1) {
        String strsql;

        String where1 = "";
        if (con.trim().length() != 0) {
            where1 = " where " + "(" + con + ")";
        }

        // 列出包含ID的所有记录
        strsql = "select " + fields + "  FROM " + tablename + where1 + " and rownum<=10" ;

        return strsql;
    }
    public String createPageSqlFromOracle3(int curpage, int pagesize,
                                           String fields, String tablename, String sid, String con,
                                           String orderby1) {
        String strsql;

        int startrow = (curpage - 1) * pagesize + 1;
        int endrow = (curpage) * pagesize;

        String where1 = "";
        if (con.trim().length() != 0) {
            where1 = " where " + "(" + con + ")";
        }

        //取最大
        strsql = "(select " + fields + ",rownum as rownumb from " + tablename + " a "
                + where1 + " and rownum<=" + endrow + ") ";
//
//		// 列出最后一页内容ID
//		strsql = "(SELECT  " + sid + "  FROM " + strsql + " b "
//				+ " where  rownumb>=" + startrow + ")";
//
//		// 列出包含ID的所有记录
//		strsql = "select " + fields + "  FROM " + tablename + " where trim(" + sid
//		         + ") in " + strsql + " ORDER BY " + orderby1 ;

        strsql = "select "+ fields +" FROM " + strsql + " where rownumb>=" + startrow ;

        return strsql;
    }


}
