package com.fzm.teamplate.po;

import com.fzm.common.annotation.AutoIncKey;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "sms_send")
@Data
public class SmsSend {

    @AutoIncKey
    @Id
    private Long id = 0L;

    private String apikey;

    private String mobile;

    private String sign;

    private String text;

    private String extend;

    private String uid;

    private String callback_url;

    private Byte register;

    private Byte mobile_stat;

    private Byte status;

    private String create_time;

    private double fee;

    // 失败时备注
    private String remark;

    /**
     * 模板内容
     */
    private String tpl_content;
    /**
     * 单位
     */
    private String unit;
}
