package io.github.ldhai99.easyOrm.datamodel;

import io.github.ldhai99.easyOrm.base.FrameConstant;

import java.util.HashMap;

public class BaseGrid {
	public BaseDm dm;

	public BaseGrid(BaseDm dm) {
		this.dm = dm;
	}


	// 生成表数据字段说明
	public void addData_column(String[] key_fields, Object[] key_values) {
		int li_columns = key_fields.length;
		HashMap m_row = new HashMap((li_columns + 3), 1);

		for (int i = 0; i < li_columns; i++) {
			m_row.put(key_fields[i], key_values[i]);
		}
		dm.lst_data_column.add(m_row);

	}

	// 生成表命令字段说明
	public void addCmd_column(String[] key_fields, Object[] key_values) {
		int li_columns = key_fields.length;
		HashMap m_row = new HashMap((li_columns + 3), 1);

		for (int i = 0; i < li_columns; i++) {
			m_row.put(key_fields[i], key_values[i]);
		}
		dm.lst_cmd_column.add(m_row);

	}

	// 生成表字段说明
	public void addInsert_cmd(String[] key_fields, Object[] key_values) {
		int li_columns = key_fields.length;
		for (int i = 0; i < li_columns; i++) {
			dm.map_insert_cmd.put(key_fields[i], key_values[i]);
		}

	}

	// 生成表字段说明
	public void addOther_cmd(String[] key_fields, Object[] key_values) {
		int li_columns = key_fields.length;
		for (int i = 0; i < li_columns; i++) {
			dm.map_other_cmd.put(key_fields[i], key_values[i]);
		}

	}


	public BaseField getDataField(String field_name, String field_desc)
	{
		BaseField field = new BaseField();
		field.field_name=field_name;
		field.field_desc=field_desc;

		return field;
	}
	public BaseField getDataField(String field_name, String field_desc, boolean visible)
	{
		BaseField field = new BaseField();
		field.field_name=field_name;
		field.field_desc=field_desc;
		field.is_display=visible;

		return field;
	}
	/*
	 * update field
	 */
	public BaseField getUpdateCmd(String aurl, String aid) {

		BaseField field = new BaseField();

		field.field_name = FrameConstant.cmd_field;
		field.field_desc = FrameConstant.update_title;
		field.url = aurl + "update";
		field.url_id = aid;
		field.ask_info = FrameConstant.ask_update;
		return field;

	}
	/*
	 * insert field
	 */
	public BaseField getInsertCmd(String aurl, String aid) {

		BaseField field = new BaseField();

		field.field_name = FrameConstant.cmd_field;
		field.field_desc = FrameConstant.insert_title;
		field.url = aurl + "insert";
		field.url_id = aid;
		field.ask_info = FrameConstant.ask_insert;
		return field;
	}
	/*
	 * delete field
	 */
	public BaseField getDeleteCmd(String aurl, String aid) {

		BaseField field = new BaseField();

		field.field_name = FrameConstant.cmd_field;
		field.field_desc = FrameConstant.delete_title;
		field.url = aurl + "delete";
		field.url_id = aid;
		field.ask_info = FrameConstant.ask_delete;
		return field;
	}
	/*
	 * view field
	 */
	public BaseField getViewCmd(String aurl, String aid) {

		BaseField field = new BaseField();

		field.field_name = FrameConstant.cmd_field;
		field.field_desc = FrameConstant.view_title;
		field.url = aurl + "view";
		field.url_id = aid;
		field.ask_info = FrameConstant.ask_view;
		return field;
	}
	/*
	 * cmd field
	 */
	public BaseField getCmdField(String title, String url, String url_id, String ask_info) {

		BaseField field = new BaseField();

		field.field_name = FrameConstant.cmd_field;
		field.field_desc = title;
		field.url = url ;
		field.url_id = url_id;
		field.ask_info = ask_info;
		return field;
	}

}
