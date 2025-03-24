package com.example.controller.admin;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.response.AccountVO;
import com.example.service.AccountDetailsService;
import com.example.service.AccountPrivacyService;
import com.example.service.AccountService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/admin/user")
public class AccountAdminController {

    @Resource
    private AccountService accountService;

    @Resource
    private AccountDetailsService detailsService;

    @Resource
    private AccountPrivacyService privacyService;

    @Resource
    private StringRedisTemplate template;

    @Value("${spring.security.jwt.expire}")
    private int expire;

    /**
     * 获取账户列表
     *
     * @return 包含账户列表信息的RestBean对象
     */
    @GetMapping("/list")
    public RestBean<JSONObject> accountList(int page, int size) {
        JSONObject object = new JSONObject();
        List<AccountVO> list = accountService.page(Page.of(page, size))
                .getRecords()
                .stream().map(a -> a.asViewObject(AccountVO.class))
                .toList();
        object.put("total", accountService.count());
        object.put("list", list);
        return RestBean.success(object);
    }

    /**
     * 根据账户ID获取账户详情
     *
     * @param id 账户ID
     * @return RestBean<JSONObject> 包含账户详情和隐私设置的RestBean对象
     */
    @GetMapping("/detail")
    public RestBean<JSONObject> accountDetail(int id) {
        JSONObject object = new JSONObject();
        object.put("detail", detailsService.findAccountDetailsById(id));
        object.put("privacy", privacyService.getAccountPrivacy(id));
        return RestBean.success(object);
    }


    /**
     * 保存账户信息
     *
     * @param object 包含账户信息的JSON对象
     * @return RestBean<Void> 成功保存的响应对象
     */
    @PostMapping("/save")
    public RestBean<Void> saveAccount(@RequestBody JSONObject object) {
        int id = object.getInteger("id");
        Account account = accountService.findAccountById(id);
        Account save = object.toJavaObject(Account.class);
        this.handleBanned(account, save);
        BeanUtils.copyProperties(save, account, "password", "registerTime");
        accountService.saveOrUpdate(account);
        AccountDetails details = detailsService.findAccountDetailsById(id);
        AccountDetails saveDetails = object.getJSONObject("detail").toJavaObject(AccountDetails.class);
        BeanUtils.copyProperties(saveDetails, details);
        detailsService.saveOrUpdate(details);
        AccountPrivacy privacy = privacyService.getAccountPrivacy(id);
        AccountPrivacy savePrivacy = object.getJSONObject("privacy").toJavaObject(AccountPrivacy.class);
        BeanUtils.copyProperties(savePrivacy, privacy);
        privacyService.saveOrUpdate(savePrivacy);
        return RestBean.success();
    }

    /**
     * 处理被禁用的账户
     *
     * @param old     旧账户信息
     * @param current 当前账户信息
     */
    private void handleBanned(Account old, Account current) {
        String key = Const.BANNED_BLOCK + old.getId();
        if (!old.isBanned() && current.isBanned()) {
            template.opsForValue().set(key, "true", expire, TimeUnit.HOURS);
        } else if (old.isBanned() && !current.isBanned()) {
            template.delete(key);
        }
    }
}
