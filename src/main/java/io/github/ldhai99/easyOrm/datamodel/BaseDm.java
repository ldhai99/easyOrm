package io.github.ldhai99.easyOrm.datamodel;

import io.github.ldhai99.easyOrm.base.FrameConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDm {
    public String title;// 项目窗口标题
    public String model_table;// 数据模型对应的表名（项目名）

    // 表字段信息-------------------------------------------------------
    public String table_id = "id";// ID字段
    public String table_p_id = "p_id";// 父ID字段

    public String table_disp_field;// 显示名字段
    public String table_url_field = "";// 地址字段
    public String table_img_field = "";
    public String table_find_fields;// 查询需要的字段

    // 更新需要的信息--------------------------------------------------
    public String update_table;// 更新表名
    public String update_fields = "*";//更新的列
    public String insert_fields = "*";//增加的列
    public int where_para_count = 1;// 更新where条件参数个数

    // 查询需要的信息(一般是页面查询，分页)---------------------------------------
    public String select_sql;
    public String select_table;// selecet 需要的主表
    public String select_fields = "*";// select 需要列出的内容,缺省全部
    public String select_where = "";// 查询条件，缺省为空
    public String select_orderby = "";// 排序条件
    public String select_groupby = "";
    //上下应用的数据模型-----------------------------
    //上一个应用的数据模型
    public BaseDm previous_dm;
    // 下一个应用的数据模型
    public BaseDm next_dm;


    // 页面传递数值-----------------------------
    public String id_value;// 主键键值
    public String p_id_value;// 父主键值
    public String readonly = "";// 只读选项
    public String eventName = "";// 用户传递URL选项
    public String operation;// 用户传递URL操作选项
    // 公用数据
  //  public List<DbParameter> pmt = new ArrayList<DbParameter>();// 用于存储更新参数的集合
    public String info;// 信息字段


    // 数据存储变量------------------------------------------------------------
    public List<Map<String, Object>> dt ;// 新框架用List<Map>数据格式datatable，从数据读取记录存dt
    public Map<String, Object> dr,old_dr;// 数据集合--单记录datarow，从数据读取记录存dr,old_dr修改，删除时候保留旧记录
    public List lst_data;// 数据集合--多记录

    public HashMap<String, Object> data = new HashMap<String, Object>();// 数据记录集合,附件数据
    public HashMap<String, Object> ctl = new HashMap<String, Object>();// 控件记录集合
    //---页面跳转配置----------
    public String viewBase= FrameConstant.GoSelfView;

    // grid需要的项目--------hashmap方式存储-------------------
    public ArrayList<HashMap> lst_data_column = new ArrayList<HashMap>();
    public ArrayList<HashMap> lst_cmd_column = new ArrayList();
    public HashMap map_insert_cmd = new HashMap();
    public HashMap map_other_cmd = new HashMap();// 增加控件信息
    // grid需要的项目--------fieldbase方式存储-------------------
    public List<BaseField> data_fields =new ArrayList();
    public List<BaseField> cmd_fields =new ArrayList();
    public BaseField insert_cmd,other_cmd;


    public BaseGrid grid=new BaseGrid(this);

    public BaseDm() {

    }

}
