package io.github.ldhai99.easyOrm.datamodel;

import io.github.ldhai99.easyOrm.base.FrameConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDm {
    // 项目窗口标题
    public String windowTitle;
    public String modelName;// 数据模型对应的表名（项目名）

    // 表字段信息-------------------------------------------------------
    public String tableId = "id"; // 主键
    public String tableParentId = "p_id"; // 父主键
    public String tableDisplayField; // 显示字段
    public String tableUrlField = ""; // URL字段
    public String tableImageField = ""; // 图片列
    public String tableQueryFields; // 查询列列表

    // 更新需要的信息--------------------------------------------------
// 更新相关配置--------------------------------------------------
    public String updateTable; // 更新的目标表
    public String updateFields = "*"; // 需要更新的列
    public String insertFields = "*"; // 插入时需要的列
    public int whereParameterCount = 1; // 更新条件参数的数量

    // 查询需要的信息(一般是页面查询，分页)---------------------------------------
// 查询相关配置(通常是分页查询)---------------------------------------
    public String selectSql; // 自定义完整SQL查询语句
    public String selectTable; // 查询主表
    public String selectFields = "*"; // 查询字段列表
    public String selectWhere = ""; // 查询条件
    public String selectOrderBy = ""; // 排序条件
    public String selectGroupBy = ""; // 分组条件
    //上下应用的数据模型-----------------------------
    public BaseDm previousModel; // 上一个数据模型
    public BaseDm nextModel; // 下一个数据模型


    // 页面传递数值-----------------------------
    public String idKeyValue; // 主键值
    public String pKeyValue; // 父主键值
    public String readOnlyFlag = ""; // 只读标志
    public String eventActionName = ""; // 用户事件名称
    public String operationType; // 操作类型
    // 公用数据
  //  public List<DbParameter> pmt = new ArrayList<DbParameter>();// 用于存储更新参数的集合
    public String info;// 信息字段


    // 公用数据集合------------------------------------------------------------
    public List<Map<String, Object>> dataTable; // 数据表记录集
    public Map<String, Object> currentRow, originalRow; // 当前行和原始行（用于修改或删除时保留旧记录）
    public List<?> dataList; // 多条记录的数据集合

    public HashMap<String, Object> recordData = new HashMap<>(); // 记录数据集合
    public HashMap<String, Object> controlData = new HashMap<>(); // 控件数据集合

    // 页面跳转配置----------
    public String viewTarget = FrameConstant.VIEW_GO_SELF;

    // Grid相关配置--------HashMap方式存储-------------------
    public ArrayList<HashMap> dataColumns = new ArrayList<>(); // 数据列集合
    public ArrayList<HashMap> commandColumns = new ArrayList<>(); // 操作列集合
    public HashMap insertCommand = new HashMap(); // 插入命令
    public HashMap otherCommands = new HashMap(); // 其他命令
    // Grid相关配置--------FieldBase方式存储-------------------
    public List<BaseField> dataFieldList = new ArrayList<>(); // 数据字段列表
    public List<BaseField> commandFieldList = new ArrayList<>(); // 操作字段列表
    public BaseField insertCommandField, otherCommandField; // 插入命令和其他命令字段

    public BaseGrid grid = new BaseGrid(this);

    public BaseDm() {

    }

}
