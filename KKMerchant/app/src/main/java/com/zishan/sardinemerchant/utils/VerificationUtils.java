/*
 *   Copyright (C)  2016 android@19code.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.zishan.sardinemerchant.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Create by h4de5ing 2016/5/21 021
 * https://github.com/sharinghuang/ASRabbit/blob/
 * 7350ea1c212946633316d36760c7088728dc2730/baselib/src/main/java/com/ht/baselib/utils/FormatVerificationUtils.ja
 * v a
 */
public class VerificationUtils {
	private static Map<String, Pattern> patternCache = new HashMap<>();

	// 验证账户 数字+字母
	public static boolean matcherNumAndLetter(String value, int minLen, int maxLen) {
		String regex = "^[a-zA-Z0-9]{" + minLen + "," + maxLen + "}$";
		return testRegex(regex, value);
	}

	// 检验手机号码
	public static boolean matcherPhoneNum(String value) {
		String regex = "^(\\+?\\d{2}-?)?(1[0-9])\\d{9}$";
		return testRegex(regex, value);
	}

	//大于0的正整数

	//大于0的正整数
	public static boolean matcherMoney(String value) {
		String regex ="^\\+?[1-9][0-9]*$";
		return testRegex(regex,value);
	}


	// 校验固定电话
	public static boolean matcherTelNum(String value, int minLen, int maxLen) {
		String regex = "^[0-9]{" + minLen + "," + maxLen + "}$";
		return testRegex(regex, value);
	}

	// 校验银行卡号
	public static boolean matcherBankNum(String value, int minLen, int maxLen) {
		String regex = "^[0-9]{" + minLen + "," + maxLen + "}$";
		return testRegex(regex, value);
	}

	// 校验身份证
	public static boolean matcherIdentityCard(String value) {
		// String regex =
		// "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|" +
		// "(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|"
		// +
		// "\\d{3}[Xx])$)$";
		// return testRegex(regex, value);
		IDCardTester idCardTester = new IDCardTester();
		return idCardTester.test(value);
	}

	// -------------------------------------------//
	public static boolean matcherRealName(String value) {
		String regex = "^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$";
		return testRegex(regex, value);
	}

	// 首字母 ， 数字 字母组合
	public static boolean matcherAccount2(String value, int minLength, int maxLength) {
		String regex = "^[a-zA-Z][a-zA-Z0-9]{" + (minLength - 1) + "," + (maxLength - 1) + "}$";
		return testRegex(regex, value);
	}

	public static boolean matcherAccount(String value) {
		String regex = "[\\u4e00-\\u9fa5a-zA-Z0-9\\-]{4,20}";
		return testRegex(regex, value);
	}

	//
	// // 首字母 ， 数字 字母 下划线组合
	// public static boolean matcherPassword3(String value, int minLength, int
	// maxLength) {
	// String regex = "^[a-zA-Z][a-zA-Z0-9_]{" + (minLength - 1) + "," +
	// (maxLength - 1) + "}$";
	// return testRegex(regex, value);
	// }
	
	// 6-12 数字和字母组合
	public static boolean matcherPassword(String value) {
		String regex = "^[a-zA-Z0-9]{6,12}$";
		return testRegex(regex, value);
	}

	// 数字 字母 下划线组合
	public static boolean matcherPassword2(String value, int minLength, int maxLength) {
		String regex = "^[a-zA-Z0-9_]{" + (minLength) + "," + (maxLength) + "}$";
		return testRegex(regex, value);
	}

	public static boolean matcherEmail(String value) {
		// String regex =
		// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)"
		// +
		// "+[a-zA-Z]{2,}$";
		String regex = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+" + "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+"
				+ "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
		return testRegex(regex, value);
	}

	public static boolean matcherIP(String value) {
		String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\"
				+ "d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\" + "d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
		return testRegex(regex, value.toLowerCase());
	}

	public static boolean matcherUrl(String value) {
		// String regex =
		// "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[wW]{3}\\.[\\w-]+\\.\\w{2,4}(\\/.*)?$";
		String regex = "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[\\w-]+\\.\\w{2,4}(\\/.*)?$";
		return testRegex(regex, value.toLowerCase());
	}

