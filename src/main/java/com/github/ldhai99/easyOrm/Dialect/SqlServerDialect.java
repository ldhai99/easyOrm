package com.github.ldhai99.easyOrm.Dialect;

public class SqlServerDialect {

    public String createPageSqlFromMSSQL(int curpage, int pagesize,
                                         String fields, String tablename, String sid, String con,
                                         String orderby1) {
        String strsql;

        int endrow = (curpage) * pagesize;

        String where1 = "";
        if (con.trim().length() != 0) {
            where1 = " where " + "(" + con + ")";
        }

        String orderfield = orderby1;
        String orderby2 = orderby1;

        //获得排序字段
        orderfield = orderfield.replaceAll(" asc", "");
        orderfield = orderfield.replaceAll(" desc", "");
        orderfield = orderfield.replaceAll(" ASC", "");
        orderfield = orderfield.replaceAll(" DESC", "");

        //获得反序
        orderby2 = orderby2.replaceAll(" asc", " asc_p");
        orderby2 = orderby2.replaceAll(" desc", " desc_p");
        orderby2 = orderby2.replaceAll(" ASC", " asc_p");
        orderby2 = orderby2.replaceAll(" DESC", " desc_p");

        orderby2 = orderby2.replaceAll(" asc_p", " desc");
        orderby2 = orderby2.replaceAll(" desc_p", " asc");

        if (orderfield.indexOf(sid) == -1) {
            orderfield = orderfield + "," + sid;
            orderby1 = orderby1 + "," + sid + " asc";
            orderby2 = orderby2 + "," + sid + " desc";
        }

        //
        // 样例-完美的分页SQL-李冬海创造
        // select * from wprkmx where id in
        // (
        // select top 5 id from
        // (SELECT top 16001 id,mxxh from wprkmx order by mxxh asc ,id asc) a
        // order by mxxh desc,id desc
        // )
        // order by mxxh asc,id asc

        // 正序列，列出从开始到本页的所有数据
        strsql = "(SELECT TOP " + endrow + " " + orderfield + " FROM "
                + tablename + where1 + " ORDER BY " + orderby1 + " ) a  ";
        // 倒序列，列出最后一页内容ID
        strsql = "(SELECT TOP " + pagesize + "  " + sid + "  FROM " + strsql
                + " order by " + orderby2 + ")";
        // 列出包含ID的所有记录
        strsql = "select " + fields + "  FROM " + tablename + " where " + sid
                + " in " + strsql + " order by " + orderby1;

        return strsql;
    }
}
