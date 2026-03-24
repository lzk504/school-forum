package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.dto.EmailRecord;
import com.example.mapper.EmailRecordMapper;
import com.example.service.EmailService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    AmqpTemplate rabbitTemplate;
    @Resource
    EmailRecordMapper recordMapper;

    @Override
    public void sendVerifyEmail(String type, String email, int code) {
//        Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
        EmailRecord emailRecord = switch (type) {
            case "register" ->
                    new EmailRecord(email, "欢迎注册我们的网站", "您的邮件注册验证码为: " + code + "，有效时间3分钟，为了保障您的账户安全，请勿向他人泄露验证码信息。");
            case "reset" ->
                    new EmailRecord(email, "您的密码重置邮件", "你好，您正在执行重置密码操作，验证码: " + code + "，有效时间3分钟，如非本人操作，请无视。");
            case "modify" ->
                    new EmailRecord(email, "您的修改验证邮件", "你好，您正在绑定新的邮箱地址，验证码: " + code + "，有效时间3分钟，如非本人操作，请无视。");
            default -> throw new IllegalArgumentException(type);
        };
        //记录邮件到数据库
        recordMapper.insert(emailRecord);
        rabbitTemplate.convertAndSend(Const.MQ_MAIL, emailRecord);

    }

    @Override
    public Page<EmailRecord> listEmailRecord(int page, int size) {
        return recordMapper.selectPage(Page.of(page,size,true),Wrappers.emptyWrapper());
    }

    @Override
    public boolean resendEmailRecord(int id) {
        EmailRecord emailRecord = recordMapper.selectById(id);
        if (emailRecord == null) {
            return false;
        }
        emailRecord.setStatus(0);
        rabbitTemplate.convertAndSend(Const.MQ_MAIL, emailRecord);
        recordMapper.updateById(emailRecord);
        return true;
    }
}
