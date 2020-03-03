package com.fzm.teamplate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzm.common.constants.Constants;
import com.fzm.common.enums.Message;
import com.fzm.common.enums.ResponseEnum;
import com.fzm.common.utils.ExtractMessage;
import com.fzm.teamplate.po.Template;
import com.fzm.teamplate.service.SendMqService;
import com.fzm.teamplate.service.TemplateService;
import com.fzm.teamplate.service.impl.ProducerService;
import com.fzm.teamplate.vo.RequesSendTemplate;
import com.fzm.teamplate.vo.RequesTemplate;
import com.fzm.teamplate.vo.ResponTemplate;
import com.fzm.teamplate.vo.TemplateVo;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020/2/24
 */
@Api(tags = "模板接口")
@RestController
public class TemplateControoler {

    private static final Logger logger = Logger.getLogger(TemplateControoler.class);

    @Autowired
    private TemplateService templateService;

    @Autowired
    private SendMqService sendMqService;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 添加模版
     *
     * @param templatevo
     * @return
     */
    @RequestMapping(value = "/v1/tpl/add.json", method = RequestMethod.POST)
    public Message tplAdd(TemplateVo templatevo) {
        if (StringUtils.isBlank(templatevo.getApikey())) {
            return new Message(ResponseEnum.USERKEY_NOT_EXIT);
        }
        if (StringUtils.isBlank(templatevo.getTpl_content())) {
            return new Message(ResponseEnum.TPLCOENT_NOT_EXIT);
        }
        if (null != templatevo.getTplType() && templatevo.getTplType().equals(1)) {
            if (StringUtils.isBlank(templatevo.getWebsite())) {
                return new Message(ResponseEnum.TPLWEBSITE_NOT_EXIT);
            }
        }
        Template template = new Template();
        template.setApiKey(templatevo.getApikey());
        template.setCheckStatus(Constants.CHECKING);
        template.setStatus(Constants.STATUS_OK);
        template.setLastUpdateTime(LocalDateTime.now());
        template.setTplContent(templatevo.getTpl_content());
        template.setNotifyType(null == templatevo.getNotify_type() ? null : templatevo.getNotify_type());
        template.setWebsite(null == templatevo.getWebsite() ? null : templatevo.getWebsite());
        template.setTplType(null == templatevo.getTplType() ? null : templatevo.getTplType());
        template.setCallback(null == templatevo.getCallback() ? null : templatevo.getCallback());
        template.setApplyDescription(null == templatevo.getApply_description() ? null : templatevo.getApply_description());
        template.setLang(null == templatevo.getLang() ? null : templatevo.getLang());
        template.setCountryCode(null == templatevo.getCountry_code() ? null : templatevo.getCountry_code());
        template.setRefId(null == templatevo.getRef_id() ? null : templatevo.getRef_id());
        templateService.save(template);
        Map<String, Object> parpms = new HashMap<>();
        parpms.put("tpl_id", template.getTplId());
        parpms.put("tpl_content", template.getTplContent());
        parpms.put("check_status", template.getCheckStatus());
        parpms.put("reason", template.getReason());
        return new Message(ResponseEnum.SUCCESS, parpms);
    }

