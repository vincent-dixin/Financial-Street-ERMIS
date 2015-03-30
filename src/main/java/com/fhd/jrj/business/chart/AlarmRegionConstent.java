package com.fhd.jrj.business.chart;

import java.util.HashMap;
import java.util.Map;

public class AlarmRegionConstent {
    private Map<String, Map<String, Double>> regionMap = new HashMap<String, Map<String, Double>>();

    public Map<String, Map<String, Double>> getAlarmRegionConstent() {
        Map<String, Double> paimingRegion = new HashMap<String, Double>();
        paimingRegion.put("max", 50.0);
        paimingRegion.put("min", 0.0);
        regionMap.put("quanguo_yingyuan_paiming", paimingRegion);
        Map<String, Double> threeXianjinBaozhangNengliRegion = new HashMap<String, Double>();
        threeXianjinBaozhangNengliRegion.put("max", 1.0);
        threeXianjinBaozhangNengliRegion.put("min", 0.0);
        regionMap.put("3_xianjin_baozhang_nengli", threeXianjinBaozhangNengliRegion);
        Map<String, Double> sixXianjinBaozhangNengliRegion = new HashMap<String, Double>();
        sixXianjinBaozhangNengliRegion.put("max", 0.5);
        sixXianjinBaozhangNengliRegion.put("min", 0.0);
        regionMap.put("6_xianjin_baozhang_nengli", sixXianjinBaozhangNengliRegion);
        Map<String, Double> zhiChanFuzhaiLvRegion = new HashMap<String, Double>();
        zhiChanFuzhaiLvRegion.put("max", 100.0);
        zhiChanFuzhaiLvRegion.put("min", 60.0);
        regionMap.put("zhichan_fuzhailv", zhiChanFuzhaiLvRegion);
        Map<String, Double> youxiFuzhaiLvRegion = new HashMap<String, Double>();
        youxiFuzhaiLvRegion.put("max", 100.0);
        youxiFuzhaiLvRegion.put("min", 35.0);
        regionMap.put("youxi_fuzhailv", youxiFuzhaiLvRegion);

        Map<String, Double> jingyingHuodongXianjinLiuliangJingerRegion = new HashMap<String, Double>();
        jingyingHuodongXianjinLiuliangJingerRegion.put("max", -110000.00);
        jingyingHuodongXianjinLiuliangJingerRegion.put("min", 0.0);
        regionMap.put("jingying_huodong_xianjin_liuliang_jinger", jingyingHuodongXianjinLiuliangJingerRegion);
        
        Map<String, Double> zongzhichanZhouzhuanlvRegion = new HashMap<String, Double>();
        zongzhichanZhouzhuanlvRegion.put("max", 10.00);
        zongzhichanZhouzhuanlvRegion.put("min", 0.0);
        regionMap.put("zongzhichan_zhouzhuanlv", zongzhichanZhouzhuanlvRegion);
        
        Map<String, Double> chengbenFeiyongLirenlvRegion = new HashMap<String, Double>();
        chengbenFeiyongLirenlvRegion.put("max", 40.00);
        chengbenFeiyongLirenlvRegion.put("min", 0.0);
        regionMap.put("chengben_feiyong_lirenlv", chengbenFeiyongLirenlvRegion);
        
        Map<String, Double> maolilvRegion = new HashMap<String, Double>();
        maolilvRegion.put("max", 38.00);
        maolilvRegion.put("min", 0.0);
        regionMap.put("maolilv", maolilvRegion);
        
        Map<String, Double> guanjianWangweiKongquelvRegion = new HashMap<String, Double>();
        guanjianWangweiKongquelvRegion.put("max", 100.00);
        guanjianWangweiKongquelvRegion.put("min", 8.0);
        regionMap.put("guanjian_wangwei_kongquelv", guanjianWangweiKongquelvRegion);
        
        
        return regionMap;
    }

}
