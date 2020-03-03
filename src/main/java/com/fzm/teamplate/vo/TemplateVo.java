package com.fzm.teamplate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class TemplateVo implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * tpl_id
     */
    @TableId(type = IdType.AUTO)
    private Long tpl_id;

    /**
     * 用户唯一标识
     */
    private String apikey;

    /**
     * 模板内容
     */
    private String tpl_content;

    /**
     * 审核结果短信通知的方式: rn0 表示需要通知，默认; rn1 表示仅审核不通过时通知;rn 2 表示仅审核通过时通知;rn 3 表示不需要通知
     */
    private Integer notify_type;

    /**
     * 验证码类模板对应的官网注册页面
     */
    private String website;

    /**
     * 1、 代表验证码类模板
     */
    private Integer tplType;

    /**
     * 审核结果会向这个地址推送
     */
    private String callback;

    /**
     * 说明模板的发送场景和对象
     */
    private String apply_description;

    /**
     * 审核状态：checking:审核中/success:审核通过/fail:审核失败
     */
    private String check_status;

    /**
     * 审核未通过的原因
     */
    private String reason;

    /**
     * 模板语言
     */
    private String lang;

    /**
     * 支持国家及地区
     */
    private String country_code;

    /**
     * ref_id
     */
    private Integer ref_id;

    /**
     * status
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime create_time;

    /**
     * 最后更新时间
     */
    private LocalDateTime last_update_time;

}
