<script setup>

import Card from "@/components/Card.vue";
import {Setting, Switch} from "@element-plus/icons-vue";
import {onMounted, reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {apiUserChangePassword, apiUserPrivacy, apiUserPrivacyUpdate} from "@/net/api/user";

const form = reactive({
    old_password: '',
    new_password: '',
    new_password_repeat: ''
})


// 验证两次密码是否一致
const validatePassword = (_, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== form.new_password) {
        callback(new Error("两次输入的密码不一致"))
    } else {
        callback()
    }
}
// 表单验证规则
const rules = {
    old_password: [
        {required: true, message: '请输入原来的密码', trigger: 'blur'}
    ],
    new_password: [
        {required: true, message: '请输入新的密码', trigger: 'blur'},
        {min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur']}
    ],
    new_password_repeat: [
        {required: true, message: '请再次输入新的密码', trigger: 'blur'},
        {validator: validatePassword, trigger: ['blur', 'change']},
    ]
}
const formRef = ref()

const valid = ref(false)
const onValidate = (prop, isValid) => valid.value = isValid
const saveLoading = ref(true)
const privacy = reactive({
    phone: '',
    email: '',
    qq: '',
    wx: '',
    gender: ''
})

// 提交表单，重置密码
function resetPassword() {
    formRef.value.validate(valid => {
        if (valid) {
            apiUserChangePassword(form, () => {
                ElMessage.success('修改密码成功！')
                formRef.value.resetFields();
            })
        }
    })
}


// 更新隐私设置
function updatePrivacy(type, status) {
    apiUserPrivacyUpdate({type, status}, saveLoading)
}

onMounted(() => {
    // 获取隐私设置数据
    apiUserPrivacy(data => {
        Object.assign(privacy, data)
        saveLoading.value = false
    })
})
</script>

<template>
    <div style="margin:auto;max-width:600px;">
        <div style="margin-top: 20px">
            <card v-loading="saveLoading" :icon="Setting" desc="在这里可以设置哪些内容可以被其他他人看到，请各位小伙伴注意自己的隐私"
                  title="隐私设置">
                <div class="checkbox-list">
                    <el-checkbox v-model="privacy.phone" @change="updatePrivacy('phone',privacy.phone)">公开我的手机号
                    </el-checkbox>
                    <el-checkbox v-model="privacy.email" @change="updatePrivacy('email',privacy.email)">公开我的电子邮箱地址
                    </el-checkbox>
                    <el-checkbox v-model="privacy.wx" @change="updatePrivacy('wx',privacy.wx)">公开我的微信号
                    </el-checkbox>
                    <el-checkbox v-model="privacy.qq" @change="updatePrivacy('qq',privacy.qq)">公开我的QQ号
                    </el-checkbox>
                    <el-checkbox v-model="privacy.gender" @change="updatePrivacy('gender',privacy.gender)">公开我的性别
                    </el-checkbox>
                </div>
            </Card>
            <card :icon="Setting" desc="修改密码请在这里进行操作，请务必牢记您的密码"
                  style="margin: 20px 0" title="修改密码">
                <el-form ref="formRef" :model="form" :rules="rules" label-width="100" style="margin: 20px"
                         @validate="onValidate">
                    <el-form-item label="当前密码" prop="password">
                        <el-input v-model="form.old_password" maxlength="16"
                                  placeholder="当前密码" type="password"/>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_password">
                        <el-input v-model="form.new_password" maxlength="16"
                                  placeholder="新密码" type="password"/>
                    </el-form-item>
                    <el-form-item label="重复新密码" prop="new_password_repeat">
                        <el-input v-model="form.new_password_repeat" maxlength="16"
                                  placeholder="重新输入新密码" type="password"/>
                    </el-form-item>
                    <div style="text-align: center">
                        <el-button :icon="Switch" type="success" @click="resetPassword">立即重置密码</el-button>
                    </div>
                </el-form>
            </card>
        </div>
    </div>
</template>

<style scoped lang="less">
.checkbox-list {
    margin: 10px 0 0 10px;
    display: flex;
    flex-direction: column;
}
</style>