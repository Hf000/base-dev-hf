package org.hf.common.web.desensitize;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hf.common.publi.constants.CommonConstant.*;
import static org.hf.common.publi.constants.NumberConstant.*;

/**
 * <p> 脱敏工具类 </p >
 * @author HUFEI
 * @date 2022-08-24
 **/
public class DesensitizedUtils {

    /**
     * 对字符串进行脱敏操作
     *
     * @param origin          原始字符串
     * @param prefixNoMaskLen 左侧需要保留几位明文字段
     * @param suffixNoMaskLen 右侧需要保留几位明文字段
     * @param maskStr         用于遮罩的字符串, 如'*'
     * @return 脱敏后结果
     */
    public static String desValue(String origin, int prefixNoMaskLen, int suffixNoMaskLen, String maskStr) {
        if (StringUtils.isBlank(origin)) {
            return origin;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = INT_0, n = origin.length(); i < n; i++) {
            if (i < prefixNoMaskLen) {
                sb.append(origin.charAt(i));
                continue;
            }
            if (i > (n - suffixNoMaskLen - 1)) {
                sb.append(origin.charAt(i));
                continue;
            }
            sb.append(maskStr);
        }
        return sb.toString();
    }

    /**
     * 对姓名进行脱敏
     *
     * @param name 入参
     * @return String
     */
    public static String desensitizedUserName(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        //中文姓名
        if (isChinese(name)) {
            return desensitizedChineseUserName(name);
        }
        //非中文姓名
        String[] s = name.split(" ");
        if (s.length > INT_0) {
            if (s.length == INT_1) {
                return name;
            }
            String subfix = s[s.length - INT_1];
            int index = name.length() - subfix.length() - INT_1;
            StringBuilder result = new StringBuilder();
            for (int i = INT_0; i < index; i++) {
                result.append(ASTERISK);
            }
            return result.toString() + " " + subfix;
        }
        return name;
    }

    private static String desensitizedChineseUserName(String name) {
        String lastName = StringUtils.EMPTY;
        int countLength = StringUtils.length(name);
        if (countLength > INT_2) {
            for (String s : DOUBLE_SURNAME) {
                if (StringUtils.startsWith(name, s)) {
                    lastName = s;
                    break;
                }
            }
        }
        if (StringUtils.isBlank(lastName)) {
            lastName += StringUtils.substring(name, INT_0, INT_1);
        }
        int length = Math.min(countLength, MAX_LENGTH);

        return StringUtils.rightPad(lastName, length, PADDING_CHAR);
    }

    /**
     * 是否是中文
     *
     * @param str 入参
     * @return boolean
     */
    public static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 身份证脱敏
     *
     * @param idNumber 入参
     * @return String
     */
    public static String desensitizedIdNumber(String idNumber) {
        return StringUtils.isBlank(idNumber) || idNumber.length() < INT_8 ? idNumber : desValue(idNumber, INT_4, INT_4, ASTERISK);
    }

