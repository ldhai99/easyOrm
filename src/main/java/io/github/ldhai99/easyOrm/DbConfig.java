package io.github.ldhai99.easyOrm;


import io.github.ldhai99.easyOrm.Dialect.Dialect;
import io.github.ldhai99.easyOrm.Dialect.MysqlDialect;

public class DbConfig {

    private static String database="mysql";
    private static Dialect dialect;

    /**
     * 测试
     * @param args
     */
    public static  void main(String[] args){
        String c="userId";
        System.out.println("驼峰命名转下划线命名"+camel2under(c));
        String s="user_id";
        System.out.println("下划线命名转驼峰命名"+under2camel(s));
    }

//    public static void main(String[] args) {
//        String content = " 012304560 ";
//        char matchContent = '0';
//        System.out.println(content.length());
//        System.out.println(trim(content).length());
//        System.out.println(leftTrim(content).length());
//        System.out.println(rightTrim(content).length());
//    }




    public static  String getDatabase() {

        return database;
    }
    //    public static String getUuid() {
    //      return UUID.randomUUID().toString().replace("-", "" );
    // }

    public static Dialect getDialect() {

        if(dialect==null){
            if(database.equalsIgnoreCase("mysql")) {
                return  new MysqlDialect();
            }
        }

        return dialect;
    }

    public static  void setDatabase(String database) {
        database = database;
    }
    /**
     * 功能：驼峰命名转下划线命名
     * 小写和大写紧挨一起的地方,加上分隔符,然后全部转小写
     */
    public static String camel2under(String c)
    {
        String separator = "_";
        c = c.replaceAll("([a-z])([A-Z])", "$1"+separator+"$2").toLowerCase();
        return c;
    }
    /**
     * 功能：下划线命名转驼峰命名
     * 将下划线替换为空格,将字符串根据空格分割成数组,再将每个单词首字母大写
     * @param s
     * @return
     */
    private static String under2camel(String s)
    {
        String separator = "_";
        String under="";
        s = s.toLowerCase().replace(separator, " ");
        String sarr[]=s.split(" ");
        for(int i=0;i<sarr.length;i++)
        {
            String w=sarr[i].substring(0,1).toUpperCase()+sarr[i].substring(1);
            under +=w;
        }
        return under;
    }



    public static String trim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+|[　 ]+$","");
        }
    }
    /*去左空格*/
    public static String leftTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+", "");
        }
    }
    /*去右空格*/
    public static String rightTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("[　 ]+$", "");
        }
    }

}
