package io.github.ldhai99.easyOrm.datamodel;


import io.github.ldhai99.easyOrm.base.FrameConstant;

public class BaseField {
	// 表信息-------------------------------------------------------
	public String modelId; // 关联的 DataModel ID (所属表)
	public String id; // 字段ID
	public String group; // 组别
	public int orderNumber; // 序号

	// 字段基本信息-------------------------------------------------
	public String fieldName; // 字段名称
	public String fieldDescription; // 字段说明

	//---涉及编辑部分属性//DataType--数据类型(varchar,DateTime,Date,Money4,Money)
	public String dataType = FrameConstant.DATA_TYPE_VARCHAR; // 数据类型 (如 varchar, DateTime, Date 等)
	public String choice; // 可选项
	public boolean isNullable = true; // 是否允许为空

	//涉及显示部分字段---------------------------------
	public boolean isDisplayable = true; // 是否显示
	public String displayType = FrameConstant.DISPLAY_TYPE_TEXT; // 显示类型 (如 Text, CheckBox, Image 等)

	// 关联表信息--------------------------------------------------
	public String relatedTable; // 关联表1
	public String relatedField; // 关联字段
	public String referenceTable; // 引用表0

	// URL 导航相关属性--------------------------------------------
	public String navigationUrl; // 导航URL链接
	public String urlParameterId; // URL参数ID
	public String urlParameterValue; // URL参数值
	public String requestInfo; // 请求信息
	public String onClickJs; // 点击事件JS脚本
	public String targetView; // 目标视图



// 打印相关属性-------------------------------------------------
	public String printName; // 字段打印名
	public int printLeftMargin; // 打印左边距
	public int printTopMargin; // 打印上边距
	public int printWidth; // 打印宽度
	public int printHeight; // 打印高度
	public int fontSize; // 字体大小
	public int fontHeight; // 字体高度
	public int fontName; // 字体名称

}
