package io.github.ldhai99.easyOrm.datamodel;


import io.github.ldhai99.easyOrm.base.FrameConstant;

public class BaseField {

	public String p_id;//属于哪个表
	public String id;
	public String group; // 组别
	public String order_number; // 序号

	public String field_name;// 字段名称
	public String field_desc;// 字段说明

	//---涉及编辑部分属性//DataType--数据类型(varchar,DateTime,Date,Money4,Money)
	public String data_type= FrameConstant.VARCHAR;
	public String choice;
	public boolean is_null=true;// 是否允许为空

	//涉及显示部分字段---------------------------------
	public boolean is_display=true;// 是否显示

	//display_type--字段显示类型(Text,CheckBox,Image,Button,HyperLink)
	public String display_type= FrameConstant.TEXT;

	public String table1;
	public String field1;
	public String table0;

	public  String url;//Url---导航Url 导航用到的URL链接
	public  String url_id;////UrlId 传参id，
	public  String url_id_value;	//UrlIdValue
	public  String ask_info;
	public  String clickJs;
	public  String target;


	// 涉及打印部分属性-----------------------------
	public String print_name;// 字段打印名
	public int p_left;//打印左边距
	public int P_top;//打印上边距
	public int p_width;//打印宽度
	public int P_height;//打印高度
	public int fontsize;//字体大小
	public int fontheight;//字体高度
	public int fontname;//字典名称

}
