package com.fzm.teamplate.service;

import sun.rmi.runtime.Log;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2020/2/26
 */
public interface SendMqService {

    Long TemplateSendMq(
            String apikey,
            String mobile,
            String tplContent,
            double fee,
            String unit);
}
