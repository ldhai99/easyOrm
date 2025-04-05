package io.github.ldhai99.easyOrm.base;

import java.util.ArrayList;
import java.util.StringTokenizer;
public class FrameConstant {

    // ====================== 动作类型常量 ======================
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_INSERT = "insert";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_PAGE = "page";
    public static final String ACTION_ROW_DATA = "row_data";

    // ====================== 请求参数常量 ======================
    public static final String PARAM_EVENT_NAME = "eventName";
    public static final String PARAM_OPERATION = "operation";
    public static final String PARAM_IS_SUBMIT = "is_submit";
    public static final String PARAM_READONLY = "readonly";

    // ====================== 用户会话常量 ======================
    public static final String SESSION_USER_ID = "session.userid";
    public static final String SESSION_LOGIN_PACK = "session.login_pack";

    // ====================== 页面跳转常量 ======================
    public static final String VIEW_CONTINUE = "ContinueView";
    public static final String VIEW_GO_SELF = "GoSelfView";
    public static final String VIEW_BACK_AND_REFRESH = "BackAndRefreshView";

    // ====================== 数据类型常量 ======================
    public static final String DATA_TYPE_VARCHAR = "Varchar";
    public static final String DATA_TYPE_DATETIME = "DateTime";
    public static final String DATA_TYPE_DATE = "Date";
    public static final String DATA_TYPE_MONEY4 = "Money4";
    public static final String DATA_TYPE_MONEY = "Money";
    public static final String DATA_TYPE_CHOICE = "Choice";

    // ====================== 字段显示类型常量 ==================
    public static final String DISPLAY_TYPE_TEXT = "Text";
    public static final String DISPLAY_TYPE_CHECKBOX = "CheckBox";
    public static final String DISPLAY_TYPE_IMAGE = "Image";
    public static final String DISPLAY_TYPE_BUTTON = "Button";
    public static final String DISPLAY_TYPE_HYPERLINK = "HyperLink";

    // ====================== 界面提示信息常量 ==================
    public static final String CMD_FIELD = "命令";
    public static final String TABLE_CLASS = "bian_list"; // 表格样式类名
    public static final String ASK_CONFIRM_UPDATE = "确定要修改吗？";
    public static final String ASK_CONFIRM_DELETE = "确定要删除吗？";
    public static final String ASK_CONFIRM_INSERT = "确定要增加吗？";
    public static final String ASK_CONFIRM_VIEW = ""; // 查看无提示
    public static final String TITLE_UPDATE = "修改";
    public static final String TITLE_DELETE = "删除";
    public static final String TITLE_INSERT = "增加";
    public static final String TITLE_VIEW = "查看";

    // ====================== 字段描述相关常量 ==================
    public static final String FIELD_NAME = "FieldName"; // 字段名称
    public static final String FIELD_DESCRIPTION = "FieldDesc"; // 字段描述
    public static final String FIELD_DATA_TYPE = "DataType"; // 数据类型
    public static final String FIELD_IS_NULLABLE = "IsNull"; // 是否为空
    public static final String FIELD_IS_DISPLAYABLE = "IsDisplay"; // 是否显示
    public static final String FIELD_DISPLAY_TYPE = "DisplayType"; // 显示类型
    public static final String FIELD_URL = "Url"; // 导航 URL
    public static final String FIELD_URL_ID = "UrlId"; // URL 参数 ID
    public static final String FIELD_URL_ID_VALUE = "UrlIdValue"; // URL 参数值
    public static final String FIELD_ASK_INFO = "AskInfo"; // 操作询问信息
    public static final String FIELD_TARGET = "_BLANK"; // 目标视图

}
