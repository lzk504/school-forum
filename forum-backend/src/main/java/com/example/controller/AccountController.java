package com.example.controller;


import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.ChangePasswordVO;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.entity.vo.request.ModifyEmailVO;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.entity.vo.response.AccountDetailsVO;
import com.example.entity.vo.response.AccountPrivacyVO;
import com.example.entity.vo.response.AccountVO;
import com.example.service.AccountDetailsService;
import com.example.service.AccountPrivacyService;
import com.example.service.AccountService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Resource
    private AccountService accountService;

    @Resource
    private AccountDetailsService detailsService;

    @Resource
    private AccountPrivacyService privacyService;

    /**
     * 获取账户信息
     *
     * @param id 用户ID，通过@RequestAttribute注解从请求属性中获取
     */
    @GetMapping("/info")
    public RestBean<AccountVO> info(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        Account account = accountService.findAccountById(id);
        return RestBean.success(account.asViewObject(AccountVO.class));
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

    /**
     * 修改用户邮箱
     *
     * @param id 用户ID，通过@RequestAttribute注解从请求头中获取
     * @param vo 包含修改邮箱所需信息的对象，通过@RequestBody注解从请求体中获取
     * @return 修改结果，成功返回RestBean.success()，失败返回RestBean.failure(400, result)，其中result为错误信息
     */
    @PostMapping("/modify")
    public RestBean<Void> modifyEmail(@RequestAttribute(Const.ATTR_USER_ID) int id, @RequestBody ModifyEmailVO vo) {
        return RestBean.messageHandle(() -> accountService.ModifyEmail(id, vo));
    }

    /**
     * 修改用户密码
     *
     * @param id 用户ID，通过@RequestAttribute注解从请求中获取
     * @param vo 包含新密码和旧密码的VO对象，通过@RequestBody注解从请求体中获取
     * @return 如果密码修改成功，返回状态码为200的RestBean对象，表示操作成功；如果密码修改失败，返回状态码为400的RestBean对象，并附带错误信息
     */
    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                         @RequestBody @Valid ChangePasswordVO vo) {
        return RestBean.messageHandle(() -> accountService.changePassword(id, vo));
    }

    /**
     * 保存隐私信息
     *
     * @param id 用户ID，从请求属性中获取
     * @param vo 隐私信息保存对象，通过请求体接收
     * @return RestBean对象，包含操作结果和消息
     */
    @PostMapping("/save-privacy")
    public RestBean<Void> savePrivacy(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid PrivacySaveVO vo) {
        return RestBean.messageHandle(() -> privacyService.savePrivacy(id, vo));
    }

    /**
     * 获取用户的隐私设置
     *
     * @param id 用户ID，从请求属性中获取
     * @return RestBean对象，包含操作结果和用户隐私设置信息
     */
    @GetMapping("/privacy")
    public RestBean<AccountPrivacyVO> privacy(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        return RestBean.success(privacyService.getAccountPrivacy(id).asViewObject(AccountPrivacyVO.class));
    }
}