	public static boolean matcherVehicleNumber(String value) {
		String regex = "^[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新渝]?[A-Z][A-HJ-NP-Z0-9学挂港澳练]{5}$";
		return testRegex(regex, value.toLowerCase());
	}

	// public static boolean matcherIdentityCard(String value) {
	// // String regex =
	// "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|" +
	// //
	// "(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|"
	// +
	// // "\\d{3}[Xx])$)$";
	// // return testRegex(regex, value);
	// IDCardTester idCardTester = new IDCardTester();
	// return idCardTester.test(value);
	// }

	private static class IDCardTester {
		public boolean test(String content) {
			if (TextUtils.isEmpty(content)) {
				return false;
			}
			final int length = content.length();
			if (15 == length) {
				try {
					return isOldCNIDCard(content);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return false;
				}
			} else if (18 == length) {
				return isNewCNIDCard(content);
			} else {
				return false;
			}
		}

		final int[] WEIGHT = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

		final char[] VALID = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

		public boolean isNewCNIDCard(String numbers) {
			numbers = numbers.toUpperCase();
			int sum = 0;
			for (int i = 0; i < WEIGHT.length; i++) {
				final int cell = Character.getNumericValue(numbers.charAt(i));
				sum += WEIGHT[i] * cell;
			}
			int index = sum % 11;
			return VALID[index] == numbers.charAt(17);
		}

		public boolean isOldCNIDCard(String numbers) {
			String yymmdd = numbers.substring(6, 11);
			boolean aPass = numbers.equals(String.valueOf(Long.parseLong(numbers)));
			boolean yPass = true;
			try {
				new SimpleDateFormat("yyMMdd").parse(yymmdd);
			} catch (Exception e) {
				e.printStackTrace();
				yPass = false;
			}
			return aPass && yPass;
		}
	}

	public static boolean isNumeric(String input) {
		if (TextUtils.isEmpty(input)) {
			return false;
		}
		char[] chars = input.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz) {
					return false;
				}
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--;
		int i = start;
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				if (hasExp) {
					return false;
				}
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false;
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				return false;
			}
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				return foundDigit && !hasExp;
			}
			return false;
		}
		return !allowSigns && foundDigit;
	}

	public static boolean testRegex(String regex, String inputValue) {
		return Pattern.compile(regex).matcher(inputValue).matches();
	}

	public static boolean checkPostcode(String postcode) {
		String regex = "[1-9]\\d{5}";
		return Pattern.matches(regex, postcode);
	}
	
	public static boolean checkShippingAddress(String address){
		String reg = "^[\\s\u4E00-\u9FFF](?=.*[\u4E00-\u9FFF])[0-9a-zA-Z\u4E00-\u9FFF\\s]{4,40}$";
		return regCheckWithCache(reg, address, "checkShippingAddress");
	}
	public static boolean checkPhone(String phone){
		String reg = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
		return regCheckWithCache(reg, phone, "checkRecipientPhone");
	}
	
	public static boolean checkRecipientName(String recipientName){
		String reg = "^([\\u4e00-\\u9fa5]{1,20}|[a-zA-Z\\.\\s]{1,20})$";
		return regCheckWithCache(reg, recipientName, "checkRecipientName");
	}
	
	public static boolean checkCode(String code) {
		String reg = "[1-9]\\d{5}";
		return regCheckWithCache(reg, code, "checkCode");
	}
	
	private static boolean regCheckWithCache(String reg,String valString,String cacheKey){
		Pattern pattern = patternCache.get(cacheKey);
		if (pattern == null) {
			pattern = Pattern.compile(reg);
			patternCache.put(cacheKey, pattern);
		}
		return pattern.matcher(valString).matches();
	}
	
	public static void clearPattern(){
		patternCache.clear();
	}

	// 长度6-16位   手机密码登录，验证密码
	public static boolean pswIsCorrect(String value) {
		String regex = "^[0-9a-zA-Z~!@#$%^&*()_+`-{}|\\\\[\\\\]:\\\"\\\";'<>?,./=]+$\n";
		return testRegex(regex, value);
	}

}
