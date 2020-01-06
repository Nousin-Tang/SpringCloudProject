package com.nousin.springcloud.server.storage.web.common.util;

import java.io.File;
import java.io.IOException;

import com.nousin.springcloud.server.storage.framework.common.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;


/**
 * 全局配置类
 */
public class Global {

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();

	public static Global getInstance() {
		return global;
	}

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";

	private static Environment env = SpringContextUtil.getBean(Environment.class);

	public static String getConfig(String key) {
		String value = env.getProperty(key);

		return value;
	}

	//获得EXCEL模板地址
	public static String getFileBase() {
		return getConfig("userfiles.basedir");
	}

	//获得零时文件
	public static String getFileBaseTemp() {
		return getConfig("userfiles.basedir") + File.separator + "userfiles" + File.separator + "temp";
	}

	//获得EXCEL模板地址
	public static String getExcelPath() {
		return getConfig("userfiles.basedir") + getConfig("userfiles.excel_template_path");
	}

	/**
	* 获取工程路径
	* @return
	*/
	public static String getProjectPath() {
		// 如果配置了工程路径，则直接返回，否则自动获取。
		String projectPath = Global.getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)) {
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				while (true) {
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()) {
						break;
					}
					if (file.getParentFile() != null) {
						file = file.getParentFile();
					} else {
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
	}
	
	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}

	public static long getImportMaxSize() {

		String importSize = getConfig("import.size").trim();
		long importMaxSize = 5*1024*1024;

		if (StringUtils.isBlank(importSize) || StringUtils.isBlank(StringUtils.left(importSize, importSize.length() - 1))) {
			return importMaxSize;
		}

		String maxSize = StringUtils.left(importSize, importSize.length() - 1);
		String unit = StringUtils.right(importSize, 1);

		if (unit.equals("K") || unit.equals("k")) {
			importMaxSize = Long.parseLong(maxSize) * 1024;
		} else if (unit.equals("M") || unit.equals("m")) {
			importMaxSize = Long.parseLong(maxSize) * 1024 * 1024;
		} else if (unit.equals("G") || unit.equals("g")) {
			importMaxSize = Long.parseLong(maxSize) * 1024 * 1024 * 1024;
		}

		return importMaxSize;
	}
}