    /**
     * 修改模板
     *
     * @param template
     * @return
     */
    @RequestMapping(value = "/v1/tpl/update.json", method = RequestMethod.POST)
    public Message tplupdate(RequesTemplate template) {
        if (StringUtils.isBlank(template.getTpl_content())) {
            return new Message(ResponseEnum.TPLCOENT_NOT_EXIT);
        }
        if (StringUtils.isBlank(template.getApi_key())) {
            return new Message(ResponseEnum.USERKEY_NOT_EXIT);
        }
        if (null == template.getTpl_id() || template.getTpl_id() <= 0) {
            return new Message(ResponseEnum.TPL_ID_NOT_EXIT);
        }
        if (null != template.getTpl_type() && template.getTpl_type().equals(1)) {
            if (StringUtils.isBlank(template.getWebsite())) {
                return new Message(ResponseEnum.TPLWEBSITE_NOT_EXIT);
            }
        }
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.eq("tpl_id", template.getTpl_id());
        templateQueryWrapper.eq("api_key", template.getApi_key());
        //Template templateUp = templateService.getById(template.getTpl_id());
        Template templateUp = templateService.getOne(templateQueryWrapper);
        if (!templateUp.getStatus().equals(1)) {
            return new Message(ResponseEnum.TPL_ID_NOTEXIT);
        }
        if (null != template) {
            templateUp.setTplContent(template.getTpl_content());
            templateUp.setWebsite(null == template.getWebsite() ? null : template.getWebsite());
            templateUp.setTplType(null == template.getTpl_type() ? null : template.getTpl_type());
            templateUp.setCallback(null == template.getCallback() ? null : template.getCallback());
            templateUp.setApplyDescription(null == template.getApply_description() ? null : template.getApply_description());
            templateUp.setCheckStatus(Constants.CHECKING);
            templateUp.setLastUpdateTime(LocalDateTime.now());
            templateService.updateById(templateUp);
            Map<String, Object> parpms = new HashMap<>();
            parpms.put("tpl_id", templateUp.getTplId());
            parpms.put("tpl_content", templateUp.getTplContent());
            parpms.put("check_status", templateUp.getCheckStatus());
            parpms.put("reason", templateUp.getReason());
            return new Message(ResponseEnum.SUCCESS, parpms);
        }
        return new Message(ResponseEnum.FAIL);
    }

    /**
     * 获取模板
     *
     * @param apikey
     * @param tpl_id
     * @return
     */
    @RequestMapping(value = "/v1/tpl/get.json", method = RequestMethod.POST)
    public Message getTpl(String apikey, Long tpl_id) {
        if (StringUtils.isBlank(apikey)) {
            return new Message(ResponseEnum.USERKEY_NOT_EXIT);
        }
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.eq("status", Constants.STATUS_OK);
        templateQueryWrapper.eq("api_key", apikey);
        if (tpl_id != null && tpl_id > 0) {
            templateQueryWrapper.eq("tpl_id", tpl_id);
        }
        List<Template> templates = templateService.list(templateQueryWrapper);
        List<ResponTemplate> template = new ArrayList<>();
        for (Template temp : templates) {
            ResponTemplate responTemplate = new ResponTemplate();
            responTemplate.setCheck_status(null == temp.getCheckStatus() ? null : temp.getCheckStatus());
            responTemplate.setCountry_code(null == temp.getCountryCode() ? null : temp.getCountryCode());
            responTemplate.setLang(null == temp.getLang() ? null : temp.getLang());
            responTemplate.setReason(null == temp.getReason() ? null : temp.getReason());
            responTemplate.setTpl_content(temp.getTplContent());
            responTemplate.setTpl_id(temp.getTplId());
            responTemplate.setRef_id(null == temp.getRefId() ? null : temp.getRefId());
            template.add(responTemplate);
        }
        return new Message(ResponseEnum.SUCCESS, template);
    }

    /**
     * 逻辑删除
     *
     * @param tpl_id
     * @return
     */
    @RequestMapping(value = "/v1/tpl/del.json", method = RequestMethod.POST)
    public Message deleTpl(Long tpl_id) {
        if (null == tpl_id || tpl_id <= 0) {
            return new Message(ResponseEnum.TPL_ID_NOT_EXIT);
        }
        Template templateUp = templateService.getById(tpl_id);
        templateUp.setStatus(Constants.STATUS_ERROR);
        boolean isUpdate = templateService.updateById(templateUp);
        if (isUpdate) {
            return new Message(ResponseEnum.SUCCESS);
        }
        return new Message(ResponseEnum.FAIL);
    }


