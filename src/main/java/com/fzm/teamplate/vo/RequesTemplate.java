package com.fzm.teamplate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Administrator
 * @date 2020/2/24
 */
@Data
public class RequesTemplate {

    /**
     * tpl_id
     */
    private Long tpl_id;

    /**
     * 用户唯一标识
     */
    private String api_key;

    /**
     * 模板内容
     */
    private String tpl_content;
    /**
     * 验证码类模板对应的官网注册页面
     */
    private String website;
    /**
     * 1、 代表验证码类模板
     */
    private Integer tpl_type;
    /**
     * 审核结果会向这个地址推送
     */
    private String callback;
    /**
     * 说明模板的发送场景和对象
     */
    private String apply_description;


}
