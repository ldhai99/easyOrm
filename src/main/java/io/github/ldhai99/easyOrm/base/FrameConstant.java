package io.github.ldhai99.easyOrm.base;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class FrameConstant {
    public static String UPDATE = "update", INSERT = "insert", DELETE = "delete", VIEW = "view", PAGE = "page",ROW_DATA="row_data";
    public static String MYACTION = "myaction";

    public static String Event_NAME = "eventName";
    public static String OPERATION = "operation";
    public static String IS_SUBMIT = "is_submit";
    public static String READONLY = "readonly";

    public static String USERID = "session.userid";
    public static String LOGIN_PACK = "session.lgoin_pack";
    // public static String RES_OPTION="";

    // 定义页面跳转常量view
    public static String ContinueView = "ContinueView";
    public static String GoSelfView = "GoSelfView";
    public static String BackAndRefreshView = "BackAndRefreshView";


    // 常量
    // DataType--数据类型(varchar,DateTime,Date,Money4,Money)
    public static String VARCHAR = "Varchar";
    public static String DATETIME = "DateTime";
    public static String DATE = "Date";
    public static String MONEY4 = "Money4";
    public static String MONEY = "Money";
    public static String CHOICE = "choice";
    // DisplayType--字段显示类型(Text,CheckBox,Image,Button,HyperLink)
    public static String TEXT = "Text";
    public static String CHECKBOX = "CheckBox";
    public static String IMAGE = "Image";
    public static String BUTTON = "Button";
    public static String HYPERLINK = "HyperLink";

    // guid 界面需要的信息-----------------------------------------------------------
    public static String cmd_field = "命令";
    public static String table_class = "bian_list";
    // 表格的样式
    public static String ask_update = "确定要修改吗?", ask_delete = "确定要删除吗?", ask_insert = "确定要增加吗?", ask_view = "";
    public static String update_title = "修改", delete_title = "删除", insert_title = "增 加", view_title = "查看";

    // ------描述key-----------------

    // FieldName--字段名称
    public static String FIELD_NAME = "FieldName";
    // FieldDesc--字段描述
    public static String FIELD_DESC = "FieldDesc";
    // DataType--数据类型(varchar,DateTime,Date,Money4,Money)
    public static String DATA_TYPE = "DataType";
    // IsNull--是否为空
    public static String IS_NULL = "IsNull";
    // IsDisplay--是否显示
    public static String IS_DISPLAY = "IsDisplay";
    // DisplayType--字段显示类型(Text,CheckBox,Image,Button,HyperLink)
    public static String DISPLAY_TYPE = "DisplayType";
    // Url---导航Url
    public static String URL = "Url";
    // UrlId 传参id，
    public static String URL_ID = "UrlId";
    // UrlIdValue
    public static String URL_ID_VALUE = "UrlIdValue";
    // AskInfo--操作询问信息
    public static String ASK_INFO = "AskInfo";

    public static String TARGET = "_BLANK";


    public static boolean IsNullOrEmpty(String a_value) {
        if (a_value != null && a_value.trim().length() != 0)
            return false;
        else
            return true;
    }

    public static String lastPart(String in) {
        int index = in.lastIndexOf(".");
        if (index == -1) {
            return in;
        } else
            return in.substring(index + 1);
    }

    public static String beforePart(String in) {
        int index = in.lastIndexOf(".");
        if (index == -1) {
            return "";
        } else
            return in.substring(0, index);
    }

    public static int StrToInt(String str) {

        try {
            int a = Integer.parseInt(str);
            return a;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static ArrayList split(String in_str) {
        ArrayList temp = new ArrayList();
        String str;
        StringTokenizer b = new StringTokenizer(in_str, "|");
        while (b.hasMoreTokens()) {
            str = b.nextToken();
            // System.out.println(str);
            temp.add(str);
        }
        return temp;
    }

    public static void main(String[] args) {
        System.out.println(lastPart("药品电子监管系统.a"));
    }
}
