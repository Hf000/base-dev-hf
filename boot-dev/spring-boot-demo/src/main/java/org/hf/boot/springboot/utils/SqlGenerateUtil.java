package org.hf.boot.springboot.utils;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.pojo.entity.UserInfo;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * sql生成工具类
 */
public class SqlGenerateUtil {

    /**
     * 根据data对象非空数据拼装SET部分SQL语句,根据data对象主键拼装WHERE部分SQL语句,构造完整更新SQL语句
     */
    public static String genUpdateSqlSelective(Class<?> entityClass, Object data) {
        return genUpdateSql(entityClass, data, null, true);
    }
    /**
     * 根据data对象非空数据拼装SET部分SQL语句,根据whereObj对象非空数据拼装WHERE部分SQL语句,构造完整更新SQL语句
     */
    public static String genUpdateSqlByWhereSelective(Class<?> entityClass, Object data, Object whereObj) {
        return genUpdateSql(entityClass, data, whereObj, true);
    }

    /**
     * 根据对象生成更新语句
     * @param entityClass   对应数据库表实体对象类型
     * @param data          需要赋值SQL更新值的对象数据
     * @param whereObj      需要组装的WHERE条件查询数据对象信息,不会拼接主键列
     * @param notEmpty      是否组装字段为空的数据
     * @return 拼装好的SQL的UPDATE语句脚本
     */
    public static String genUpdateSql(Class<?> entityClass, Object data, Object whereObj, boolean notEmpty) {
        if (data == null) {
            return "";
        }
        EntityHelper.initEntityNameMap(entityClass, new Config());
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        // 开始拼sql
        StringBuilder sql = new StringBuilder();
        try {
            // 构建更新表名部分语句
            sql.append(updateTable(entityTable.getName()));
            // 构建更新值SET部分语句
            sql.append(updateSetColumns(entityClass, data, notEmpty, notEmpty));
            if (whereObj == null) {
                // 根据主键列拼接where条件语句
                sql.append(wherePKColumns(entityClass, data));
            } else {
                // 根据whereObj不为空的属性列进行where条件拼装,排除主键列
                sql.append(whereObjColumns(entityClass, whereObj));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        sql.append(";");
        return sql.toString();
    }

    /**
     * 根据指定对象数据拼装除了主键之外的其他有值属性的where条件部分的SQL语句
     * @param entityClass   对应数据库表实体对象类型
     * @param whereObj      where条件拼装数据对象
     * @return 返回where拼装条件语句字符串
     */
    private static String whereObjColumns(Class<?> entityClass, Object whereObj)
            throws IllegalAccessException, InvocationTargetException {
        StringBuilder sql = new StringBuilder();
        sql.append(" WHERE ");
        // 获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        int num = 0;
        for (EntityColumn entityColumn : columnSet) {
            if (!entityColumn.isId()) {
                String value = valueToString(entityColumn, whereObj);
                if("NULL".equals(value) || "''".equals(value)) {
                    continue;
                }
                if(num > 0) {
                    sql.append(" AND ");
                }
                sql.append(entityColumn.getColumn()).append("=").append(value);
                num++;
            }
        }
        return sql.toString();
    }

    /**
     * 生成更新的where条件部分SQL语句,根据表主键构建
     */
    private static String wherePKColumns(Class<?> entityClass, Object data)
            throws IllegalAccessException, InvocationTargetException {
        StringBuilder sql = new StringBuilder();
        sql.append(" WHERE ");
        // 获取全部主键列
        Set<EntityColumn> columnSet = EntityHelper.getPKColumns(entityClass);
        int num = 1;
        for (EntityColumn entityColumn : columnSet) {
            String value = valueToString(entityColumn, data);
            sql.append(entityColumn.getColumn()).append("=").append(value);
            if (num < columnSet.size()) {
                sql.append(" AND ");
            }
            num++;
        }
        return sql.toString();
    }

    /**
     * 生成更新值set部分的SQL语句
     */
    private static String updateSetColumns(Class<?> entityClass, Object data, boolean notNull, boolean notEmpty)
            throws IllegalAccessException, InvocationTargetException {
        StringBuilder sql = new StringBuilder();
        sql.append("SET ");
        // 获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnSet) {
            if (!column.isId() && column.isUpdatable()) {
                String valueToString = valueToString(column, data);
                if (notNull) {
                    if ("NULL".equals(valueToString)) {
                        continue;
                    }
                    String notNullStr = getIfNotNull(valueToString, column, notEmpty);
                    if (StringUtils.isNotBlank(notNullStr)) {
                        sql.append(notNullStr).append(",");
                    }
                } else {
                    sql.append(column.getColumn()).append("=").append(valueToString).append(",");
                }
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        return sql.toString();
    }

    /**
     * 获取值为非空字符串的数据
     */
    private static String getIfNotNull(String value, EntityColumn column, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        if (notEmpty) {
            if ("''".equals(value)) {
                return "";
            }
        }
        sql.append(column.getColumn()).append("=").append(value);
        return sql.toString();
    }

    /**
     * 生成更新语句UPDATE 表名 部分的SQL语句
     */
    public static String updateTable(String defaultTableName) {
        return "UPDATE " + defaultTableName + " ";
    }

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(2);
        userInfo.setUserName("李四");
//        UserInfo userInfo2 = new UserInfo();
//        userInfo2.setId(2);
        System.out.println(genInsertSql(UserInfo.class, userInfo));
    }

    /**
     * 根据对象生成insert的SQL语句
     */
    public static String genInsertSql(Class<?> entityClass, Object data) {
        return genInsertSql(true, entityClass, data);
    }

    /**
     * 根据对象生成insert的SQL语句
     * @param withIgnore  是否生成违反唯一约束就跳过的插入语句,就是INSERT IGNORE INTO
     * @param entityClass 对应数据库表实体对象类型
     * @param data        需要赋值SQL新增值的对象数据
     * @return 拼装好的SQL的insert语句脚本
     */
    public static String genInsertSql(boolean withIgnore, Class<?> entityClass, Object data) {
        if (data == null) {
            return "";
        }
        EntityHelper.initEntityNameMap(entityClass, new Config());
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        // 开始拼sql
        StringBuilder sql = new StringBuilder();
        try {
            // 构建INSERT INTO的SQL部分语句
            sql.append(insertIntoTable(withIgnore, entityTable.getName()));
            // 构建表名插入列的SQL部分语句
            sql.append(insertColumns(entityClass, true, false, false));
            // 构建表列值的SQL部分语句
            sql.append(insertValuesColumns(entityClass, true, data));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        sql.append(";");
        return sql.toString();
    }

    /**
     * 构建INSERT INTO的SQL部分语句
     */
    public static String insertIntoTable(boolean withIgnore, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        if (withIgnore) {
            sql.append("INSERT IGNORE INTO ");
        } else {
            sql.append("INSERT INTO ");
        }
        sql.append(defaultTableName);
        sql.append(" ");
        return sql.toString();
    }

    /**
     * 构建表名插入列的SQL部分语句
     * @param entityClass   对应数据库表实体对象类型
     * @param skipId        跳过id列
     * @param notNull       构建列对应值不为null的if标签判断,例如:<if test="sex != null">sex,</if>, notNull为true时,notEmpty参数生效
     * @param notEmpty      表示是否做列对应值不为空字符串的判断, 例如:<if test="sex != null and sex != '' ">sex,</if>, notNull为true时,此参数生效
     * @return 返回构建好的SQL片段语句
     */
    public static String insertColumns(Class<?> entityClass, boolean skipId, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("(");
        // 获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnSet) {
            // 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
            if (!column.isInsertable()) {
                continue;
            }
            // 跳过id列
            if (skipId && column.isId()) {
                continue;
            }
            // 是否根据字段值判断是否构建当前列
            if (notNull) {
                // 构建列对应值不为null的if标签判断,例如:<if test="sex != null">sex,</if>
                // notEmpty参数,表示是否做列对应值不为空字符串的判断, 例如:<if test="sex != null and sex != '' ">sex,</if>
                sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", notEmpty));
            } else {
                // 构建除主键外的所有字段列
                sql.append(column.getColumn()).append(",");
            }
        }
        // 删除多余的","
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }

    /**
     * 构建表列值的SQL部分语句
     */
    public static String insertValuesColumns(Class<?> entityClass, boolean skipId, Object object)
            throws IllegalAccessException, InvocationTargetException {
        StringBuilder sql = new StringBuilder();
        sql.append(" VALUES (");
        // 获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnSet) {
            // 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
            if (!column.isInsertable()) {
                continue;
            }
            // 跳过id列
            if (skipId && column.isId()) {
                continue;
            }
            // 构建字段列的值
            sql.append(valueToString(column, object)).append(",");
        }
        // 删除多余的","
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }

    /**
     * 将值转换成字符串
     */
    public static String valueToString(EntityColumn column, Object object)
            throws IllegalAccessException, InvocationTargetException {
        Object value = column.getEntityField().getValue(object);
        if (value == null) {
            return "NULL";
        }
        StringBuilder sb = new StringBuilder();
        if (value instanceof CharSequence) {
            sb.append("'").append(value).append("'");
        } else if (value instanceof Date) {
            Date date = (Date) value;
            sb.append("'").append(DateUtil.formatDateTime(date)).append("'");
        } else if (value instanceof Number) {
            sb.append(value);
        } else {
            sb.append("'").append(JacksonUtil.toJson(value)).append("'");
        }
        return sb.toString();
    }

    /**
     * SQL新增脚本,如果唯一键或者主键冲突,则更新
     */
    private static final String SQL_SCRIPT_TEMPLATE = "INSERT INTO user_info (user_name, password, name, age, sex, birthday)"
            + " VALUES (%s, %s, %s, %s, %s, %s) ON DUPLICATE KEY UPDATE password = values(password), name = values(name), " +
            "age = values(age), sex = values(sex), birthday = values(birthday)";


    /**
     * 通过脚本模板的方式生成Sql脚本语句
     */
    private String genSqlBySqlScriptTemplate(List<UserInfo> userInfoList) {
        if (CollectionUtils.isEmpty(userInfoList)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        userInfoList.forEach(userInfo -> {
            String formatted = String.format(SQL_SCRIPT_TEMPLATE, userInfo.getUserName(), userInfo.getPassword(),
                    userInfo.getName(), userInfo.getAge(), userInfo.getSex(), userInfo.getBirthday());
            sb.append(formatted).append("\n\n");
        });
        return sb.toString();
    }

}