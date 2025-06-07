package cc.wangzijie.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字符串拆分工具
 */
public class StringSplitUtils {

    /**
     * 字符串根据固定长度拆分为列表
     *
     * @param src    字符串
     * @param length 固定长度
     * @return 拆分列表
     */
    public static List<String> splitByLen(String src, int length) {
        //检查参数是否合法
        if (null == src || src.equals("")) {
            return Collections.emptyList();
        }

        if (length <= 0) {
            return Collections.emptyList();
        }
        //获取整个字符串可以被切割成字符子串的个数
        int n = (src.length() + length - 1) / length;
        List<String> list = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (i < (n - 1)) {
                list.add(src.substring(i * length, (i + 1) * length));
            } else {
                list.add(src.substring(i * length));
            }
        }
        return list;
    }


    /**
     * 将列表合并为字符串, 中间用英文逗号分隔
     *
     * @param values 待合并的列表
     * @return 合并后的字符串
     */
    public static String joinWithComma(Collection<Long> values) {
        return join(",", values);
    }


    /**
     * 将列表合并为字符串
     *
     * @param delimiter 合并间隔符
     * @param values    待合并的列表
     * @return 合并后的字符串
     */
    public static String join(String delimiter, Collection<Long> values) {
        if (CollectionUtils.isEmpty(values)) {
            return "";
        }
        return values.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    /**
     * 将字符串input按照英文逗号拆分，默认过滤掉空字符串
     *
     * @param input 输入字符串
     * @return 拆分结果列表
     */
    public static List<String> splitToListByComma(String input) {
        return splitToList(input, ",");
    }


    /**
     * 将字符串input按照splitBy拆分，默认过滤掉空字符串
     *
     * @param input   输入字符串
     * @param splitBy 拆分分隔符
     * @return 拆分结果列表
     */
    public static List<String> splitToList(String input, String splitBy) {
        return splitToList(input, splitBy, true);
    }


    /**
     * 将字符串input按照splitBy拆分
     *
     * @param input       输入字符串
     * @param splitBy     拆分分隔符
     * @param filterBlank 是否过滤掉空字符串
     * @return 拆分结果列表
     */
    public static List<String> splitToList(String input, String splitBy, boolean filterBlank) {
        if (input == null) {
            return new ArrayList<>();
        }
        String[] parts = input.split(splitBy);
        return filterBlank
                ? Arrays.stream(parts).filter(StringUtils::isNotBlank).collect(Collectors.toList())
                : Arrays.asList(parts);
    }


    /**
     * (ID列表)将字符串input按照英文逗号拆分，默认过滤掉空字符串
     *
     * @param input 输入字符串
     * @return 拆分结果列表
     */
    public static List<Long> splitToIdListByComma(String input) {
        return splitToIdList(input, ",");
    }


    /**
     * (ID列表)将字符串input按照splitBy拆分，默认过滤掉空字符串
     *
     * @param input   输入字符串
     * @param splitBy 拆分分隔符
     * @return 拆分结果列表
     */
    public static List<Long> splitToIdList(String input, String splitBy) {
        return splitToIdList(input, splitBy, true);
    }


    /**
     * (ID列表)将字符串input按照splitBy拆分
     *
     * @param input       输入字符串
     * @param splitBy     拆分分隔符
     * @param filterBlank 是否过滤掉空字符串
     * @return 拆分结果列表
     */
    public static List<Long> splitToIdList(String input, String splitBy, boolean filterBlank) {
        if (input == null) {
            return new ArrayList<>();
        }
        String[] parts = input.split(splitBy);
        return !filterBlank
                ? Arrays.stream(parts).map(Long::parseLong).collect(Collectors.toList())
                : Arrays.stream(parts).filter(StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toList());
    }


    /**
     * 将字符串input按照英文逗号拆分，默认过滤掉空字符串
     *
     * @param input 输入字符串
     * @return 拆分结果列表
     */
    public static Set<String> splitToSetByComma(String input) {
        return splitToSet(input, ",");
    }


    /**
     * 将字符串input按照splitBy拆分，默认过滤掉空字符串
     *
     * @param input   输入字符串
     * @param splitBy 拆分分隔符
     * @return 拆分结果列表
     */
    public static Set<String> splitToSet(String input, String splitBy) {
        return splitToSet(input, splitBy, true);
    }


    /**
     * 将字符串input按照splitBy拆分
     *
     * @param input       输入字符串
     * @param splitBy     拆分分隔符
     * @param filterBlank 是否过滤掉空字符串
     * @return 拆分结果列表
     */
    public static Set<String> splitToSet(String input, String splitBy, boolean filterBlank) {
        if (input == null) {
            return new HashSet<>();
        }
        String[] parts = input.split(splitBy);
        return filterBlank
                ? Arrays.stream(parts).filter(StringUtils::isNotBlank).collect(Collectors.toSet())
                : new HashSet<>(Arrays.asList(parts));
    }

    /**
     * (ID列表)将字符串input按照英文逗号拆分，默认过滤掉空字符串
     *
     * @param input 输入字符串
     * @return 拆分结果列表
     */
    public static Set<Long> splitToIdSetByComma(String input) {
        return splitToIdSet(input, ",");
    }

    /**
     * (ID列表)将字符串input按照splitBy拆分
     *
     * @param input   输入字符串
     * @param splitBy 拆分分隔符
     * @return 拆分结果列表
     */
    public static Set<Long> splitToIdSet(String input, String splitBy) {
        if (input == null) {
            return new HashSet<>();
        }
        String[] parts = input.split(splitBy);
        return Arrays.stream(parts)
                .map(String::trim)
                .filter(NumberUtils::isCreatable)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    public static Map<Long, String> splitToIdNameMapByComma(String ids, String names) {
        return splitToIdNameMap(ids, names, ",", ",");
    }

    public static Map<Long, String> splitToIdNameMap(String ids, String names, String idSplitBy, String nameSplitBy) {
        Map<Long, String> idNameMap = new HashMap<>();
        if (ids == null) {
            return idNameMap;
        }
        String[] idArr = ids.split(idSplitBy);
        if (idArr.length == 0) {
            return idNameMap;
        }
        names = NullValueUtils.onNull(names, "");
        String[] nameArr = names.split(nameSplitBy);
        int idNum = idArr.length;
        int nameNum = nameArr.length;
        for (int i=0; i<idNum; i++) {
            String idStr = idArr[i];
            String name = i < nameNum ? nameArr[i] : "";
            if (StringUtils.isNotBlank(idStr) && NumberUtils.isCreatable(idStr)) {
                Long id = Long.parseLong(idStr);
                idNameMap.put(id, name);
            } else {
                String noIdNames = idNameMap.get(0L);
                noIdNames = noIdNames == null ? name : noIdNames + nameSplitBy + name;
                idNameMap.put(0L, noIdNames);
            }
        }
        return idNameMap;
    }

    public static Map<String, String> splitToKeyValueMapByComma(String keys, String values) {
        return splitToKeyValueMap(keys, values, ",", ",");
    }


    public static Map<String, String> splitToKeyValueMap(String keys, String values, String keySplitBy, String valueSplitBy) {
        Map<String, String> keyValueMap = new HashMap<>();
        if (keys == null) {
            return keyValueMap;
        }
        String[] keyArr = keys.split(keySplitBy);
        if (keyArr.length == 0) {
            return keyValueMap;
        }
        values = NullValueUtils.onNull(values, "");
        String[] valueArr = values.split(valueSplitBy);
        int keyNum = keyArr.length;
        int valueNum = valueArr.length;
        for (int i=0; i<keyNum; i++) {
            String key = keyArr[i];
            String value = i < valueNum ? valueArr[i] : "";
            if (StringUtils.isNotBlank(key)) {
                keyValueMap.put(key, value);
            } else {
                String noKeyValues = keyValueMap.get("");
                noKeyValues = noKeyValues == null ? value : noKeyValues + valueSplitBy + value;
                keyValueMap.put(value, noKeyValues);
            }
        }
        return keyValueMap;
    }
}
