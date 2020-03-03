package com.fzm.teamplate.service.impl;

import com.fzm.teamplate.vo.SendTemplate;
import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Value("${apache.rocketmq.consumer.PushConsumer}")
    private String consumerGroup;

    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.name}")
    private String rocketName;

    private  DefaultMQPushConsumer consumer;

    @Autowired
    private MongoTemplate mongoTemplate;


    @PostConstruct
    public void defaultMQPushConsumer() {
        Gson gson=new Gson();
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeMessageBatchMaxSize(1);

        try {
            consumer.subscribe(rocketName, "*");
            // 如果是第一次启动，从队列头部开始消费
            // 如果不是第一次启动，从上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //顺序消费
            consumer.registerMessageListener(new MessageListenerOrderly() {
                 @Override
                 public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    /* System.out.print(Thread.currentThread().getName() + " Receive New Messages: ");*/
                     try {
                         for (MessageExt msg : msgs) {
                             String msgJson=new String(msg.getBody(),RemotingHelper.DEFAULT_CHARSET);
                             SendTemplate sendTemplate=gson.fromJson(msgJson, SendTemplate.class);
                             logger.info(msg.getMsgId() + "==content:" + sendTemplate.getTplContent());
                             // 1、把短信发送：
                             //成功：扣除用户钱
                            //失败：先重试,重试不了修改mogo
                            /*Query query=new Query(Criteria.where("_id").is(sendTemplate.getSid()));
                             Update update = new Update();
                             update.set("status", Constants.STARUS_SEND_ERROR);
                             update.set("remark","发送失败!");
                             mongoTemplate.updateFirst(query, update, SmsSend.class);*/
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
             });
            consumer.start();
            logger.info("[Consumer 已启动]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}