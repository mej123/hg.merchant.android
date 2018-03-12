package com.zishan.sardinemerchant;

import java.util.Random;

/**
 * Created by wislie on 2017/10/12.
 */

public class Constant {

    //事件统计
    public static final String UMENG_EVENT_1 = "homePageOrMoreClick";
    //所有工具链接
    public static final String TOOL_URL = "https://h5.jie365.cn/dos/app/201/android/modules.json?"+new Random().nextLong();

    //分块大小
    public static int BLOCK_SIZE = 1024 * 1024;

    //1包厢 2大厅 3卡座
    public static final int BOX = 1;
    public static final int HALL = 2;
    public static final int CARD = 3;
    //4全部 0新订单，1就餐中，2闲置中 3已买单
    public static final int NEW_ORDER = 0;
    public static final int AT_MEAL = 1;
    public static final int EMPTY_ORDER = 2;
    public static final int PAID_ORDER = 3;
    public static final int ALL_ORDER = 4;



    //0 全部， 1在售, 2下架, 3售空
    public static final int STORE_ALL = 0;
    public static final int STORE_SALE = 1;
    public static final int STORE_OFF = 2;
    public static final int STORE_EMPTY = 3;


    //-1表示全部， -2折扣 -3推荐
    public static final long STORE_MENUE_ALL = -1;
    public static final long STORE_MENUE_DISCOUNT = -2;
    public static final long STORE_MENUE_RECOMMEND = -3;


    //	支付类型(1:线下,2:线上,3:无需付款)
    public static final int STORE_PAY_TYPE_LIVE = 1;
    public static final int STORE_PAY_TYPE_ONLINE = 2;
    public static final int STORE_PAY_TYPE_FREE = 3;
    // 订单类型(1:线下消费,2:线下扫码,3:核销)
    public static final int STORE_ORDER_TYPE_LIVE_CONSUME = 1;
    public static final int STORE_ORDER_TYPE_LIVE_SCAN = 2;
    public static final int STORE_ORDER_TYPE_CERIFICATION = 3;

    //增加常量 来区分全部，在线，现金, 扫码支付
    public static final int STORE_ORDER_ALL = 1;
    public static final int STORE_ORDER_RECIPIENT = 2;
    public static final int STORE_ORDER_CHECK_TICKET = 3;


    //0未入单，1已入单，3已上菜 4结束 5非正常结束 6已退菜
    public static final int DISH_STATE_INIT = 0;
    public static final int DISH_STATE_CONFIRM = 1;
    public static final int DISH_STATE_COOK = 2;
    public static final int DISH_STATE_PUT = 3;
    public static final int DISH_STATE_FINISH = 4;
    public static final int DISH_STATE_UNEXPECTED = 5;
    public static final int DISH_STATE_CANCEL = 6;

    //预约类型 已落座/已接受 已通知/未处理  已拒绝
    public static final int APPOINTMENT_SEATED = 0;
    public static final int APPOINTMENT_INFORM = 1;
    public static final int APPOINTMENT_REFUSE = 2;

    //分别表示 免单 退菜 已上菜 修改价格
    public static final String DISH_FREE = "dish_free";
    public static final String DISH_CANCEL = "dish_cancel";
    public static final String DISH_PUT = "dish_put";
    public static final String DISH_EDIT_PRICE = "dish_edit_price";

    //结算 合并 拆台 转台 撤单 点菜 查看订单详情 清台 结算并清台
    public static final String OPERATION_BLANCE = "blance";
    public static final String OPERATION_MERGE = "merge";
    public static final String OPERATION_APART = "apart";
    public static final String OPERATION_TRANSMIT = "transmit";
    public static final String OPERATION_REMOVE = "removeFragment";
    public static final String OPERATION_CHOOSE = "choose";
    public static final String OPERATION_VIEW = "view";
    public static final String OPERATION_CLEAN = "clean";
    public static final String OPERATION_CLEAN_BLANCE = "clean_blance";

    //添加额外的菜
    public static final int DISH_ADD = 1;

    //商品
    public static final String CONFIG_PRODUCT = "product";
    //商品组
    public static final String CONFIG_PRODUCT_GROUP = "product_group";
    //商品操作状态
    public static final String CONFIG_PRODUCT_STATE = "product_state";
    //商品分类类型
    public static final String CONFIG_CLASSIFY_TYPE = "classify_type";

    //2停业整顿，3开始营业，4封停，5开店中
    public static final int STORE_REPAIRED = 2;
    public static final int STORE_IN = 3;
    public static final int STORE_STOPPED = 4;
    public static final int STORE_STARTNG = 5;

    //商品添加
    public static final int GOODS_ADD = 108;
    //商品编辑
    public static final int GOODS_UPDATE = 109;


    //商品分类刷新
    public static final int CLASSIFY_GROUP = 113;


    //权限允许
    public static final int PERMISSION_ALLOWED = 116;
    //权限禁止
    public static final int PERMISSION_FORBIDDEN = 117;


    //动态权限设置


    //二维码权限
    public static final int PERMISSION_QR_CODE = 12;

    //高德地图定位权限
    public static final int PERMISSION_GAODE_LOCATION = 13;

    //相机权限
    public static final int PERMISSION_CAMERA = 14;

    //添加图片
    public static final int REQUEST_CODE_SELECT = 15;
    //预览图片
    public static final int REQUEST_CODE_PREVIEW = 16;
}
