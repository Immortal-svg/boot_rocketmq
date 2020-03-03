package com.fzm.common.enums;

/**
 * Created by lff on 2016/9/12.
 */
public enum ResponseEnum {

    SUCCESS(0, "SUCCESS"),
    TPLCOENT_NOT_EXIT(-1,"模板内容不能为空!"),
    USERKEY_NOT_EXIT(-1,"用户唯一标识apikey不能为空!"),
    TPLWEBSITE_NOT_EXIT(-1,"验证码类模板必须填写对应的官网注册页面!"),
    TPL_ID_NOT_EXIT(-1,"模板ID（tpl_id）不能为空!"),
    TPL_ID_NOTEXIT(-1,"未匹配到该tpl_id的模板!"),
    MODUBLE_ISNULL(-1,"手机号不能为空！"),
    TPL_STARTUS_CHECKING(-1,"该 tpl_id 模板审核中！"),
    TPL_STARTUS_FALL(-1,"该 tpl_id 模板审核未通过！"),
    TPL_VALUES_FORMATERROR(-1,"tpl_value 对应的变量名和变量值格式必须正确!"),
    TPL_STARTUS_ISNULL(-1,"该 tpl_id 模板未审核！"),
    TPLVALUE_ISNULL(-1,"tpl_value不能为空！"),
    FAIL(-1, "FAIL");

    private Integer code;
    private String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
