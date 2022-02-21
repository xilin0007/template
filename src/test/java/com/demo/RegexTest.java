package com.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 正则匹配
 * @Description TODO
 * @author fangxilin
 * @date 2018年10月26日
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2018
 */
public class RegexTest {

    public static void main(String[] args) {

         /*String s = "\\\\192.168.0.17\\US$\\Report\\44740189374090427.pdf";
        System.out.println(s);
        System.out.println(StringEscapeUtils.escapeJava(s));*/


        /**
         * ()表示分组，
         * + 一次或多次匹配前面的字符或子表达式
         * . 匹配除"\r\n"之外的任何单个字符
         * * 零次或多次匹配前面的字符或子表达式
         * ? 零次或一次匹配前面的字符或子表达式
         * *? 如果 ? 是限定符 * 或 ? 或 {} 后面的第一个字符,那么表示非贪婪模式(尽可能少的匹配字符),而不是默认的贪婪模式
         * \ 将下一字符标记为特殊字符、文本、反向引用或八进制转义符
         * 在匹配 . 或 { 或 [ 或 ( 或 ? 或 $ 或 ^ 或 * 这些特殊字符时,需要在前面加上 \\
         * 比如匹配 . 时,Java 中要写为 \\.,但对于正则表达式来说就是 \.
         * 在匹配 \ 时,Java 中要写为 \\\\,但对于正则表达式来说就是 \\
         * $ 匹配输入字符串结尾的位置
         *
         * \s 匹配任何空白字符，包括空格、制表符、换页符等。与 [ \f\n\r\t\v] 等效
         */

//        tanlanMoS();

//        String content = "https://yzs.yygr.cn/ywz/#/interrogation-fail?name={t_name}&age={t_age}";
//        String regex = "\\{(.*?)\\}";

//        String content = "ftp://192.168.0.17/2018-10-23/1005420181023245812008.pdf";
//        String regex = "^(ftp://)+";

//        String content = "https://wxis.91160.com/wxis/act/order/physicalExamOrderPage.do?unit_id=140&code=aadfsd232aa&";
//        String regex = "&code=(.*?)&"; //匹配 &code= 和 & 之间的内容

//        String content = "https://wxis.91160.com/wxis/act/order/physicalExamOrderPage.do?unit_id=140&code=aadfsd232aa";
//        String regex = "&code=(.*?)$"; //匹配某字符串到结尾后的内容

//        String content =
//                "\n" +
//                "44\t广东省\n" +
//                "45\t广西\n" +
//                "46\t广东省深圳市\n" +
//                "\n";
//        String regex = "\n(.*广东+?.*)\n";


//        String content = "<DATA><METHOD>pay_Comm_RefundFee</METHOD><UNITID>200031755</UNITID><TIME>时间</TIME><BEAN><REFUND_INFO><HIS_PAY_NO>LD28594900</HIS_PAY_NO><PAY_TRADE_NO>4200001220202109153122127699</PAY_TRADE_NO><PAY_AMT>2500</PAY_AMT><REFUND_TIME>2021-09-15 11:00:27</REFUND_TIME><REFUND_TYPE>1</REFUND_TYPE><FEE_MAN>6512</FEE_MAN><PAY_METHOD>weixinBarcode</PAY_METHOD></REFUND_INFO></BEAN></DATA>";
        //匹配 <PAY_AMT>和</PAY_AMT>之间的内容
//        String regex = "<PAY_AMT>(.*?)</PAY_AMT>";


        String content = "<Output xmlns=\"\"><Return>1</Return><ErrorMessage /><PatientId>4060883</PatientId></Output>";
        //匹配 <PAY_AMT>和</PAY_AMT>之间的内容
        String regex = "<Output xmlns=\"\">(.*?)</Output>";


        test(content, regex);



    }

    public static void test(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher mat = pattern.matcher(content);
//        if (mat.find()) {
//            System.out.println(mat.group(0)); //0，匹配分组内外的所有内容
//            System.out.println(mat.group(1)); //1，匹配()分组内的内容
//            //&code=aadfsd232aa& => &
////            content = content.replace(mat.group(0), "&");
////            System.out.println(content);
//        }
        while(mat.find()) {
            for (int i = 0; i <= mat.groupCount(); i++) {
//                content = content.replace(mat.group(0), "<PAY_AMT>25.0</PAY_AMT>");
//                System.out.println(content);
                System.out.println(i + ":" + mat.group(i));
            }
        }
    }


    /**
     * 贪婪模式与非贪婪模式
     */
    public static void tanlanMoS() {
        String content = "<div>文章标题</div><div>发布时间</div>";
        // 贪婪模式
        String regex = "<div>(?<title>.+)</div>";
        // 非贪婪模式
//        String regex = "<div>(?<title>.+?)</div>";
        Pattern pattern = Pattern.compile(regex);
        Matcher mat = pattern.matcher(content);
        while(mat.find()) {
            System.out.println(mat.group("title"));
        }
    }

}
