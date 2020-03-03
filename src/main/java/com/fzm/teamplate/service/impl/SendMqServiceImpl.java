package com.fzm.teamplate.service.impl;

import com.fzm.common.constants.Constants;
import com.fzm.teamplate.po.SmsSend;
import com.fzm.teamplate.service.SendMqService;
import com.fzm.teamplate.vo.SendTemplate;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @date 2020/2/26
 */
@Service
public class SendMqServiceImpl implements SendMqService {

    @Value("${rocketmq.name}")
    private String rocketName;

    @Autowired
    private ProducerService producer;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Long TemplateSendMq(String apikey, String mobile, String tplContent, double fee, String unit) {
        try {
            Gson gson = new Gson();
            SmsSend smsSend = new SmsSend();
            smsSend.setApikey(apikey);
            smsSend.setMobile(mobile);
            smsSend.setTpl_content(tplContent);
            smsSend.setFee(fee);
            smsSend.setUnit(unit);
            smsSend.setStatus(Constants.STARUS_SEND_OK);
            smsSend.setRemark("");
            SmsSend insert = mongoTemplate.insert(smsSend);
            SendTemplate sendTemplate = new SendTemplate();
            sendTemplate.setSid(insert.getId());
            sendTemplate.setApikey(apikey);
            sendTemplate.setMobile(mobile);
            sendTemplate.setTplContent(tplContent);
            sendTemplate.setFee(fee);
            sendTemplate.setUnit(unit);
            String res = producer.send(rocketName, "push", gson.toJson(sendTemplate, SendTemplate.class));
            if (StringUtils.isNotBlank(res)) {
                return sendTemplate.getSid();
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }
}
