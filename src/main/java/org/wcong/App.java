package org.wcong;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    static int a;

    public static void main(String[] args) {
        System.out.println(a);
    }

    public static Class<?> findSuperClassParameterType(Object instance,  int parameterIndex) {
        Class<?> subClass = instance.getClass();
        while (subClass != subClass.getSuperclass()) {
            // instance.getClass() is no subclass of classOfInterest or instance is a direct instance of classOfInterest
            subClass = subClass.getSuperclass();
            if (subClass == null) throw new IllegalArgumentException();
        }
        ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
        return (Class<?>) parameterizedType.getActualTypeArguments()[parameterIndex];
    }

    public class A {
    }

    public static class B {
    }


    public enum LogisticsCompanyEnum {

        QITA("Q", 0, "其它"), SHUNFENG("S", 1, "顺丰速运"), ZHONGTONG("Z", 2, "中通快递"), SHENTONG("S", 3, "申通快递"), YUANTONG("Y", 4, "圆通快递"), YUNDA("Y", 5, "韵达快递"),
        EMS("E", 6, "EMS"), TIANTIAN("T", 7, "天天快递"), GUOTONG("G", 8, "国通快递"), SUER("S", 9, "速尔快递"), BAISHI("B", 10, "百世汇通"), LIANBANG("L", 11, "联邦快递"),
        QUANFENG("Q", 12, "全峰快递"), ZAIJISONG("Z", 13, "宅急送"), QUANYI("Q", 14, "全一快递"), UPS("U", 15, "UPS快递"),
        ZHONG_WAI_YUN("Z", 16, "中外运速递"),;

        private String initial;
        private int logisticsCompanyId;
        private String logisticsCompanyName;

        private LogisticsCompanyEnum(String initial, int logisticsCompanyId, String logisticsCompanyName) {
            this.initial = initial;
            this.logisticsCompanyId = logisticsCompanyId;
            this.logisticsCompanyName = logisticsCompanyName;
        }

        public String getInitial() {
            return initial;
        }

        public int getLogisticsCompanyId() {
            return logisticsCompanyId;
        }

        public String getLogisticsCompanyName() {
            return logisticsCompanyName;
        }

    }

    private static final Map<Long, String> ID_TO_NAME_LOGISTICS = new HashMap<Long, String>();

    static {
        ID_TO_NAME_LOGISTICS.put(16L, "中外运速递");
        ID_TO_NAME_LOGISTICS.put(17L, "E特快");
        ID_TO_NAME_LOGISTICS.put(18L, "芝麻开门");
        ID_TO_NAME_LOGISTICS.put(19L, "赛澳递");
        ID_TO_NAME_LOGISTICS.put(20L, "安能物流");
        ID_TO_NAME_LOGISTICS.put(21L, "安迅物流");
        ID_TO_NAME_LOGISTICS.put(22L, "巴伦支快递");
        ID_TO_NAME_LOGISTICS.put(23L, "北青小红帽");
        ID_TO_NAME_LOGISTICS.put(24L, "百福东方物流");
        ID_TO_NAME_LOGISTICS.put(25L, "邦送物流");
        ID_TO_NAME_LOGISTICS.put(26L, "宝凯物流");
        ID_TO_NAME_LOGISTICS.put(27L, "百千诚物流");
        ID_TO_NAME_LOGISTICS.put(28L, "博源恒通");
        ID_TO_NAME_LOGISTICS.put(29L, "百成大达物流");
        ID_TO_NAME_LOGISTICS.put(30L, "百世快运");
        ID_TO_NAME_LOGISTICS.put(31L, "COE（东方快递）");
        ID_TO_NAME_LOGISTICS.put(32L, "城市100");
        ID_TO_NAME_LOGISTICS.put(33L, "传喜物流");
        ID_TO_NAME_LOGISTICS.put(34L, "城际速递");
        ID_TO_NAME_LOGISTICS.put(35L, "成都立即送");
        ID_TO_NAME_LOGISTICS.put(36L, "出口易");
        ID_TO_NAME_LOGISTICS.put(37L, "晟邦物流");
        ID_TO_NAME_LOGISTICS.put(38L, "DHL快递");
        ID_TO_NAME_LOGISTICS.put(39L, "DHL");
        ID_TO_NAME_LOGISTICS.put(40L, "德邦大田物流");
        ID_TO_NAME_LOGISTICS.put(41L, "东方快递");
        ID_TO_NAME_LOGISTICS.put(42L, "递四方");
        ID_TO_NAME_LOGISTICS.put(43L, "大洋物流");
        ID_TO_NAME_LOGISTICS.put(44L, "店通快递");
        ID_TO_NAME_LOGISTICS.put(45L, "德创物流");
        ID_TO_NAME_LOGISTICS.put(46L, "东红物流");
        ID_TO_NAME_LOGISTICS.put(47L, "D速物流");
        ID_TO_NAME_LOGISTICS.put(48L, "东瀚物流");
        ID_TO_NAME_LOGISTICS.put(49L, "达方物流");
        ID_TO_NAME_LOGISTICS.put(50L, "俄顺达");
        ID_TO_NAME_LOGISTICS.put(51L, "FedEx快递");
        ID_TO_NAME_LOGISTICS.put(52L, "飞康达物流");
        ID_TO_NAME_LOGISTICS.put(53L, "飞豹快递");
        ID_TO_NAME_LOGISTICS.put(54L, "飞狐快递");
        ID_TO_NAME_LOGISTICS.put(55L, "凡宇速递");
        ID_TO_NAME_LOGISTICS.put(56L, "颿达国际");
        ID_TO_NAME_LOGISTICS.put(57L, "飞远配送");
        ID_TO_NAME_LOGISTICS.put(58L, "飞鹰物流");
        ID_TO_NAME_LOGISTICS.put(59L, "风行天下");
        ID_TO_NAME_LOGISTICS.put(60L, "GATI快递");
        ID_TO_NAME_LOGISTICS.put(61L, "港中能达物流");
        ID_TO_NAME_LOGISTICS.put(62L, "共速达");
        ID_TO_NAME_LOGISTICS.put(63L, "广通速递");
        ID_TO_NAME_LOGISTICS.put(64L, "广东速腾物流");
        ID_TO_NAME_LOGISTICS.put(65L, "港快速递");
        ID_TO_NAME_LOGISTICS.put(66L, "高铁速递");
        ID_TO_NAME_LOGISTICS.put(67L, "冠达快递");
        ID_TO_NAME_LOGISTICS.put(68L, "华宇物流");
        ID_TO_NAME_LOGISTICS.put(69L, "恒路物流");
        ID_TO_NAME_LOGISTICS.put(70L, "好来运快递");
        ID_TO_NAME_LOGISTICS.put(71L, "华夏龙物流");
        ID_TO_NAME_LOGISTICS.put(72L, "海航天天");
        ID_TO_NAME_LOGISTICS.put(73L, "河北建华");
        ID_TO_NAME_LOGISTICS.put(74L, "海盟速递");
        ID_TO_NAME_LOGISTICS.put(75L, "华企快运");
        ID_TO_NAME_LOGISTICS.put(76L, "昊盛物流");
        ID_TO_NAME_LOGISTICS.put(77L, "户通物流");
        ID_TO_NAME_LOGISTICS.put(78L, "华航快递");
        ID_TO_NAME_LOGISTICS.put(79L, "黄马甲快递");
        ID_TO_NAME_LOGISTICS.put(80L, "合众速递（UCS）");
        ID_TO_NAME_LOGISTICS.put(81L, "韩润物流");
        ID_TO_NAME_LOGISTICS.put(82L, "皇家物流");
        ID_TO_NAME_LOGISTICS.put(83L, "伙伴物流");
        ID_TO_NAME_LOGISTICS.put(84L, "红马速递");
        ID_TO_NAME_LOGISTICS.put(85L, "汇文配送");
        ID_TO_NAME_LOGISTICS.put(86L, "华赫物流");
        ID_TO_NAME_LOGISTICS.put(87L, "佳吉物流");
        ID_TO_NAME_LOGISTICS.put(88L, "佳怡物流");
        ID_TO_NAME_LOGISTICS.put(89L, "加运美快递");
        ID_TO_NAME_LOGISTICS.put(90L, "急先达物流");
        ID_TO_NAME_LOGISTICS.put(91L, "京广速递");
        ID_TO_NAME_LOGISTICS.put(92L, "晋越快递");
        ID_TO_NAME_LOGISTICS.put(93L, "捷特快递");
        ID_TO_NAME_LOGISTICS.put(94L, "久易快递");
        ID_TO_NAME_LOGISTICS.put(95L, "快捷快递");
        ID_TO_NAME_LOGISTICS.put(96L, "康力物流");
        ID_TO_NAME_LOGISTICS.put(97L, "跨越速运");
        ID_TO_NAME_LOGISTICS.put(98L, "快优达速递");
        ID_TO_NAME_LOGISTICS.put(99L, "快淘快递");
        ID_TO_NAME_LOGISTICS.put(100L, "联昊通物流");
        ID_TO_NAME_LOGISTICS.put(101L, "龙邦速递");
        ID_TO_NAME_LOGISTICS.put(102L, "乐捷递");
        ID_TO_NAME_LOGISTICS.put(103L, "立即送");
        ID_TO_NAME_LOGISTICS.put(104L, "蓝弧快递");
        ID_TO_NAME_LOGISTICS.put(105L, "乐天速递");
        ID_TO_NAME_LOGISTICS.put(106L, "民航快递");
        ID_TO_NAME_LOGISTICS.put(107L, "美国快递");
        ID_TO_NAME_LOGISTICS.put(108L, "门对门");
        ID_TO_NAME_LOGISTICS.put(109L, "明亮物流");
        ID_TO_NAME_LOGISTICS.put(110L, "民邦速递");
        ID_TO_NAME_LOGISTICS.put(111L, "闽盛快递");
        ID_TO_NAME_LOGISTICS.put(112L, "麦力快递");
        ID_TO_NAME_LOGISTICS.put(113L, "能达速递");
        ID_TO_NAME_LOGISTICS.put(114L, "偌亚奥国际");
        ID_TO_NAME_LOGISTICS.put(115L, "平安达腾飞");
        ID_TO_NAME_LOGISTICS.put(116L, "陪行物流");
        ID_TO_NAME_LOGISTICS.put(117L, "全日通快递");
        ID_TO_NAME_LOGISTICS.put(118L, "全晨快递");
        ID_TO_NAME_LOGISTICS.put(119L, "秦邦快运");
        ID_TO_NAME_LOGISTICS.put(120L, "如风达快递");
        ID_TO_NAME_LOGISTICS.put(121L, "日昱物流");
        ID_TO_NAME_LOGISTICS.put(122L, "瑞丰速递");
        ID_TO_NAME_LOGISTICS.put(123L, "山东海红");
        ID_TO_NAME_LOGISTICS.put(124L, "盛辉物流");
        ID_TO_NAME_LOGISTICS.put(125L, "世运快递");
        ID_TO_NAME_LOGISTICS.put(126L, "盛丰物流");
        ID_TO_NAME_LOGISTICS.put(127L, "上大物流");
        ID_TO_NAME_LOGISTICS.put(128L, "三态速递");
        ID_TO_NAME_LOGISTICS.put(129L, "申通E物流");
        ID_TO_NAME_LOGISTICS.put(130L, "圣安物流");
        ID_TO_NAME_LOGISTICS.put(131L, "山西红马甲");
        ID_TO_NAME_LOGISTICS.put(132L, "穗佳物流");
        ID_TO_NAME_LOGISTICS.put(133L, "沈阳佳惠尔");
        ID_TO_NAME_LOGISTICS.put(134L, "上海林道货运");
        ID_TO_NAME_LOGISTICS.put(135L, "十方通物流");
        ID_TO_NAME_LOGISTICS.put(136L, "山东广通速递");
        ID_TO_NAME_LOGISTICS.put(137L, "顺捷丰达");
        ID_TO_NAME_LOGISTICS.put(138L, "TTNT快递");
        ID_TO_NAME_LOGISTICS.put(139L, "天地华宇");
        ID_TO_NAME_LOGISTICS.put(140L, "通和天下");
        ID_TO_NAME_LOGISTICS.put(141L, "天纵物流");
        ID_TO_NAME_LOGISTICS.put(142L, "同舟行物流");
        ID_TO_NAME_LOGISTICS.put(143L, "腾达速递");
        ID_TO_NAME_LOGISTICS.put(144L, "UC优速快递");
        ID_TO_NAME_LOGISTICS.put(145L, "万象物流");
        ID_TO_NAME_LOGISTICS.put(146L, "微特派");
        ID_TO_NAME_LOGISTICS.put(147L, "万家物流");
        ID_TO_NAME_LOGISTICS.put(148L, "万博快递");
        ID_TO_NAME_LOGISTICS.put(149L, "希优特快递");
        ID_TO_NAME_LOGISTICS.put(150L, "新邦物流");
        ID_TO_NAME_LOGISTICS.put(151L, "信丰物流");
        ID_TO_NAME_LOGISTICS.put(152L, "祥龙运通物流");
        ID_TO_NAME_LOGISTICS.put(153L, "西安城联速递");
        ID_TO_NAME_LOGISTICS.put(154L, "西安喜来快递");
        ID_TO_NAME_LOGISTICS.put(155L, "鑫世锐达");
        ID_TO_NAME_LOGISTICS.put(156L, "鑫通宝物流");
        ID_TO_NAME_LOGISTICS.put(157L, "运通快递");
        ID_TO_NAME_LOGISTICS.put(158L, "远成物流");
        ID_TO_NAME_LOGISTICS.put(159L, "亚风速递");
        ID_TO_NAME_LOGISTICS.put(160L, "优速快递");
        ID_TO_NAME_LOGISTICS.put(161L, "亿顺航");
        ID_TO_NAME_LOGISTICS.put(162L, "越丰物流");
        ID_TO_NAME_LOGISTICS.put(163L, "源安达快递");
        ID_TO_NAME_LOGISTICS.put(164L, "原飞航物流");
        ID_TO_NAME_LOGISTICS.put(165L, "邮政EMS速递");
        ID_TO_NAME_LOGISTICS.put(166L, "银捷速递");
        ID_TO_NAME_LOGISTICS.put(167L, "一统飞鸿");
        ID_TO_NAME_LOGISTICS.put(168L, "宇鑫物流");
        ID_TO_NAME_LOGISTICS.put(169L, "易通达");
        ID_TO_NAME_LOGISTICS.put(170L, "邮必佳");
        ID_TO_NAME_LOGISTICS.put(171L, "一柒物流");
        ID_TO_NAME_LOGISTICS.put(172L, "音素快运");
        ID_TO_NAME_LOGISTICS.put(173L, "亿领速运");
        ID_TO_NAME_LOGISTICS.put(174L, "煜嘉物流");
        ID_TO_NAME_LOGISTICS.put(175L, "英脉物流");
        ID_TO_NAME_LOGISTICS.put(176L, "云南中诚");
        ID_TO_NAME_LOGISTICS.put(177L, "中铁快运");
        ID_TO_NAME_LOGISTICS.put(178L, "中铁物流");
        ID_TO_NAME_LOGISTICS.put(179L, "中邮物流");
        ID_TO_NAME_LOGISTICS.put(180L, "邮政快递");
        ID_TO_NAME_LOGISTICS.put(181L, "郑州建华");
        ID_TO_NAME_LOGISTICS.put(182L, "中速快件");
        ID_TO_NAME_LOGISTICS.put(183L, "中天万运");
        ID_TO_NAME_LOGISTICS.put(184L, "中睿速递");
        ID_TO_NAME_LOGISTICS.put(185L, "增益速递");
        ID_TO_NAME_LOGISTICS.put(186L, "郑州速捷");
        ID_TO_NAME_LOGISTICS.put(187L, "智通物流");

//        // 把enum中的最初的标准15个快递公司和其它也加进来
//        LogisticsCompanyEnum[] firstArray = LogisticsCompanyEnum.values();
//        for (int i = 0; i < firstArray.length; i++) {
//            ID_TO_NAME_LOGISTICS.put(new Long(firstArray[i].getLogisticsCompanyId()),
//                    firstArray[i].getLogisticsCompanyName());
//        }
    }

}
