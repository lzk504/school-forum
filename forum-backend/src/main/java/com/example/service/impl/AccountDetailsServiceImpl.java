package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.mapper.AccountDetailsMapper;
import com.example.service.AccountDetailsService;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService {

    @Resource
    private AccountService accountService;

    /**
     * 通过账户ID查找账户详情
     *
     * @param id 账户ID
     * @return 账户详情对象，如果未找到则返回null
     */
    @Override
    public AccountDetails findAccountDetailsById(int id) {
        return this.getById(id);
    }

    /**
     * 保存账户详情
     *
     * @param id 账户ID
     * @param vo 包含要保存的账户详情的DetailsSaveVo对象
     * @return 保存成功返回true，失败返回false
     */
    @Override
    @Transactional
    public synchronized boolean saveAccountDetails(int id, DetailsSaveVO vo) {
        Account account = accountService.findAccountByNameOrEmail(vo.getUsername());
        if (account == null || account.getId() == id) {
            if(accountService.update()
                    .eq("id", id)
                    .set("username", vo.getUsername())
                    .update()){
                this.saveOrUpdate(new AccountDetails(
                        id, vo.getGender(), vo.getPhone(),
                        vo.getQq(), vo.getWx(), vo.getDesc()
                ));
                return true;
            }
        }
        return false;
    }

}