    /**
     * 1.大陆的 1+（3—9）+任意九位数
     * ^[1][3-9]\\d{9}$
     * <p>
     * 2.香港（5|6|9）+任意七位数
     * ^([5|6|9])\\d{7}$
     * <p>
     * 3.台湾 09+任意八位数
     * ^[0][9]\\d{8}$
     * <p>
     * 4.澳门6+任意七位数
     * ^[6]\\d{7}$
     * <p>
     * 手机号脱敏
     *
     * @param phoneNumber 入参
     * @return String
     */
    public static String desensitizedPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return phoneNumber;
        }
        PhoneNumAttributionEnum phoneNumAttributionEnum = getPhoneNumAttributionEnum(phoneNumber);
        switch (phoneNumAttributionEnum) {
            case MAINLAND:
                phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
                break;
            case HK:
            case MACAO:
                phoneNumber = DesensitizedUtils.desValue(phoneNumber, INT_2, INT_2, ASTERISK);
                break;
            case TAIWAN:
                phoneNumber = DesensitizedUtils.desValue(phoneNumber, INT_2, INT_3, ASTERISK);
                break;
            default:
                if (phoneNumber.length() <= INT_4) {
                    break;
                } else if (phoneNumber.length() <= INT_8) {
                    phoneNumber = phoneNumber.replaceAll("(\\w{4})\\w*", "$1****");
                } else {
                    int subfixLenth = phoneNumber.length() - INT_8;
                    phoneNumber = phoneNumber.replaceAll("(\\w{4})\\w*(\\w{" + subfixLenth + "})", "$1****$2");
                }
                break;
        }
        return phoneNumber;
    }

    /**
     * 是否大陆手机号
     *
     * @param phone 入参
     * @return boolean
     */
    public static boolean isMainlandPhone(String phone) {
        String regEx = "^[1][3-9]\\d{9}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    /**
     * 是否香港手机号
     *
     * @param phone 入参
     * @return boolean
     */
    public static boolean isHkPhone(String phone) {
        String regEx = "^([5|6|9])\\d{7}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    /**
     * 是否台湾手机号
     *
     * @param phone 入参
     * @return boolean
     */
    public static boolean isTaiwanPhone(String phone) {
        String regEx = "^[0][9]\\d{8}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    /**
     * 是否澳门手机号
     *
     * @param phone 入参
     * @return boolean
     */
    public static boolean isMacaoPhone(String phone) {
        String regEx = "^[6]\\d{7}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    public static PhoneNumAttributionEnum getPhoneNumAttributionEnum(String phone) {
        if (isMainlandPhone(phone)) {
            return PhoneNumAttributionEnum.MAINLAND;
        }
        if (isHkPhone(phone)) {
            return PhoneNumAttributionEnum.HK;
        }
        if (isTaiwanPhone(phone)) {
            return PhoneNumAttributionEnum.TAIWAN;
        }
        if (isMacaoPhone(phone)) {
            return PhoneNumAttributionEnum.MACAO;
        }
        return PhoneNumAttributionEnum.OTHER;
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address 入参
     */
    public static String desensitizedAddress(final String address) {
        if (StringUtils.isBlank(address) || address.length() < INT_6) {
            return address;
        }
        final int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - INT_6), length, ASTERISK);

    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address 入参
     */
    public static String desensitizedAddress2(final String address) {
        if (StringUtils.isBlank(address) || address.length() <= INT_6) {
            return StringUtils.leftPad("",address.length(), ASTERISK);
        }
        final int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, INT_6), length, ASTERISK);

    }

    /**
     * 邮箱脱敏
     *
     * @param email 入参
     * @return String
     */
    public static String desensitizedEmail(String email) {
        if (StringUtils.isBlank(email) || !email.contains(AT)) {
            return email;
        }
        String emailPrefix = email.substring(INT_0, email.indexOf(AT));
        if (StringUtils.length(emailPrefix) < INT_2) {
            return email;
        }
        String emailSubfix = email.substring(email.indexOf(AT));
        return StringUtils.left(emailPrefix, INT_2).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(emailPrefix, INT_0), StringUtils.length(emailPrefix), ASTERISK), ASTERISK)) + emailSubfix;
    }

    /**
     * 银行卡号脱敏
     *
     * @param bankCode 入参
     * @return String
     */
    public static String desensitizedBankCode(String bankCode) {
        if (StringUtils.isBlank(bankCode)) {
            return bankCode;
        }
        return bankCode.length() > INT_10 ? desValue(bankCode, INT_6, INT_4, ASTERISK) : desValue(bankCode, INT_2, INT_2, ASTERISK);
    }

    /**
     * 脱敏用户信息
     * @param userObj 入参
     */
    public static void desensitizedUserInfo(Object userObj) {
        if (Objects.isNull(userObj)) {
            return;
        }
        try {
            for (Field field : userObj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String filedName = field.getName();
                Object filedValue = field.get(userObj);
                // 姓名
                if (filedValue instanceof String && DES_USER_NAME.contains(filedName)) {
                    field.set(userObj, desensitizedChineseUserName((String) filedValue));
                }
                // 身份证号
                if (filedValue instanceof String && DES_ID_NO.contains(filedName)) {
                    field.set(userObj, desensitizedIdNumber((String) filedValue));
                }
                // 手机号
                if (filedValue instanceof String && DES_MOBILE.contains(filedName)) {
                    field.set(userObj, desensitizedPhoneNumber((String) filedValue));
                }
                // 地址
                if (filedValue instanceof String && DES_ADDRESS.contains(filedName)) {
                    field.set(userObj, desensitizedAddress((String) filedValue));
                }
                // 邮件
                if (filedValue instanceof String && DES_EMAIL.contains(filedName)) {
                    field.set(userObj, desensitizedEmail((String) filedValue));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static List<String> DOUBLE_SURNAME = new ArrayList<>();

    public static final String PADDING_CHAR = ASTERISK;

    public static final int MAX_LENGTH = INT_5;

    static {
        DOUBLE_SURNAME.add("欧阳");
        DOUBLE_SURNAME.add("太史");
        DOUBLE_SURNAME.add("端木");
        DOUBLE_SURNAME.add("上官");
        DOUBLE_SURNAME.add("司马");
        DOUBLE_SURNAME.add("东方");
        DOUBLE_SURNAME.add("独孤");
        DOUBLE_SURNAME.add("南宫");
        DOUBLE_SURNAME.add("万俟");
        DOUBLE_SURNAME.add("闻人");
        DOUBLE_SURNAME.add("夏侯");
        DOUBLE_SURNAME.add("诸葛");
        DOUBLE_SURNAME.add("尉迟");
        DOUBLE_SURNAME.add("公羊");
        DOUBLE_SURNAME.add("赫连");
        DOUBLE_SURNAME.add("澹台");
        DOUBLE_SURNAME.add("皇甫");
        DOUBLE_SURNAME.add("宗政");
        DOUBLE_SURNAME.add("濮阳");
        DOUBLE_SURNAME.add("公冶");
        DOUBLE_SURNAME.add("太叔");
        DOUBLE_SURNAME.add("申屠");
        DOUBLE_SURNAME.add("公孙");
        DOUBLE_SURNAME.add("慕容");
        DOUBLE_SURNAME.add("仲孙");
        DOUBLE_SURNAME.add("钟离");
        DOUBLE_SURNAME.add("长孙");
        DOUBLE_SURNAME.add("宇文");
        DOUBLE_SURNAME.add("司徒");
        DOUBLE_SURNAME.add("鲜于");
        DOUBLE_SURNAME.add("司空");
        DOUBLE_SURNAME.add("闾丘");
        DOUBLE_SURNAME.add("子车");
        DOUBLE_SURNAME.add("亓官");
        DOUBLE_SURNAME.add("司寇");
        DOUBLE_SURNAME.add("巫马");
        DOUBLE_SURNAME.add("公西");
        DOUBLE_SURNAME.add("颛孙");
        DOUBLE_SURNAME.add("壤驷");
        DOUBLE_SURNAME.add("公良");
        DOUBLE_SURNAME.add("漆雕");
        DOUBLE_SURNAME.add("乐正");
        DOUBLE_SURNAME.add("宰父");
        DOUBLE_SURNAME.add("谷梁");
        DOUBLE_SURNAME.add("拓跋");
        DOUBLE_SURNAME.add("夹谷");
        DOUBLE_SURNAME.add("轩辕");
        DOUBLE_SURNAME.add("令狐");
        DOUBLE_SURNAME.add("段干");
        DOUBLE_SURNAME.add("百里");
        DOUBLE_SURNAME.add("呼延");
        DOUBLE_SURNAME.add("东郭");
        DOUBLE_SURNAME.add("南门");
        DOUBLE_SURNAME.add("羊舌");
        DOUBLE_SURNAME.add("微生");
        DOUBLE_SURNAME.add("公户");
        DOUBLE_SURNAME.add("公玉");
        DOUBLE_SURNAME.add("公仪");
        DOUBLE_SURNAME.add("梁丘");
        DOUBLE_SURNAME.add("公仲");
        DOUBLE_SURNAME.add("公上");
        DOUBLE_SURNAME.add("公门");
        DOUBLE_SURNAME.add("公山");
        DOUBLE_SURNAME.add("公坚");
        DOUBLE_SURNAME.add("左丘");
        DOUBLE_SURNAME.add("公伯");
        DOUBLE_SURNAME.add("西门");
        DOUBLE_SURNAME.add("公祖");
        DOUBLE_SURNAME.add("第五");
        DOUBLE_SURNAME.add("公乘");
        DOUBLE_SURNAME.add("贯丘");
        DOUBLE_SURNAME.add("公皙");
        DOUBLE_SURNAME.add("南荣");
        DOUBLE_SURNAME.add("东里");
        DOUBLE_SURNAME.add("东宫");
        DOUBLE_SURNAME.add("仲长");
        DOUBLE_SURNAME.add("子书");
        DOUBLE_SURNAME.add("子桑");
        DOUBLE_SURNAME.add("即墨");
        DOUBLE_SURNAME.add("淳于");
        DOUBLE_SURNAME.add("达奚");
        DOUBLE_SURNAME.add("褚师");
        DOUBLE_SURNAME.add("吴铭");
        DOUBLE_SURNAME.add("纳兰");
        DOUBLE_SURNAME.add("归海");
        DOUBLE_SURNAME.add("刘付");
    }

    private static final List<String> DES_USER_NAME = Arrays.asList("realName", "customerName", "appointmentHolderName", "orderPlacerName", "otherPayerName", "receiverName", "stewardName");

    private static final List<String> DES_ID_NO = Arrays.asList("idNo", "customerIdNo", "certNo");

    private static final List<String> DES_MOBILE = Arrays.asList("mobile", "customerMobile", "appointmentHolderMobile", "orderPlacerMobile", "otherPayerMobile", "receiverMobile", "contact", "phoneNum", "phone");

    private static final List<String> DES_ADDRESS = Arrays.asList("address", "customerAddress", "fullAddress");

    private static final List<String> DES_EMAIL = Arrays.asList("email", "customerEmail");

}
