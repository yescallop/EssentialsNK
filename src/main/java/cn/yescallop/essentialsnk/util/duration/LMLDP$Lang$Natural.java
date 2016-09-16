package cn.yescallop.essentialsnk.util.duration;

import java.time.Duration;
import java.util.IntSummaryStatistics;
import java.util.regex.Pattern;

/**
 * Created by Mulan Lin('Snake1999') on 2016/9/16 15:39.
 */
//Natural forms like 11:00:00(11 hours), 1:03(1 min 3 sec). HH:mm:ss, mm:ss, ss are supported
class LMLDP$Lang$Natural implements LMLDP$Lang {
    private static Pattern time = Pattern.compile("^[0-9]*:([0-5]?[0-9])(:([0-5]?[0-9]))?$");
    private static Pattern positiveInteger = Pattern.compile("^[0-9]+$");
    private boolean isPositiveInteger(String a) {
        return positiveInteger.matcher(a).find();
    }
    @Override
    public boolean identify(String s) {
        s = s.replace("：", ":");
        return positiveInteger.matcher(s).find() || time.matcher(s).find();
    }

    @Override
    public Duration convert(String s) {
        if (positiveInteger.matcher(s).find()) {
            return Duration.ofSeconds(Integer.parseInt(s));
        } else if (time.matcher(s).find()) {
            long sec = 0L;
            String[] cut = s.split(":");
            if (cut.length == 3) {
                sec += Integer.parseInt(cut[0]) * 3600;
                sec += Integer.parseInt(cut[1]) * 60;
                sec += Integer.parseInt(cut[2]);
            } else { // length == 2
                sec += Integer.parseInt(cut[0]) * 60;
                sec += Integer.parseInt(cut[1]);
            }
            return Duration.ofSeconds(sec);
        }
        return null;
    }
}
