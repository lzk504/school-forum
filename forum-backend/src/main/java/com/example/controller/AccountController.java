package com.example.controller;


import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.entity.vo.response.AccountDetailsVO;
import com.example.entity.vo.response.AccountVo;
import com.example.service.AccountDetailsService;
import com.example.service.AccountService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Resource
    private AccountService accountService;

    @Resource
    private AccountDetailsService detailsService;

    /**
     * 获取账户信息
     *
     * @param id 用户ID，通过@RequestAttribute注解从请求属性中获取
     */
    @GetMapping("/info")
    public RestBean<AccountVo> info(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        Account account = accountService.findAccountById(id);
        return RestBean.success(account.asViewObject(AccountVo.class));
    }

    /**
     * 获取账户详情
     *
     * @param id 用户ID，通过@RequestAttribute注解从请求中获取
     * @return 包含账户详情的RestBean对象
     */
    @GetMapping("/details")
    public RestBean<AccountDetailsVO> details(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        AccountDetails details = Optional.ofNullable(detailsService.findAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBean.success(details.asViewObject(AccountDetailsVO.class));
    }

    /**
     * 保存账户详情
     *
     * @param id 用户ID，通过@RequestAttribute注解从请求中获取
     * @param vo 包含要保存的账户详情的DetailsSaveVO对象
     */
    @PostMapping("/save-details")
    public RestBean<Void> saveDetails(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody DetailsSaveVO vo) {
        boolean result = detailsService.saveAccountDetails(id, vo);
        return result ? RestBean.success() : RestBean.failure(400, "此用户名已经被其他用户使用！");
    }
}
