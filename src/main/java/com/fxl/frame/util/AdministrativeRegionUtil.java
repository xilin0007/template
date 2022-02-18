package com.fxl.frame.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description 国家行政区工具类
 * @author fangxilin
 * @date 2020-11-16
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class AdministrativeRegionUtil {

    private static final String FILE_DIR = "/Users/fangxilin/Desktop/work/研发需求/二院-入院登记接口增加传入的地区code（59813）/";

    static File regionNyFile = new File(FILE_DIR + "宁远-区域四级划分.json");

    static File provinceNatFile = new File(FILE_DIR + "国标-省.txt");

    static File cityNatFile = new File(FILE_DIR + "国标-市.txt");

    static File countyNatFile = new File(FILE_DIR + "国标-县.txt");

    static File regionNatFile = new File(FILE_DIR + "国标-区域四级划分.txt");

    static File overseasNatFile = new File(FILE_DIR + "国标-海外.txt");

    static File finalNotMatchedFile = new File(FILE_DIR + "未匹配到的宁远区域码列表.txt");

    static File administrativeSqlFile = new File(FILE_DIR + "administrative_region_code.sql");

    /**
     * 生成国标code及宁远code
     */
    public static void generateNationalCode() throws IOException {
        String regionNyStr = FileUtils.readFileToString(regionNyFile, "UTF-8");
        JSONArray regionNyJson = JSON.parseObject(regionNyStr).getJSONArray("data");
//        System.out.println(regionNyJson.getJSONObject(0).getString("label"));
        String provinceNatStr = FileUtils.readFileToString(provinceNatFile, "UTF-8");
        String cityNatStr = FileUtils.readFileToString(cityNatFile, "UTF-8");
        List<String> countyNatArr = FileUtils.readLines(countyNatFile, "UTF-8");
        List<String> regionNatAllArr = FileUtils.readLines(regionNatFile, "UTF-8");
        String overseasNatStr = FileUtils.readFileToString(overseasNatFile, "UTF-8");


        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO administrative_region_code(area_level, area_name, parent_ny_code, ny_code, ny_code_path, parent_national_code, national_code, national_code_path, is_show) VALUES \n");
        //第一次过滤-未匹配到的宁远区域码列表，// TODO 回收机制，再一次与 regionNatFile 进行匹配，最终没匹配上的，才打印到文件上
        List<String> notMatchedList = new ArrayList<>();

        for (int i = 0; i < regionNyJson.size(); i++) {
            //一级区域等级-省、自治区、直辖市
            JSONObject provinceNy = regionNyJson.getJSONObject(i);
            if ("1,6144,".equals(provinceNy.getString("idPath"))) { //跳过海外，海外的在后面单独处理
                continue;
            }
            String nationalCodePath1 = produceFileContent(provinceNatStr, sql, notMatchedList, 2, "1", provinceNy);
            //获取二级区域-市
            JSONArray cityNyArr = provinceNy.getJSONArray("children");
            for (int i1 = 0; i1 < cityNyArr.size(); i1++) {
                JSONObject cityNy = cityNyArr.getJSONObject(i1);
                String nationalCodePath2 = produceFileContent(cityNatStr, sql, notMatchedList, 3, nationalCodePath1, provinceNy, cityNy);
                if (StringUtils.isEmpty(nationalCodePath2)) {
                    continue;
                }
                //获取三级区域-县、区
                JSONArray countyNyArr = cityNy.getJSONArray("children");
                if (countyNyArr == null || countyNyArr.size() == 0) {
                    System.out.println(cityNy.getString("label") + "\t" + cityNy.getString("idPath") + "\t" +
                            provinceNy.getString("label") + cityNy.getString("label")
                            + "\t暂无子节点，直接跳过！");
                    continue;
                }
                for (int i2 = 0; i2 < countyNyArr.size(); i2++) {
                    JSONObject countyNy = countyNyArr.getJSONObject(i2);
                    String nationalCodePath3 = produceFileContentV2(countyNatArr, sql, notMatchedList, 4, nationalCodePath2, provinceNy, cityNy, countyNy);
                    if (StringUtils.isEmpty(nationalCodePath3)) {
                        continue;
                    }
                    //获取4级区域-街道、镇
                    JSONArray townNyArr = countyNy.getJSONArray("children");
                    if (townNyArr == null || townNyArr.size() == 0) {
                        continue;
                    }
                    for (int i3 = 0; i3 < townNyArr.size(); i3++) {
                        JSONObject townNy = townNyArr.getJSONObject(i3);
                        produceFileContentV3(regionNatAllArr, sql, notMatchedList, 5, nationalCodePath3, provinceNy, cityNy, countyNy, townNy);
                    }
                }
            }
        }

        //处理海外的
        for (int i = 0; i < regionNyJson.size(); i++) {
            JSONObject overseasNys = regionNyJson.getJSONObject(i);
            if ("1,6144,".equals(overseasNys.getString("idPath"))) {
                JSONArray overseasNyArr = overseasNys.getJSONArray("children");
                for (int i1 = 0; i1 < overseasNyArr.size(); i1++) {
                    JSONObject overseasNy = overseasNyArr.getJSONObject(i1);
                    String areaName = overseasNy.getString("label");
                    //正则匹配
                    String regex = "\n(.*" + areaName + "+?.*)\n";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher mat = pattern.matcher(overseasNatStr);
                    String parentNyCode = overseasNy.getString("idPath");
                    parentNyCode = parentNyCode.substring(0, parentNyCode.length() - 1);
                    if (mat.find()) {
                        String[] overseasNatObj = mat.group(1).split("\t");
                        String nationalCodePath = overseasNatObj[0];
                        sql.append(String.format("(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d),\n",
                                3, areaName, overseasNy.getString("parentId"), overseasNy.getString("value"), parentNyCode, "1", nationalCodePath, nationalCodePath, 1));
                    } else {
                        //未匹配到
                        if (!"海外".equals(areaName)) {
                            String text = areaName + "\t" + overseasNy.getString("parentId") + "\t" + overseasNy.getString("value") + "\t" + parentNyCode + "\t"
                                    + overseasNys.getString("label") + "," + overseasNy.getString("label");
                            notMatchedList.add(text);
                        }
                    }

                    //第二层海外
                    JSONArray overseasNyArr1 = overseasNy.getJSONArray("children");
                    if (overseasNyArr1 == null || overseasNyArr1.size() == 0) {
                        continue;
                    }
                    for (int i2 = 0; i2 < overseasNyArr1.size(); i2++) {
                        JSONObject overseasNy1 = overseasNyArr1.getJSONObject(i2);
                        String areaName1 = overseasNy1.getString("label");
                        //适配 美国全区 => 美国
                        String tempAreaName1 = areaName1.replace("全区", "");
                        //正则匹配
                        String regex1 = "\n(.*" + tempAreaName1 + "+?.*)\n";
                        Pattern pattern1 = Pattern.compile(regex1);
                        Matcher mat1 = pattern1.matcher(overseasNatStr);
                        String parentNyCode1 = overseasNy1.getString("idPath");
                        parentNyCode1 = parentNyCode1.substring(0, parentNyCode1.length() - 1);
                        if (mat1.find()) {
                            String[] overseasNatObj = mat1.group(1).split("\t");
                            String nationalCodePath = overseasNatObj[0];
                            sql.append(String.format("(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d),\n",
                                    4, areaName1, overseasNy1.getString("parentId"), overseasNy1.getString("value"), parentNyCode1, "1", nationalCodePath, nationalCodePath, 1));
                        } else {
                            //未匹配到
                            String text = areaName1 + "\t" + overseasNy1.getString("parentId") + "\t" + overseasNy1.getString("value") + "\t" + parentNyCode1 + "\t"
                                    + overseasNys.getString("label") + "," + overseasNy.getString("label") + "," + overseasNy1.getString("label");
                            notMatchedList.add(text);
                        }
                    }

                }
                break;
            }
        }

        //第二层过滤匹配
        List<String> finalNotMatchedList = new ArrayList<>();
        String title = "区域名\t父区域code\t区域code\t区域code全路径\t区域名全路径\t国标-父区域code";
        finalNotMatchedList.add(title);
        notMatchedList.forEach(s -> {
            String[] record = s.split("\t");
            if (record.length < 6) {
                finalNotMatchedList.add(s);
                return; //跳过一次forEach循环
            }
//            try {
//                String parentNationalCode = record[5];
//            } catch (Exception e) {
//                String msg = s + "匹配异常！";
//                throw new RuntimeException(msg, e);
//            }

            String areaName = record[0];
            String parentNationalCode = record[5];
            String nationalCode = "";
            String nationalCodePath = "";
            if (parentNationalCode.length() == 2) { //父级为一级，当前为二级
                for (String regionNat : regionNatAllArr) {
                    String[] regionNatObj = regionNat.split("\t");
                    if (regionNatObj[0].startsWith(parentNationalCode) &&
                            ((regionNatObj.length > 3 && regionNatObj[3].contains("@") && regionNatObj[3].contains(areaName))
                                || (regionNatObj.length > 4 && regionNatObj[4].contains("@") && regionNatObj[4].contains(areaName)))

                    ) {
//                            && (regionNatObj.length <= 4 || StringUtils.isEmpty(regionNatObj[4].trim()))) {
                        nationalCodePath = regionNatObj[0];
                        nationalCode = nationalCodePath.substring(2, 9);
                        break;
                    }
                }
            } else {  //当前为三、四级
                //适配 莞城区 => 广东省东莞市莞城街道办事处
                String tempAreaName = areaName.length() > 2 ? areaName.replace("区", "").replace("市", "").replace("县", "") : areaName;
                for (String regionNat : regionNatAllArr) {
                    if (regionNat.startsWith(parentNationalCode) && regionNat.contains(tempAreaName)) {
                        nationalCodePath = regionNat.split("\t")[0];
                        nationalCode = nationalCodePath.substring(parentNationalCode.length(), 9);
                        break;
                    }
                }
            }

            if (StringUtils.isNotEmpty(nationalCodePath)) {
                int areaLevel = 3; //3表示二级
                if (parentNationalCode.length() == 4) {
                    areaLevel = 4;
                } else if (parentNationalCode.length() == 6) {
                    areaLevel = 5;
                }
                sql.append(String.format("(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d),\n",
                        areaLevel, areaName, record[1], record[2], record[3], parentNationalCode, nationalCode, nationalCodePath, 1));
            } else {
                finalNotMatchedList.add(s);
            }
        });


        //第三层过滤匹配，针对nationalCode不对的情况 北京,北京,密云区	1101 => 1102的情况
        for (int i = 1; i < finalNotMatchedList.size(); i++) {
            String[] record = finalNotMatchedList.get(i).split("\t");
            if (record.length < 6) {
                continue; //跳过一次forEach循环
            }

            String areaName = record[0];
            String parentNationalCode = record[5];
            parentNationalCode = parentNationalCode.substring(0, 2); //值判断前两位
            String nationalCode = "";
            String nationalCodePath = "";
            //适配 密云区 => 密云县
            String tempAreaName = areaName.length() > 2 ? areaName.replace("区", "").replace("市", "").replace("县", "") : areaName;
            for (String regionNat : regionNatAllArr) {
                String[] regionNatObj = regionNat.split("\t");
                if (regionNatObj[0].startsWith(parentNationalCode) &&
                        ((regionNatObj[3].contains("@") && regionNatObj[3].contains(tempAreaName)) || (regionNatObj.length >=5 && regionNatObj[4].contains("@") && regionNatObj[4].contains(tempAreaName)))) {
                    nationalCodePath = regionNatObj[0];
                    parentNationalCode = nationalCodePath.substring(0, 4);
                    nationalCode = nationalCodePath.substring(4, 9);
                    break;
                }
            }

            if (StringUtils.isNotEmpty(nationalCodePath)) {
                sql.append(String.format("(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d),\n",
                        4, areaName, record[1], record[2], record[3], parentNationalCode, nationalCode, nationalCodePath, 1));
                System.out.println(finalNotMatchedList.get(i));
                finalNotMatchedList.remove(i);
                i--; //必须的，否则会跳过下一个
            }
        }





        //输出sql文件
        String sqlStr = sql.substring(0, sql.length() - 2) + ";";
        FileUtils.writeStringToFile(administrativeSqlFile, sqlStr, "UTF-8");
        //打印未匹配到的记录
        FileUtils.writeLines(finalNotMatchedFile, "UTF-8", finalNotMatchedList);

        System.out.println("生成国标code及宁远code成功！");
    }

    /**
     * 适用于四级别区域
     */
    private static String produceFileContentV3(List<String> regionNatArr, StringBuilder sql, List<String> notMatchedList,
                                               int areaLevel, String parentNationalCode, JSONObject... regionNyObj) {

        String nationalCodePath = "";
        int idx = regionNyObj.length - 1;
        String areaName = regionNyObj[idx].getString("label");
        String nyCode = regionNyObj[idx].getString("value");
        String parentNyCode = regionNyObj[idx].getString("parentId");
        String nyCodePath = regionNyObj[idx].getString("idPath");
        nyCodePath = nyCodePath.endsWith(",") ? nyCodePath.substring(0, nyCodePath.length() - 1) : nyCodePath; //去掉尾部的逗号 1,2, => 1,2
        for (String regionNat : regionNatArr) {
            //包含parentNationalCode且区域名相匹配
            if (regionNat.startsWith(parentNationalCode) && regionNat.contains(areaName)) {
                String[] regionNatObj = regionNat.split("\t");
                nationalCodePath = regionNatObj[0];
                //String nationalAreaName = provinceNatObj[1];
                break;
            }
        }
        try {
            String nationalCode = "";
            if (StringUtils.isNotEmpty(nationalCodePath)) {
                nationalCode = nationalCodePath.substring(6, 9);
                sql.append(String.format("(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d),\n",
                        areaLevel, areaName, parentNyCode, nyCode, nyCodePath, parentNationalCode, nationalCode, nationalCodePath, 1));
            } else {
                //未匹配到
                String text = areaName + "\t" + nyCode + "\t" + parentNyCode + "\t" + nyCodePath + "\t";
                Stream<String> areaNameStream = Arrays.stream(regionNyObj).flatMap(jsonObject -> {
                    String s = jsonObject.getString("label");
                    return Stream.of(s);
                });
                String areaNamePath = areaNameStream.collect(Collectors.joining(","));
                text += areaNamePath + "\t" + parentNationalCode;
                notMatchedList.add(text);
            }
            return nationalCodePath;
        } catch (Exception e) {
            String msg = areaName + "\t" + parentNyCode + "\t" + nyCode + "\t" + nyCodePath + "\t" + "匹配异常！";
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * 适用于三级别区域
     */
    private static String produceFileContentV2(List<String> regionNatArr, StringBuilder sql, List<String> notMatchedList,
                                               int areaLevel, String parentNationalCode, JSONObject... regionNyObj) {

        String nationalCodePath = "";
        int idx = regionNyObj.length - 1;
        String areaName = regionNyObj[idx].getString("label");
        String nyCode = regionNyObj[idx].getString("value");
        String parentNyCode = regionNyObj[idx].getString("parentId");
        String nyCodePath = regionNyObj[idx].getString("idPath");
        nyCodePath = nyCodePath.endsWith(",") ? nyCodePath.substring(0, nyCodePath.length() - 1) : nyCodePath; //去掉尾部的逗号 1,2, => 1,2
        for (String regionNat : regionNatArr) {
            //适配 赣　县 => 赣县
            areaName = areaName.replace("　", "").replace("\t", "").replace(" ", "");
            //适配 贡山自治县 => 云南省怒江傈僳族自治州贡山独龙族怒族自治
            String tempAreaName = (areaName.contains("自治县")) ? areaName.substring(0, 2) : areaName;
            //适配 增城区 => 增城市，潮安区 => 潮安县
            tempAreaName = (tempAreaName.length() > 2) ? tempAreaName.replace("区", "").replace("市", "").replace("县", "") : tempAreaName;
            //包含parentNationalCode且区域名相匹配
            if (regionNat.startsWith(parentNationalCode) && regionNat.contains(tempAreaName)
                    || ("5001".equals(parentNationalCode) && regionNat.contains(tempAreaName))) { //重庆市包含5002、5003等情况，需额外处理
                String[] regionNatObj = regionNat.split("\t");
                if (regionNatObj[0].length() == 6) {
                    nationalCodePath = regionNatObj[0];
                    //String nationalAreaName = provinceNatObj[1];
                    break;
                }
            }
        }
        try {
            String nationalCode = "";
            if (StringUtils.isNotEmpty(nationalCodePath)) {
                nationalCode = nationalCodePath.substring(4, 6);
                sql.append(String.format("(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d),\n",
                        areaLevel, areaName, parentNyCode, nyCode, nyCodePath, parentNationalCode, nationalCode, nationalCodePath, 1));
            } else {
                //未匹配到
                String text = areaName + "\t" + nyCode + "\t" + parentNyCode + "\t" + nyCodePath + "\t";
                Stream<String> areaNameStream = Arrays.stream(regionNyObj).flatMap(jsonObject -> {
                    String s = jsonObject.getString("label");
                    return Stream.of(s);
                });
                String areaNamePath = areaNameStream.collect(Collectors.joining(","));
                text += areaNamePath + "\t" + parentNationalCode;
                notMatchedList.add(text);

                //未匹配到3级的情况，3级下的所有4级区域都应该记录下来
                recordNotMachedCountyTown(notMatchedList, regionNyObj[idx], areaNamePath, parentNationalCode);
            }
            return nationalCodePath;
        } catch (Exception e) {
            String msg = areaName + "\t" + parentNyCode + "\t" + nyCode + "\t" + nyCodePath + "\t" + "匹配异常！";
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * 未匹配到3级的情况，3级下的所有4级区域都应该记录下来
     */
    private static void recordNotMachedCountyTown(List<String> notMatchedList, JSONObject regionNyObj, String parentAreaNamePath, String parentNationalCode) {
        JSONArray townArr = regionNyObj.getJSONArray("children");
        if (townArr == null || townArr.size() == 0) {
            return;
        }
        for (int i = 0; i < townArr.size(); i++) {
            JSONObject townNyobj = townArr.getJSONObject(i);
            String areaName = townNyobj.getString("label");
            String nyCode = townNyobj.getString("value");
            String parentNyCode = townNyobj.getString("parentId");
            String nyCodePath = townNyobj.getString("idPath");
            nyCodePath = nyCodePath.endsWith(",") ? nyCodePath.substring(0, nyCodePath.length() - 1) : nyCodePath; //去掉尾部的逗号 1,2, => 1,2
            String areaNamePath = parentAreaNamePath + "," + areaName;
            String text = areaName + "\t" + nyCode + "\t" + parentNyCode + "\t" + nyCodePath + "\t" +
                    areaNamePath + "\t" + parentNationalCode;
            notMatchedList.add(text);

        }
    }


    /**
     * 适用于一、二级别区域，三、四区域由于会出现重名情况，故不适用
     */
    private static String produceFileContent(String regionNatStr, StringBuilder sql, List<String> notMatchedList, int areaLevel, String parentNationalCode, JSONObject... regionNyObj) {
        String nationalCodePath = "";
        int idx = regionNyObj.length - 1;
        String areaName = regionNyObj[idx].getString("label");
        String nyCode = regionNyObj[idx].getString("value");
        String parentNyCode = regionNyObj[idx].getString("parentId");
        String nyCodePath = regionNyObj[idx].getString("idPath");
        nyCodePath = nyCodePath.endsWith(",") ? nyCodePath.substring(0, nyCodePath.length() - 1) : nyCodePath; //去掉尾部的逗号 1,2, => 1,2
        //正则匹配
        String regex1 = "\n(.*" + areaName + "+?.*)\n";
        if (areaLevel == 3 && areaName.length() > 2) {
            //替换掉洲，防止 大理州 这种匹配不上 云南省大理白族自治州
            //湘西自治州 => 湖南省湘西土家族苗族自治州
            regex1 = "\n(.*" + areaName.replace("自治州", "").replace("州", "") + "+?.*)\n";
        }


        //适配 吉林省吉林 => 吉林省吉林市
        if (areaLevel == 3 && areaName.equals("吉林")) {
            regex1 = "\n(.*" + areaName + "市" + "+?.*)\n";
        }
        //适配 海南省 => 青海省海南藏族自治州
        if (areaLevel == 3 && areaName.equals("海南")) {
            regex1 = "\n(.*" + "青海省" + areaName + "+?.*)\n";
        }

        Pattern pattern1 = Pattern.compile(regex1);
        Matcher mat1 = pattern1.matcher(regionNatStr);
        if (mat1.find()) {
            String[] regionNatObj = mat1.group(1).split("\t");
            nationalCodePath = regionNatObj[0];
//                String nationalAreaName = provinceNatObj[1];

            String nationalCode = "";
//            String parentNationalCode = "";
            switch (areaLevel) {
                case 2:
                    parentNationalCode = "1"; //一级区域默认父级为1
                    nationalCode = nationalCodePath; //一级区域nationalCodePath与nationalCode相等
                    break;
                case 3:
//                    parentNationalCode = nationalCodePath.substring(0, 2);
                    nationalCode = nationalCodePath.substring(2, 4);
                    break;
                default:
                    parentNationalCode = "1";
                    nationalCode = nationalCodePath;
                    break;
            }

            sql.append(String.format("(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d),\n",
                    areaLevel, areaName, parentNyCode, nyCode, nyCodePath, parentNationalCode, nationalCode, nationalCodePath, 1));
        } else {
            //未匹配到
            String text = areaName + "\t" + parentNyCode + "\t" + nyCode + "\t" + nyCodePath + "\t";
            Stream<String> areaNameStream = Arrays.stream(regionNyObj).flatMap(jsonObject -> {
                String s = jsonObject.getString("label");
                return Stream.of(s);
            });
            String areaNamePath = areaNameStream.collect(Collectors.joining(","));
            text += areaNamePath + "\t" + parentNationalCode;
            notMatchedList.add(text);
        }
        return nationalCodePath;
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
//        unifyStandard();
        generateNationalCode();
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + ((end - start)) + "毫秒");


    }

    /**
     * 统一标准
     */
    public static void unifyStandard() throws IOException {
//        String provinceNatStr = FileUtils.readFileToString(provinceNatFile, "UTF-8");
//        String provinceNatReplace = provinceNatStr.replace("省", "").replace("市", "").replace("自治区", "").replace("特别行政区", "")
//                .replace("壮族", "").replace("回族", "").replace("维吾尔", "");
//        FileUtils.writeStringToFile(provinceNatFile, provinceNatReplace, "UTF-8");

//        String cityNatStr = FileUtils.readFileToString(cityNatFile, "UTF-8");
//        String cityNatStrReplace = cityNatStr.replace("省", "").replace("市", "").replace("自治区", "").replace("特别行政区", "")
//                .replace("壮族", "").replace("回族", "").replace("维吾尔", "");
//        FileUtils.writeStringToFile(cityNatFile, cityNatStrReplace, "UTF-8");


        String countyNatStr = FileUtils.readFileToString(countyNatFile, "UTF-8");
        String countyNatStrReplace = countyNatStr.replace("重庆市所属市", "重庆市");
        FileUtils.writeStringToFile(countyNatFile, countyNatStrReplace, "UTF-8");


        System.out.println("统一标准完成！");

    }

}
