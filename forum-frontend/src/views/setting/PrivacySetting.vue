<script setup>

import Card from "@/components/Card.vue";
import {Setting, Switch} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const form = reactive({
  old_password: '',
  new_password: '',
  new_password_repeat: ''
})

const formRef = ref()
const valid = ref(false)
const onValidate = (prop, isValid) => valid.value = isValid
// 验证两次密码是否一致
const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.new_password) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

// 提交表单，重置密码
function resetPassword() {
  formRef.value.validate(valid => {
    if (valid) {
      post('/api/user/change-password', form, () => {
        ElMessage.success('修改密码成功！')
        formRef.value.resetFields();
      })
    }
  })
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
</script>

<template>
  <div style="margin:auto;max-width:600px;">
    <div style="margin-top: 20px">
      <card :icon="Setting" title="隐私设置" desc="在这里可以设置哪些内容可以被其他他人看到，请各位小伙伴注意自己的隐私">
        <div class="checkbox-list">
          <el-checkbox>公开我的手机号</el-checkbox>
          <el-checkbox>公开我的电子邮箱地址</el-checkbox>
          <el-checkbox>公开我的微信号</el-checkbox>
          <el-checkbox>公开我的QQ号</el-checkbox>
          <el-checkbox>公开我的性别</el-checkbox>
        </div>
      </Card>
      <card style="margin: 20px 0" :icon="Setting"
            title="修改密码" desc="修改密码请在这里进行操作，请务必牢记您的密码">
        <el-form :rules="rules" :model="form" ref="formRef" @validate="onValidate" label-width="100"
                 style="margin: 20px">
          <el-form-item label="当前密码" prop="password">
            <el-input type="password" v-model="form.old_password"
                      placeholder="当前密码" maxlength="16"/>
          </el-form-item>
          <el-form-item label="新密码" prop="new_password">
            <el-input type="password" v-model="form.new_password"
                      placeholder="新密码" maxlength="16"/>
          </el-form-item>
          <el-form-item label="重复新密码" prop="new_password_repeat">
            <el-input type="password" v-model="form.new_password_repeat"
                      placeholder="重新输入新密码" maxlength="16"/>
          </el-form-item>
          <div style="text-align: center">
            <el-button @click="resetPassword" :icon="Switch" type="success">立即重置密码</el-button>
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