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
    public RestBean<Void> saveAccount(@RequestBody JSONObject object, @RequestAttribute(Const.ATTR_USER_ID) int uid) {

        int id = object.getInteger("id");
        System.out.println("保存用户信息，用户ID: " + id);

        if (uid == id) {
            return RestBean.failure(400, "不能修改自己的账号信息");
        }

        Account account = accountService.findAccountById(id);
        System.out.println("找到账户: " + (account != null ? "存在" : "不存在"));

        Account save = object.toJavaObject(Account.class);
        this.handleBanned(account, save);
        BeanUtils.copyProperties(save, account, "password", "registerTime");
        accountService.saveOrUpdate(account);

        AccountDetails details = detailsService.findAccountDetailsById(id);

        AccountDetails saveDetails = object.getJSONObject("detail") != null ?
                object.getJSONObject("detail").toJavaObject(AccountDetails.class) : (details != null ? details : new AccountDetails());

        if (details != null) {
            details.setGender(saveDetails.getGender());
            details.setPhone(saveDetails.getPhone());
            details.setQq(saveDetails.getQq());
            details.setWx(saveDetails.getWx());
            details.setDesc(saveDetails.getDesc());
            detailsService.saveOrUpdate(details);
        } else {
            // 如果记录不存在，创建新记录
            saveDetails.setId(id);
            detailsService.saveOrUpdate(saveDetails);
        }

        AccountPrivacy privacy = privacyService.getAccountPrivacy(id);
        AccountPrivacy savePrivacy = object.getJSONObject("privacy") != null ?
                object.getJSONObject("privacy").toJavaObject(AccountPrivacy.class) : privacy;

        // 如果有隐私设置，更新现有记录
        if (savePrivacy != null) {
            privacy.setPhone(savePrivacy.isPhone());
            privacy.setEmail(savePrivacy.isEmail());
            privacy.setQq(savePrivacy.isQq());
            privacy.setWx(savePrivacy.isWx());
            privacy.setGender(savePrivacy.isGender());
            privacyService.saveOrUpdate(privacy);
            System.out.println("更新隐私设置完成");
        } else {
            privacyService.saveOrUpdate(privacy);
            System.out.println("保存默认隐私设置完成");
        }

        return RestBean.success();
    }

    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestBody JSONObject jsonObject, @RequestAttribute(Const.ATTR_USER_ID) int uid){
        int id = jsonObject.getInteger("id");
        String newPassword = jsonObject.getString("newPassword");
        if (id == uid) {
            return RestBean.failure(400, "不能修改自己的密码");
        }
        accountService.modifyPassword(id, newPassword);
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
