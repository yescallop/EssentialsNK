package cn.yescallop.essentialsnk.util.duration;


import cn.nukkit.Server;
import cn.yescallop.essentialsnk.EssentialsAPI;

import java.time.Duration;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mulan Lin('Snake1999') on 2016/9/16 15:28.
 */
//Package local class for duration parser. Do NOT use in your plugins.
//This is a class for Chinese number formats and time formats.
//What you only need to know is: it works. So do not try to understand every character inside :)
    //Copyleft: Lin Mulan(林木兰). Zhejiang, China
class LMLDP$Lang$Chinese implements LMLDP$Lang{

    private static String timePattern = "^((.*)纪)?((.*)年)?((.*)月)?((.*)周)?((.*)日)?((.*)时)?((.*)分)?((.*)秒)?((.*)半)?$";
    private static Pattern time = Pattern.compile(timePattern);

    /*
    * 中文日期时间格式说明
    * 数码可以是汉字或阿拉伯数字，整数或分数，“一百三十五”“1234”“半/一半”，后面可以带“个”比如“一个钟头”。
    *                   这样的数字下面简称N
    * 日期格式：（N世纪）（N年）（N月）（N星期/周）（N天/日）（N小时/时/钟头）（N分钟/分）（N秒）
    * 合法的：“三个星期零三天”“半个月”“2天零6分30秒”
    * */
    @Override
    public boolean identify(String s) {
        s = replaceLikeThis(s); //替换！
        //然后思路就简单了
        return time.matcher(s).matches();
    }

    @Override
    public Duration convert(String s) {
        s = replaceLikeThis(s);
        String match = "(((.*)纪)|((.*)年)|((.*)月)|((.*)周)|((.*)日)|((.*)时)|((.*)分)|((.*)秒))";
        Pattern p = Pattern.compile(match);
        Matcher m = p.matcher(s);
        ArrayList<String> cut = new ArrayList<>();
        while (m.find()) cut.add(m.group(1));
        Duration ans = Duration.ZERO;
        for (String a: cut) {
            if (a.length()<=1) continue;
            char c1 = a.charAt(a.length()-1);
            char c2 = a.charAt(a.length()-2);
            Duration unit = Duration.ZERO,part = Duration.ZERO;
            switch (c1) {
                case '纪':unit = unit.plusDays(36500);break;
                case '年':unit = unit.plusDays(365);break;
                case '月':unit = unit.plusDays(30);break;
                case '周':unit = unit.plusDays(7);break;
                case '日':unit = unit.plusDays(1);break;
                case '时':unit = unit.plusHours(1);break;
                case '分':unit = unit.plusMinutes(1);break;
                case '秒':unit = unit.plusSeconds(1);break;
            }
            if (c2=='半') part = part.plus(unit.dividedBy(2));
            String sub = a.substring(0, c2=='半'?a.length()-2:a.length()-1);
            long mul;
            if (isPositiveInteger(sub)) mul = Integer.parseInt(sub);
            else mul = ChineseNumToInteger(sub);
            part = part.plus(unit.multipliedBy(mul));
            ans = ans.plus(part);
        }
        return ans;
    }

    private static Pattern p = Pattern.compile("^[0-9]+$");
    private boolean isPositiveInteger(String a) {
        return p.matcher(a).find();
    }
    private static String replaceLikeThis(String s) {
        s = s.replace("世纪","纪").replace("小时", "时").replace("分钟", "分").replace("秒钟", "秒")
                .replace("星期", "周").replace("钟头","时").replace("天", "日"); //双音节判断不容易，先替换再说
        s = s.replaceAll("^(.*)年半$", "$1半年").replaceAll("^(.*)月半$", "$1半月").replaceAll("^(.*)日半$","$1半日")
                .replaceAll("^(.*)周半$","$1半周").replaceAll("^(.*)时半$","$1半时").replaceAll("^(.*)分半$","$1半分");
        s = s.replace("个","").replace("两","二"); //汉语博大精深
        return s;
    }
    //http://www.cnblogs.com/bsping/p/4514471.html
    @SuppressWarnings("unused")
    private static int ChineseNumToInteger(String chineseNumber){
        int result = 0;
        int temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'一','二','三','四','五','六','七','八','九'};
        char[] chArr = new char[]{'十','百','千','万','亿'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if(0 != count){//添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if(b){//单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:temp *= 10;break;
                            case 1:temp *= 100;break;
                            case 2:temp *= 1000;break;
                            case 3:temp *= 10000;break;
                            case 4:temp *= 100000000;break;
                            default:break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }
}
