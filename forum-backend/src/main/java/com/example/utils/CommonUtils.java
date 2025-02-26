package com.example.utils;

import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.dto.AccountPrivacy;
import com.example.mapper.AccountDetailsMapper;
import com.example.mapper.AccountMapper;
import com.example.mapper.AccountPrivacyMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommonUtils {
    @Resource
    private AccountPrivacyMapper privacyMapper;

    @Resource
    private AccountMapper accountMapper;


    @Resource
    private AccountDetailsMapper detailsMapper;

    /**
     * 根据用户隐私设置填充用户详情。
     *
     * @param <T>    泛型类型，表示目标对象的类型
     * @param target 目标对象，用于填充用户详情
     * @param uid    用户ID
     * @return 填充后的目标对象
     */
    public <T> T fillUserDetailsByPrivacy(T target, int uid) {
        AccountPrivacy accountPrivacy = privacyMapper.selectById(uid);
        Account account = accountMapper.selectById(uid);
        AccountDetails details = detailsMapper.selectById(uid);
        String[] ignores = accountPrivacy.hiddenFields();
        BeanUtils.copyProperties(account, target, ignores);
        BeanUtils.copyProperties(details, target, ignores);
        return target;
    }

}
