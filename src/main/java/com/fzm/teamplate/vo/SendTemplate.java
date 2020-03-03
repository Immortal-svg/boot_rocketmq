package com.fzm.teamplate.vo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2020/2/26
 */
@Data
@Document(collation = "t_note")
public class SendTemplate {

    private Long sid;

    private String apikey;

    private String mobile;

    private String tplContent;

    private double fee;

    private String unit;
}
