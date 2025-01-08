package com.example.controller;


import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AccountVo;
import com.example.service.AccountService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Resource
    private AccountService accountService;

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
}
