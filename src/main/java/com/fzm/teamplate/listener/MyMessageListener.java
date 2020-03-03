package com.fzm.teamplate.listener;

import com.fzm.common.constants.Constants;
import com.fzm.teamplate.po.SmsSend;
import com.fzm.teamplate.service.impl.ConsumerService;
import com.fzm.teamplate.vo.SendTemplate;
import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyMessageListener implements MessageListenerOrderly {


    private static final Logger logger = LoggerFactory.getLogger(MyMessageListener.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        // 设置自动提交
        context.setAutoCommit(true);
        Gson gson=new Gson();
       /* System.out.print(Thread.currentThread().getName() + " Receive New Messages: ");*/
        try {
            for (MessageExt msg : msgs) {
                String msgJson=new String(msg.getBody(),RemotingHelper.DEFAULT_CHARSET);
                SendTemplate sendTemplate=gson.fromJson(msgJson, SendTemplate.class);
                logger.info(msg.getMsgId() + "==content:" + sendTemplate.getTplContent());
                // 1、把短信发送：
                //成功：扣除用户钱
                //失败：先重试,重试不了修改mogo
                 Query query=new Query(Criteria.where("_id").is(sendTemplate.getSid()));
                 Update update = new Update();
                 update.set("status", Constants.STARUS_SEND_ERROR);
                 update.set("remark","发送失败!");
                 mongoTemplate.updateFirst(query, update, SmsSend.class);
            }
            //int i=1/0;
        } catch (Exception e) {
            e.printStackTrace();
            if (msgs.get(0).getReconsumeTimes() == 3) {
                return ConsumeOrderlyStatus.SUCCESS;
            }
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}