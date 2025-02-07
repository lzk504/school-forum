package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.mapper.AccountPrivacyMapper;
import com.example.service.AccountPrivacyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountPrivacyServiceImpl extends ServiceImpl<AccountPrivacyMapper, AccountPrivacy> implements AccountPrivacyService {

    /**
     * 保存隐私设置
     */
    @Override
    @Transactional
    public String savePrivacy(int id, PrivacySaveVO vo) {
        AccountPrivacy accountPrivacy = Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
        boolean status = vo.isStatus();
        switch (vo.getType()) {
            case "phone" -> accountPrivacy.setPhone(status);
            case "email" -> accountPrivacy.setEmail(status);
            case "wx" -> accountPrivacy.setWx(status);
            case "qq" -> accountPrivacy.setQq(status);
            case "gender" -> accountPrivacy.setGender(status);
        }
        boolean result = this.saveOrUpdate(accountPrivacy);
        return result ?  null : "未知错误，请联系管理员";
    }

    /**
     * 根据用户ID获取账户隐私信息
     *
     * @param id 用户ID
     * @return 返回对应ID的账户隐私信息对象，如果不存在则返回一个新的AccountPrivacy对象，该对象以给定的ID为参数进行初始化
     */
    @Override
    public AccountPrivacy getAccountPrivacy(int id) {
        return Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
    }
}
