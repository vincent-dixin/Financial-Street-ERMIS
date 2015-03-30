package com.fhd.kpi.web.controller.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class Utils {
    public static String getValue(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = new DecimalFormat("0.00").format(Double.parseDouble(value));
        }
        return value;
    }

    /**判断是否为数字字符串
     * @param str 数字字符串
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}