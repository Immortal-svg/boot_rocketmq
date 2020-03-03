package com.fzm.teamplate.vo;

import lombok.Data;

/**
 * @author Administrator
 * @date 2020/2/24
 */
@Data
public class RequesSendTemplate {

    private String apikey;

    private String mobile;

    private Long tpl_id;

    private String tpl_value;

    private String extend;

    private String uid;

}
