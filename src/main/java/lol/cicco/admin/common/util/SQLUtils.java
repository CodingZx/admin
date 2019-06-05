package lol.cicco.admin.common.util;

import com.google.common.base.Strings;

public class SQLUtils {

    /**
     * 前后模糊查询
     * @param condition 查询条件
     * @return '%' + condition + '%'
     */
    public static String fuzzyAll(String condition) {
        if (Strings.isNullOrEmpty(condition)) {
            return null;
        }
        return "%" + condition + "%";
    }

    /**
     * 前模糊查询
     * @param condition 查询条件
     * @return '%' + condition
     */
    public static String fuzzyLeft(String condition) {
        if (Strings.isNullOrEmpty(condition)) {
            return null;
        }
        return "%" + condition;
    }

    /**
     * 后模糊查询
     * @param condition 查询条件
     * @return condition + '%'
     */
    public static String fuzzyRight(String condition) {
        if (Strings.isNullOrEmpty(condition)) {
            return null;
        }
        return condition + "%";
    }
}
