package com.jf.common.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Title: BaseUtils Description:
 * 
 * @author 阴磊
 * @version 1.0
 */
public final class BaseUtils {

	private static final String EMPTY_TEXT = "";
	private static final String REGEX_MAIL = "^\\+?[A-Za-z0-9](([-+.]|[_]+)?[A-Za-z0-9]+)*@([A-Za-z0-9]+(\\.|\\-))+[A-Za-z]{2,6}$";
	private static final String REGEX_PHONE = "^(\\([0-9]+\\))?[0-9]{7,8}$";
	private static final String REGEX_MOBILE = "^(\\+[0-9]+)?[0-9]{11}";
	private static final String REGEX_NUM = "^\\d+$";
	private static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
	/** 一天的MilliSecond毫秒数 */
	public static final long DAY_MILLI = 24 * 60 * 60 * 1000;
	/** 一小时的MilliSecond数 */
	public static final long HOUR_MILLI = 60 * 60 * 1000;
	public static final long MINUTE_MILLI = 60 * 1000; //

	/**
	 * 验证空白文本信息
	 * 
	 * @param text
	 *            文本
	 * @return 是否为空白
	 */
	public static boolean emptyText(String text) {
		return text == null || EMPTY_TEXT.equals(text);
	}

	/**
	 * 验证邮件
	 * 
	 * @param mail
	 *            邮件文本
	 * @return 是否为mail
	 */
	public static boolean checkMail(String mail) {
		return baseCheck(mail, REGEX_MAIL);
	}

	/**
	 * 验证注册密码  8-20位数字字母组成（不能仅数字或字母）
	 * 
	 * @param password
	 *            密码
	 * @return 是否符合密码格式
	 */
	public static boolean checkPassword(String password) {
		return baseCheck(password, REGEX_PASSWORD);
	}
	
	/**
	 * 验证座机电话
	 * 
	 * @param phone
	 *            电话文本
	 * @return 是否为电话
	 */
	public static boolean checkPhone(String phone) {
		return baseCheck(phone, REGEX_PHONE);
	}

	/**
	 * 验证移动电话
	 * 
	 * @param mobile
	 *            移动电话文本
	 * @return 是否为移动电话
	 */
	public static boolean checkMobile(String mobile) {
		return baseCheck(mobile, REGEX_MOBILE);
	}

	/**
	 * 验证数值整数,不包括小数的验证
	 * 
	 * @param num
	 *            数值
	 * @return 是否为数值
	 */
	public static boolean checkNum(String num) {
		return baseCheck(num, REGEX_NUM);
	}

	/*
	 * 验证符合规则的字符
	 */
	private static boolean baseCheck(String v, String regex) {
		boolean flag = true;
		if (v == null || emptyText(v)) {
			flag = false;
		} else if (REGEX_MAIL.equals(regex)) {// 验证邮件
			flag = isValidateEmail(v);
		} else if (REGEX_MOBILE.equals(regex)) {// 验证移动电话
			int len = v.length();
			if (len < 11) {
				return false;
			} else {
				String str = v;
				if (len > 11) {
					str = v.substring(len - 11, len);
				}
				if (!str.startsWith("1")) {
					flag = false;
				}
				try {
					Long.parseLong(str);
				} catch (Exception e) {
					flag = false;
				}
			}
		} else {
			flag = v.matches(regex); // orginal code
		}
		return flag;
	}

	public static boolean isValidateEmail(String s) {
		boolean flag = false;
		int index;
		int end;
		try {
			if (s.indexOf("@") > 0) {
				index = s.indexOf("@");
				end = s.length();
				int index_2 = s.indexOf("@", index);
				// the number of @ is more than 1
				if (index_2 != -1) {
					if (index < (end - 1)) {
						s = s.substring(index + 1);
						int end_2 = 0;
						while (s.indexOf(".") != -1) {
							index_2 = s.indexOf(".");
							end_2 = s.length();
							if (index_2 != -1) {
								String pointS = s.substring(0, index_2);
								if (!pointS.equals("")) {
									if (index_2 < (end_2 - 1)) {
										s = s.substring(index_2 + 1);
										flag = true;
									} else {
										flag = false;
										break;
									}
								} else {
									flag = false;
									break;
								}
							}
						}
					}
				}
			}
		} catch (StringIndexOutOfBoundsException se) {
			se.printStackTrace();
			return false;
		}
		return flag;
	}

	/**
	 * 生成32位UUID
	 */
	public static String generateUUID32() {
		return generateUUID(32);
	}

