package cn.yescallop.essentialsnk.util.duration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mulan Lin('Snake1999') on 2016/9/16 15:28.
 */
public final class LMLDurationParser {
    private static List<LMLDP$Lang> lang = new ArrayList<LMLDP$Lang>() {{
        add(new LMLDP$Lang$Natural());
        add(new LMLDP$Lang$Chinese());
    }};

    //supported: Chinese
    public static Duration parse(String s) {
        final Duration[] res = {null};
        lang.forEach((al) -> {
            if (!al.identify(s) || res[0] != null) return;
            res[0] = al.convert(s);
        });
        return res[0];
    }

}
