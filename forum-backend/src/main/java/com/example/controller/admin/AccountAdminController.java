package com.example.controller.admin;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.vo.response.AccountVo;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/admin/user")
public class AccountAdminController {

    @Resource
    private AccountService accountService;

    /**
     * 获取账户列表
     * @return 包含账户列表信息的RestBean对象
     */
    @GetMapping("/list")
    public RestBean<JSONObject> accountList(int page, int size) {
        JSONObject object = new JSONObject();
        List<AccountVo> list = accountService.page(Page.of(page, size))
                .getRecords()
                .stream().map(a -> a.asViewObject(AccountVo.class))
                .toList();
        object.put("total", accountService.count());
        object.put("list", list);
        return RestBean.success(object);
    }

}
