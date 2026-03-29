package com.gg.guimall.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * 支付宝配置类
 * 读取application.yml中的配置，初始化AlipayClient
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {

        /** 应用ID */
        private String appId;
        /** 应用私钥 */
        private String privateKey;
        /** 支付宝公钥 */
        private String alipayPublicKey;
        /** 异步通知地址 */
        private String notifyUrl;
        /** 同步通知地址 */
        private String returnUrl;
        /** 签名类型 */
        private String signType;
        /** 字符编码 */
        private String charset;
        /** 支付宝网关 */
        private String gatewayUrl;
        /**
        * 初始化AlipayClient
        * AlipayClient是线程安全的，可以单例使用
        */

}