package com.gg.guimall.web.constants;

public interface MQConstants {

    /** 订单超时取消 Topic */
    String ORDER_TIMEOUT_TOPIC = "order-timeout-topic";

    /** 订单超时取消 Tag */
    String ORDER_TIMEOUT_TAG = "order-timeout";

    /** RocketMQ 延迟级别 16 = 30 分钟 */
    int ORDER_TIMEOUT_DELAY_LEVEL = 16;
}
