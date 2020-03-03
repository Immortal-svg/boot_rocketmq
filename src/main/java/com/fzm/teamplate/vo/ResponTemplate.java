package com.fzm.teamplate.vo;

import lombok.Data;

/**
 * @author Administrator
 * @date 2020/2/24
 */
@Data
public class ResponTemplate {

    private Long tpl_id;

    private String tpl_content;

    private String check_status;

    private String reason;

    private Integer ref_id;

    private String lang;

    private String country_code;

}