    /**
     * 指定模板发送
     *
     * @param requesSendTemplate
     * @return
     */
    @RequestMapping(value = "/v1/sms/tpl_single_send.json", method = RequestMethod.POST)
    public Message tplSingleSend(RequesSendTemplate requesSendTemplate) {
        //一 1、验证参数
        if (StringUtils.isBlank(requesSendTemplate.getApikey())) {
            return new Message(ResponseEnum.USERKEY_NOT_EXIT);
        }
        if (StringUtils.isBlank(requesSendTemplate.getMobile())) {
            return new Message(ResponseEnum.MODUBLE_ISNULL);
        }
        if (null == requesSendTemplate.getTpl_id() || 0 >= requesSendTemplate.getTpl_id()) {
            return new Message(ResponseEnum.TPL_ID_NOT_EXIT);
        }
        Template template = templateService.getById(requesSendTemplate.getTpl_id());
        if (null == template) {
            return new Message(ResponseEnum.TPL_ID_NOTEXIT);
        }
        if (!template.getStatus().equals(1)) {
            return new Message(ResponseEnum.TPL_ID_NOTEXIT);
        }
        //该 tpl_id 模板未审核！
        if (null == template.getCheckStatus()) {
            return new Message(ResponseEnum.TPL_STARTUS_ISNULL);
        }
        //该 tpl_id 模板审核未通过！
        if (Constants.FAIL.equals(template.getCheckStatus())) {
            return new Message(ResponseEnum.TPL_STARTUS_FALL);
        }
        //该 tpl_id 模板审核中！
        if (Constants.CHECKING.equals(template.getCheckStatus())) {
            return new Message(ResponseEnum.TPL_STARTUS_CHECKING);
        }
        //验证用户金额是否充足！
        String tplcontent = template.getTplContent();
        String tplValue = (null == requesSendTemplate.getTpl_value() ? "" : requesSendTemplate.getTpl_value());
        List<String> list = ExtractMessage.extractMessageByRegular(tplcontent);
        Map<String, String> tplValueparms = ExtractMessage.tplValueExtract(tplValue);
        for (String var : list) {
            if (tplValueparms.isEmpty()) {
                return new Message(ResponseEnum.TPL_VALUES_FORMATERROR);
            }
            String regex = "#" + var + "#";
            tplcontent = tplcontent.replaceAll(regex, tplValueparms.get(regex));
        }
        if (list.isEmpty()) {
            tplValue = "";
        }
        //
        //2、需要保存的数据进入rocketmq  、保存mogo （短信默认发送成功、失败再去修改mogo）
        logger.info("内容：" + tplcontent);
        Long sid = sendMqService.TemplateSendMq(requesSendTemplate.getApikey(), requesSendTemplate.getMobile(), tplcontent, 0.05, "RMB");


        //producer.send(rocketName, "push", gson.toJson(person, Person.class));
        //二 2 文档
      /* Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", requesSendTemplate.getApikey());
        params.put("mobile", requesSendTemplate.getMobile());
        params.put("tpl_id", String.valueOf(requesSendTemplate.getTpl_id()));
        params.put("tplcontent", tplcontent);
        try {
            params.put("tpl_value",
                    URLEncoder.encode("#code#", Constants.ENCODING) + "=" +
                            URLEncoder.encode("1234", Constants.ENCODING) + "&" +
                            URLEncoder.encode("#company#", Constants.ENCODING) + "=" +
                            URLEncoder.encode("xxx网", Constants.ENCODING));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        // return post("https://xxxx.xxx.com/v2/sms/tpl_single_send.json", params);


        Map<String, Object> params = new HashMap<String, Object>();
        if (null == sid) {
            params.put("msg", "发送失败!");
            params.put("code", -1);
        } else {
            params.put("code", 0);
            params.put("count", 1);
            params.put("msg", "发送成功!");
            params.put("fee", 0.05);
            params.put("unit", "RMB");
            params.put("mobile", requesSendTemplate.getMobile());
            params.put("sid", sid);
        }
        return new Message(ResponseEnum.SUCCESS, params);
    }


}
