import {get, post} from "@/net";
import {useStore} from "@/store";
import {ElMessage} from "element-plus";
import router from "@/router";

// 获取用户信息
export const apiUserInfo = (loadingRef) => {
    loadingRef.value = true
    get('/api/user/info', (res) => {
        const store = useStore()
        store.user = res
        loadingRef.value = false
    })
}

// 用户登录
export const apiAuthRegister = (data) => {
    post('/api/auth/register', data, () => {
        ElMessage.success('注册成功，欢迎加入我们')
        router.push("/")
    })
}

//邮箱验证码
export const apiAuthAskCode = (email, coldTime, type = 'register') => {
    coldTime.value = 60
    get(`/api/auth/ask-code?email=${email}&type=${type}`, () => {
        ElMessage.success(`验证码已发送到邮箱: ${email}，请注意查收`)
        const handle = setInterval(() => {
            coldTime.value--
            if (coldTime.value === 0) {
                clearInterval(handle)
            }
        }, 1000)
    }, (message) => {
        ElMessage.warning(message)
        coldTime.value = 0
    })
}

// 用户登录
export const apiAuthResetConfirm = (data, active) => {
    post('/api/auth/reset-confirm', data, () => active.value++)
}

//重置密码
export const apiAuthResetPassWord = (data) => {
    post('/api/auth/reset-password', data, () => {
        ElMessage.success('密码重置成功，请重新登录')
        router.push('/')
    })
}

// 获取隐私设置
export const apiUserPrivacy = (success) => {
    get('/api/user/privacy', success)
}

// 更新隐私设置
export const apiUserPrivacyUpdate = (data, loadingRef) => {
    loadingRef.value = true
    post('/api/user/save-privacy', data, () => {
        ElMessage.success('修改设置成功!')
        loadingRef.value = false
    })
}

// 用户修改密码
export const apiUserChangePassword = (data, success) => {
    post('/api/user/change-password', data, success)
}

// 获取用户详情
export const apiUserDetail = (success) => {
    get('/api/user/details', success)
}

// 保存用户详情信息
export const apiUserDetailSave = (data, success, failure) => {
    post('/api/user/save-details', data, success, failure)
}

// 修改邮箱
export const apiUserEmailModify = (data, success, failure) => {
    post('/api/user/modify', data, success, failure)

}
// 获取通知列表
export const apiNotificationList = (success) => {
    get('/api/notification/list', success)
}

// 删除通知
export const apiNotificationDelete = (id, success) => {
    get(`/api/notification/delete?id=${id}`, success)
}
// 删除所有通知
export const apiNotificationDeleteAll = (success) => {
    get('/api/notification/delete-all', success)
}

// 获取用户列表
export const apiUserList = (page, size, success) => {
    get(`/api/admin/user/list?page=${page}&size=${size}`, success)
}

// 获取用户详情信息
export const apiUserDetailTotal = (id, success) => {
    get(`/api/admin/user/detail?id=${id}`, success)
}

// 保存用户信息
export const apiUserSave = (data,success) => {
    post('/api/admin/user/save', data,success)
}