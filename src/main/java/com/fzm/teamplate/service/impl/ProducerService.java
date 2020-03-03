package com.fzm.teamplate.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {

    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Value("${apache.rocketmq.producer.producerGroup}")
    private String producerGroup;

    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.name}")
    private String rocketName;

    private DefaultMQProducer producer;

    @PostConstruct
    public void initProducer() {
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setRetryTimesWhenSendFailed(3);
        producer.setSendMsgTimeout(6000);
        try {
            producer.start();
            logger.info("[Producer 已启动]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String send(String topic, String tags, String msg) {
        SendResult result = null;
        try {
            Message message = new Message(topic, tags, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            result = producer.send(message, new MessageQueueSelector() {
                /**
                 *
                 * @param list topic下的所有队列
                 * @param message 发送的消息
                 * @param queue arg参数
                 * @return
                 */
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object queue) {
                    Integer queueNum = (Integer) queue;
                    return list.get(queueNum);
                }
            },1);
            logger.info("[Producer] msgID(" + result.getMsgId() + ") " + result.getSendStatus()+"   msg:"+msg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "{\"MsgId\":\"" + result.getMsgId() + "\"}";
    }

    @PreDestroy
    public void shutDownProducer() {
        if (producer != null) {
            producer.shutdown();
        }
    }
}