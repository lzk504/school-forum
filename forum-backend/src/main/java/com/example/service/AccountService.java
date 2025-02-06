package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrEmail(String text);

    String registerEmailVerifyCode(String type, String email, String address);

    String registerEmailAccount(EmailRegisterVO info);

    String resetEmailAccountPassword(EmailResetVO info);

    String resetConfirm(ConfirmResetVO info);

    /**
     * 根据电子邮件地址查找账户
     */
    Account findAccountById(int id);

    /**
     * 修改用户邮箱
     */
    String ModifyEmail(int id, ModifyEmailVO vo);


    /**
     * 修改密码
     */
    String changePassword(int id, ChangePasswordVO vo);
}
