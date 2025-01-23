package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;

public interface AccountDetailsService extends IService<AccountDetails> {
    //通过账户ID查找账户详情
    AccountDetails findAccountDetailsById(int id);
    //保存账户详情
    boolean saveAccountDetails(int id, DetailsSaveVO vo);
}
