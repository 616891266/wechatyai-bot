package com.smwsk.bot.util;

import com.sun.javafx.scene.shape.PathUtils;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/18 - 11:28
 * Description:
 */
public class FreeMarkUtil {

	public static String getMsgByTemplate(String templateName, Map<String,Object> params){
		try {
			StringWriter stringWriter = new StringWriter();
			Template templateInfo = getTemplateInfo(templateName, "ftl");
			templateInfo.process(params,stringWriter);
			String msgResult = stringWriter.toString();
			return msgResult;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Template getTemplateInfo(String ftlName, String ftlPath){
		try {
			FreeMarkerConfigurationFactory freeMarkerConfigurationFactory = new FreeMarkerConfigurationFactory();
			Configuration configuration = freeMarkerConfigurationFactory.createConfiguration();
			configuration.setDirectoryForTemplateLoading(new File(getClassResource() + ftlPath));
			Template template = configuration.getTemplate(ftlName);
			return template;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getClassResource(){
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString().replaceAll("file:/", "").replaceAll("%20", " ").trim();
		return path;
	}

}
