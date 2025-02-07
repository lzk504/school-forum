package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;

public interface AccountPrivacyService extends IService<AccountPrivacy> {

    /**
     * 保存隐私信息
     */
    String savePrivacy(int id, PrivacySaveVO vo);

    /**
     * 根据用户ID获取账户隐私信息
     */
    AccountPrivacy getAccountPrivacy(int id);
}
