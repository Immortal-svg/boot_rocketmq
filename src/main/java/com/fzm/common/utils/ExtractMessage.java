package com.fzm.common.utils;

import com.alibaba.druid.support.monitor.entity.MonitorApp;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class ExtractMessage {
 
/*	public static void main(String[] args) {

		String msg = "PerformanceManager[#第1个中括号#]Product[#第2个中括号#]<[第3个中括号]79~";
		List<String> list = extractMessageByRegular(msg);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i+"-->"+list.get(i));
		}
	}*/

	/**
	 * 使用正则表达式提取中括号中的内容
	 *
	 * @param msg
	 * @return
	 */
	public static List<String> extractMessageByblank(String msg) {
		if (StringUtils.isBlank(msg)) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
		Matcher m = p.matcher(msg);
		while (m.find()) {
			list.add(m.group().substring(1, m.group().length() - 1));
		}
		return list;
	}


	public static ArrayList<String> extractMessageByRegular(String msg) {
		if (StringUtils.isBlank(msg)) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("(\\#[^\\#]*\\#)");
		Matcher m = p.matcher(msg);
		while (m.find()) {
			list.add(m.group().substring(1, m.group().length() - 1));
		}
		return list;
	}


	public static Map<String, String> tplValueExtract(String msg) {
		if (StringUtils.isBlank(msg)) {
			return null;
		}
		Map<String, String> data = new HashMap<>();

		try {
			String[] parms = msg.split("&");
			if (null != parms) {
				for (int i = 0; i < parms.length; i++) {
					String var = parms[i];
					if (null != var) {
						String[] string = var.split("=");
						if (null != string && string.length == 2) {
							data.put(URLDecoder.decode(string[0], "UTF-8"), URLDecoder.decode(string[1], "UTF-8"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}
}