	/**
	 * 生成指定长度的UUID 大写
	 * @date 2017年8月4日 下午2:41:32
	 * @param length 指定的长度
	 * @return
	 */
	public static String generateUUID(int length) {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length).toUpperCase();
	}
	
	/**
	 * 生成商品流水号
	 * 
	 * @return
	 */
	public static String generateGoodsBn() {
		return generateUUID32();
	}

	/**
	 * 生成询价单编号
	 * 
	 * @return
	 */
	public static String generateOfferNo() {
		return generateUUID(11);
	}

	/**
	 * 生成报价单编号
	 * 
	 * @return
	 */
	public static String generateTenderNo() {
		return generateUUID32();
	}

	// ----------------------------- date util---------------------------------------------//

	private static final String DATE_FORMAT_REGEX = "yyyy-MM-dd";
	private static final String TIME_FORMAT_REGEX = "yyyy-MM-dd HH:mm:ss";
	private static final String CHINESE_FORMAT_REGEX = "MM月dd日";

	/**
	 * 转换日期字符串，格式：MM月dd日
	 */
	public static String changeChineseMouthDay(Date date) {
		final DateFormat CHINESE_FORMAT = new SimpleDateFormat(CHINESE_FORMAT_REGEX);
		return CHINESE_FORMAT.format(date);
	}

	/**
	 * 转换为日期字符串，格式：yyyy-MM-dd
	 */
	public static String changeToDateString(Integer date) {
		if (date == null)
			return null;
		final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_REGEX);
		return DATE_FORMAT.format(changeIntToDate(date));
	}

	/**
	 * 转换为时间字符串，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String changeToTimeString(Integer date) {
		if (date == null)
			return null;
		return changeDateToTimeString(changeIntToDate(date));
	}

	/**
	 * 将秒时转换为时间类型
	 */
	public static Date changeToDate(Integer date) {
		if (date == null)
			return new Date();
		return changeIntToDate(date);
	}

	/**
	 * 将整型时间转换为Date类型
	 */
	private static Date changeIntToDate(int date) {
		return new Date(date * 1000L);
	}

	/**
	 * 将Date转换为秒时
	 */
	public static int changeDateToInt(Date date) {
		return (int) (date.getTime() / 1000);
	}

	/**
	 * 将字符串的日期转换为int
	 */
	public static int changeStringDateToInt(String date) {
		return changeDateToInt(changeStringTimeToDate(date));
	}

	/**
	 * 将字符串的时间转换为int
	 */
	public static int changeStringTimeToInt(String date) {
		if (emptyText(date))
			return 0;
		return changeDateToInt(changeStringTimeToDate(date));
	}

	/**
	 * 将字符串的时间转换为date，舍弃时分秒
	 */
	public static final Date changeStringDateToDate(String date) {
		final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_REGEX);
		try {
			return DATE_FORMAT.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串的时间转换为date,带有时分秒
	 */
	public static final Date changeStringTimeToDate(String date) {
		final DateFormat DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT_REGEX);
		try {
			return DATE_FORMAT.parse(date);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 将date时间转化为字符串 格式yyyy-MM-dd
	 */
	public static final String changeDateTimeToString(Date date) {
		if (date == null)
			return null;
		final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_REGEX);
		return DATE_FORMAT.format(date);
	}

	/**
	 * 将Date时间转换为字符串 yyyy-MM-dd hh:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static final String changeDateToTimeString(Date date) {
		if (date == null)
			return null;
		final DateFormat TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT_REGEX);
		return TIME_FORMAT.format(date);
	}

	/**
	 * 获取当前系统时间，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		return changeDateToTimeString(getNowDate());
	}

	/**
	 * 获得当前系统的秒时数
	 */
	public static int getNowSecond() {
		return changeDateToInt(getNowDate());
	}

	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 去掉时分秒
	 */
	public static Date exchangeDate(Date d) {
		return changeStringDateToDate(changeDateTimeToString(d));
	}

	/**
	 * @param offset
	 * @return
	 */
	public static Date getDate(int offset) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, offset);
		return c.getTime();
	}

	/**
	 * 获取当前时间的前后某一天，无时分秒
	 * 
	 * @param offset
	 *            游标；前为负数，后为正数
	 */
	public static Date getDateByDate(int offset) {
		return exchangeDate(getDate(offset));
	}

	/**
	 * 获得当前小时
	 */
	public static int getCurrentHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	public static int isCommonDate(Date d1, Date d2) {
		return d1.compareTo(d2);
	}

	/**
	 * 日期转XMLGregorianCalendar
	 * 
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar gc = null;
		try {
			gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gc;
	}

	/**
	 * XMLGregorianCalendar转日期
	 * 
	 * @param xmlDate
	 * @return
	 */
	public static Date convertToDate(XMLGregorianCalendar xmlDate) {
		if (xmlDate != null) {
			GregorianCalendar ca = xmlDate.toGregorianCalendar();
			return ca.getTime();
		}
		return null;
	}

	// ----------------- DATE UTIL END -----------------------------//

	/**
	 * 处理url中的中文参数
	 */
	public static String decodeUrl(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (Exception e) {
			return value;
		}
	}

	// --------------------------- number util------------------------------------//
	/**
	 * 获得实际的整数，默认为0
	 */
	public static Integer realInt(Integer i) {
		return realInt(i, 0);
	}

	/**
	 * 获得实际的整数
	 * 
	 * @param i
	 *            整数
	 * @param defval
	 *            默认值
	 */
	public static Integer realInt(Integer i, int defval) {
		return i == null ? defval : i;
	}

	/**
	 * 获得实际的double值，默认值为0
	 */
	public static Double readDouble(Double d) {
		return readDouble(d, 0);
	}

	/**
	 * 获得实际的double值
	 * 
	 * @param defval
	 *            默认值
	 */
	public static Double readDouble(Double d, double defval) {
		return d == null ? defval : d;
	}

	/**
	 * 取得两个日期之间的日数
	 * 
	 * @author Administrator
	 * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
	 */
	public static long daysBetween(Timestamp t1, Timestamp t2) {
		return (t2.getTime() - t1.getTime()) / DAY_MILLI;
	}

	/**
	 * 取得两个日期之间的
	 *
	 * @author Administrator
	 * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
	 */
	public static long daysBetweenForHour(Timestamp t1,
			Timestamp t2) {
		return (t2.getTime() - t1.getTime()) / HOUR_MILLI;
	}

	/**
	 * 取得两个日期之间的分钟数
	 *
	 * @author Administrator
	 * @return t1到t2间的分钟数，如果t2 在 t1之后，返回正数，否则返回负数
	 */
	public static long daysBetweenForHourMinute(Timestamp t1,
			Timestamp t2) {
		return (t2.getTime() - t1.getTime()) / MINUTE_MILLI;
	}

	public static long daysBetween(Date t1, Date t2) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(t2);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.SECOND, 1);
		Date now = calendar.getTime();

		calendar.setTime(t1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date before = calendar.getTime();
		Timestamp time1 = new Timestamp(before.getTime());
		Timestamp time2 = new Timestamp(now.getTime());
		return daysBetween(time1, time2);
	}

	public static long daysBetweenForHour(Date t1, Date t2) {
		Timestamp time1 = new Timestamp(t1.getTime());
		Timestamp time2 = new Timestamp(t2.getTime());
		return daysBetweenForHour(time1, time2);
	}

	public static long daysBetweenForMinute(Date t1, Date t2) {
		Timestamp time1 = new Timestamp(t1.getTime());
		Timestamp time2 = new Timestamp(t2.getTime());
		return daysBetweenForHourMinute(time1, time2);
	}

	/**
	 * 判断字符是否为空，如果对象为null或者对象的值为"null"或者对象值为""，返回true，否则返回false
	 * 
	 * @param str
	 * @return
	 * @author Administrator
	 */
	public static boolean isEmpty(String str) {
		return (str == null) || "null".equals(str) || "".equals(str);
	}

	/**
	 * 判断字符是否为空，如果对象不为null且对象的值不为"null"且对象值不为""，返回true，否则返回false
	 * 
	 * @param str
	 * @return
	 * @author Administrator
	 */
	public static boolean isNotEmpty(String str) {
		return (str != null) && !"null".equals(str) && !"".equals(str);
	}

	/**
	 * 将金额转换为千分位显示
	 * 
	 * @param money
	 * @return
	 * @author Administrator
	 */
	public static String getSpecialMoney(Double money) {
		DecimalFormat f = new DecimalFormat(",##0.00");
		String specialMOney = f.format(money);
		System.out.println(specialMOney);
		return specialMOney;
	}

	/**
	 * 将Double型四舍五入，只保留小数点两位
	 */
	public static double doubleToDouble(double num) {
		NumberFormat form = NumberFormat.getInstance();
		if (form instanceof DecimalFormat) {
			((DecimalFormat) form).applyPattern("#0.00#");
			((DecimalFormat) form).setMaximumFractionDigits(2);
		}
		String value = form.format(num);
		return Double.parseDouble(value);
	}

	/**
	 * DecimalFormat转换最简便
	 */
	public static String doubleValue2(double num) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(num);
	}

	/**
	 * 将金额四舍五入并转换为千分位显示
	 * 
	 * @param num
	 * @return
	 * @author Administrator
	 */
	public static String roundAndComma(Double num) {
		return getSpecialMoney(doubleToDouble(num));
	}

	/** 计算结束时间，date下单时间，timeLong产品期限，timeUnit产品期限单位 */
	public static Date getEndTime(Date date, String timeLong, String timeUnit) {
		Calendar calendar = Calendar.getInstance();
		if ("Y".equals(timeUnit)) {
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, Integer.parseInt(timeLong));
		} else if ("M".equals(timeUnit)) {
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, Integer.parseInt(timeLong));
		} else {
			calendar.setTime(date);
			calendar.add(Calendar.DATE, Integer.parseInt(timeLong));
		}
		return calendar.getTime();
	}

	// 保留后四位
	public static String numberFormat(Double d) {
		DecimalFormat f = new DecimalFormat(",##0.0000");
		f.setGroupingUsed(false);
		String str = f.format(d);
		return str;
	}

	/**
	 * 过滤数值类型（double,float....）中的科学技术法 (注：以便以后可扩展及页面处理方便) 另注：float
	 * 类型的如果小数点保留六位，数值也会出错。 例如（7775.54f 转化后为：“7775.540039”）
	 * 
	 * @param obj
	 * @return
	 */
	public static String numberToString(Object obj) {
		DecimalFormat df = null;
		if (obj instanceof Double) {
			df = new DecimalFormat("##.##");
		}
		if (obj instanceof Float) {
			df = new DecimalFormat("##.##");
		}
		return df.format(obj);
	}

	// ---------------------------number util end------------------------------------
	public static String nameForMart(String name) {
		if (name == null)
			return "";
		if (name.length() <= 1)
			return name;
		if (name.length() == 2)
			return "*" + name.substring(name.length() - 1, name.length());
		if (name.length() >= 3)
			return "**" + name.substring(name.length() - 1, name.length());
		else
			return "**" + name.substring(name.length() - 1, name.length());
	}

	public static String productNoFormat(String productNo) {
		if (null == productNo || "".equals(productNo))
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(productNo.substring(0, 3));
		sb.append("******");
		sb.append(productNo.substring(productNo.length() - 3,
				productNo.length()));
		return sb.toString().toUpperCase();
	}

	public static String idCardNoFormat(String idCardNo) {
		if (null == idCardNo)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(idCardNo.substring(0, 3));
		int j = idCardNo.length() - 6;
		for (int i = 0; i < j; i++) {
			sb.append("*");
		}
		sb.append(idCardNo.substring(idCardNo.length() - 3, idCardNo.length()));
		return sb.toString().toUpperCase();
	}

	public static String getSexByIdCardNo(String loanIdCardNo) {
		if (loanIdCardNo == null)
			return "男";
		String str = "";
		if (loanIdCardNo.length() == 15) {
			str = loanIdCardNo.substring(loanIdCardNo.length() - 1,
					loanIdCardNo.length());
		} else {
			str = loanIdCardNo.substring(loanIdCardNo.length() - 2,
					loanIdCardNo.length() - 1);
		}
		System.out.println(str);
		if (Integer.valueOf(str) % 2 == 0) {
			return "女";
		} else {
			return "男";
		}
	}

	public static String getAgeByIdCardNo(String idCardNo) {
		if (isEmpty(idCardNo))
			return "";
		if (idCardNo.length() != 18 && idCardNo.length() != 15)
			return "";
		idCardNo = idCardNo.trim();
		Date date = new Date();
		String yearStr = "";
		if (idCardNo.length() == 18) {
			yearStr = idCardNo.substring(6, 10);
		} else {
			yearStr = idCardNo.substring(6, 8);
			yearStr = "19" + yearStr;
		}
		Integer yeatInt = Integer.valueOf(yearStr);
		Integer integer = yeatInt - 1900;
		int year = date.getYear();
		int i = year - integer;
		return i + "";
	}

	/**
	 * 获取本机IP
	 * 
	 * @return
	 */
	public static String getLocalIP() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		return ipAddrStr;
	}

	/**
	 * 四舍五入
	 * 
	 * <p>
	 * 创建人：luyj , 2015年7月26日 上午12:06:13
	 * </p>
	 * <p>
	 * 修改人：luyj , 2015年7月26日 上午12:06:13
	 * </p>
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static BigDecimal round(BigDecimal v, int scale) {
		if (v == null || scale < 0) {
			return BigDecimal.ZERO;
		}
		return v.divide(BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP);
	}
}
