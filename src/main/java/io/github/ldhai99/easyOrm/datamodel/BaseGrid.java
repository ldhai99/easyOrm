package io.github.ldhai99.easyOrm.datamodel;

import io.github.ldhai99.easyOrm.base.FrameConstant;
import java.util.HashMap;

public class BaseGrid {

	public BaseDm dm;

	/**
	 * 构造函数，初始化 BaseGrid 对象
	 *
	 * @param dm 数据模型对象
	 */
	public BaseGrid(BaseDm dm) {
		if (dm == null) {
			throw new IllegalArgumentException("BaseDm cannot be null");
		}
		this.dm = dm;
	}

	/**
	 * 添加数据字段到表格数据列
	 *
	 * @param keyFields  字段名数组
	 * @param keyValues  字段值数组
	 */
	public void addDataColumn(String[] keyFields, Object[] keyValues) {
		validateInput(keyFields, keyValues);

		int columnCount = keyFields.length;
		HashMap<String, Object> rowMap = new HashMap<>((columnCount + 3), 1);

		for (int i = 0; i < columnCount; i++) {
			rowMap.put(keyFields[i], keyValues[i]);
		}
		dm.dataColumns.add(rowMap);
	}

	/**
	 * 添加命令字段到表格命令列
	 *
	 * @param keyFields  字段名数组
	 * @param keyValues  字段值数组
	 */
	public void addCmdColumn(String[] keyFields, Object[] keyValues) {
		validateInput(keyFields, keyValues);

		int columnCount = keyFields.length;
		HashMap<String, Object> rowMap = new HashMap<>((columnCount + 3), 1);

		for (int i = 0; i < columnCount; i++) {
			rowMap.put(keyFields[i], keyValues[i]);
		}
		dm.commandColumns.add(rowMap);
	}

	/**
	 * 添加插入命令字段到映射表
	 *
	 * @param keyFields  字段名数组
	 * @param keyValues  字段值数组
	 */
	public void addInsertCmd(String[] keyFields, Object[] keyValues) {
		validateInput(keyFields, keyValues);

		for (int i = 0; i < keyFields.length; i++) {
			dm.insertCommand.put(keyFields[i], keyValues[i]);
		}
	}

	/**
	 * 添加其他命令字段到映射表
	 *
	 * @param keyFields  字段名数组
	 * @param keyValues  字段值数组
	 */
	public void addOtherCmd(String[] keyFields, Object[] keyValues) {
		validateInput(keyFields, keyValues);

		for (int i = 0; i < keyFields.length; i++) {
			dm.otherCommands.put(keyFields[i], keyValues[i]);
		}
	}

	private void validateInput(String[] keyFields, Object[] keyValues) {
		if (keyFields == null || keyValues == null || keyFields.length != keyValues.length) {
			throw new IllegalArgumentException("Key fields and key values must not be null and must have the same length");
		}
	}

	/**
	 * 创建数据字段对象
	 *
	 * @param fieldName   字段名称
	 * @param fieldDesc   字段描述
	 * @return 返回 BaseField 对象
	 */
	public BaseField createDataField(String fieldName, String fieldDesc) {
		return createDataField(fieldName, fieldDesc, true);
	}

	/**
	 * 创建数据字段对象，并设置是否可见
	 *
	 * @param fieldName   字段名称
	 * @param fieldDesc   字段描述
	 * @param isVisible   是否可见
	 * @return 返回 BaseField 对象
	 */
	public BaseField createDataField(String fieldName, String fieldDesc, boolean isVisible) {
		if (fieldName == null || fieldDesc == null) {
			throw new IllegalArgumentException("Field name and description must not be null");
		}

		BaseField field = new BaseField();
		field.fieldName = fieldName;
		field.fieldDescription = fieldDesc;
		field.isDisplayable = isVisible;
		return field;
	}

	/**
	 * 创建通用命令字段对象
	 *
	 * @param title       命令标题
	 * @param url         命令的 URL
	 * @param urlId       命令的 ID 参数
	 * @param askInfo     请求信息
	 * @return 返回 BaseField 对象
	 */
	public BaseField createCommandField(String title, String url, String urlId, String askInfo) {
		if (title == null || url == null || urlId == null || askInfo == null) {
			throw new IllegalArgumentException("All parameters must not be null");
		}

		BaseField field = new BaseField();
		field.fieldName = FrameConstant.CMD_FIELD;
		field.fieldDescription = title;
		field.navigationUrl = url;
		field.urlParameterId = urlId;
		field.requestInfo = askInfo;
		return field;
	}

	/**
	 * 创建更新命令字段对象
	 *
	 * @param url 更新操作的 URL
	 * @param id  更新操作的 ID 参数
	 * @return 返回 BaseField 对象
	 */
	public BaseField createUpdateCommand(String url, String id) {
		return createCommandField(FrameConstant.TITLE_UPDATE, url + "update", id, FrameConstant.ASK_CONFIRM_UPDATE);
	}

	/**
	 * 创建插入命令字段对象
	 *
	 * @param url 插入操作的 URL
	 * @param id  插入操作的 ID 参数
	 * @return 返回 BaseField 对象
	 */
	public BaseField createInsertCommand(String url, String id) {
		return createCommandField(FrameConstant.TITLE_INSERT, url + "insert", id, FrameConstant.ASK_CONFIRM_INSERT);
	}

	/**
	 * 创建删除命令字段对象
	 *
	 * @param url 删除操作的 URL
	 * @param id  删除操作的 ID 参数
	 * @return 返回 BaseField 对象
	 */
	public BaseField createDeleteCommand(String url, String id) {
		return createCommandField(FrameConstant.TITLE_DELETE, url + "delete", id, FrameConstant.ASK_CONFIRM_DELETE);
	}

	/**
	 * 创建查看命令字段对象
	 *
	 * @param url 查看操作的 URL
	 * @param id  查看操作的 ID 参数
	 * @return 返回 BaseField 对象
	 */
	public BaseField createViewCommand(String url, String id) {
		return createCommandField(FrameConstant.TITLE_VIEW, url + "view", id, FrameConstant.ASK_CONFIRM_VIEW);
	}
}