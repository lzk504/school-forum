<script setup>

import Card from "@/components/Card.vue";
import {User, Message, Select} from "@element-plus/icons-vue";
import {useStore} from "@/store";
import {computed, reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post, get, accessHeader} from "@/net";
import axios from "axios";


const store = useStore()
const registerTime = computed(() => new Date(store.user.registerTime).toLocaleString())
const emailFormRef = ref()
const baseFormRef = ref()
const desc = ref('')

const loading = reactive({
  form: true,
  base: false,
})

// 用户基本信息表单
const baseForm = reactive(({
  username: '',
  gender: 1,
  phone: '',
  qq: '',
  wx: '',
  desc: ''
}))

// 邮箱
const emailForm = reactive({
  email: '',
  code: ''
})

// 用户名验证表单
const validateForm = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符，只能是中文/英文'))
  } else {
    callback()
  }
}

// 保存基本信息
function saveDetails() {
  baseFormRef.value.validate(isValid => {
    if (isValid) {
      loading.base = true
      post('/api/user/save-details', baseForm, () => {
        ElMessage.success('保存成功')
        store.user.username = baseForm.username
        desc.value = baseForm.desc
        loading.base = false
      }, (message) => {
        ElMessage.error(message)
        loading.base = false
      })
    }
  })
}

//获取用户详细信息
get('/api/user/details', data => {
  baseForm.username = store.user.username
  baseForm.gender = data.gender
  baseForm.phone = data.phone
  baseForm.qq = data.qq
  baseForm.wx = data.wx
  baseForm.desc = desc.value = data.desc
  emailForm.email = store.user.email
  loading.form = false
})

const coldTime = ref(0)
const isEmailValid = ref(true)
const onValidate = (prop, isValid) => {
  if (prop === 'email')
    isEmailValid.value = isValid
}

// 修改邮箱验证码
function sendEmailCode() {
  emailFormRef.value.validate(isValid => {

    coldTime.value = 60
    get(`/api/auth/ask-code?email=${emailForm.email}&type=modify`, () => {
      ElMessage.success(`验证码已发送到,${emailForm.email},请注意查收`)
      const handle = setInterval(() => {
        coldTime.value--
        if (coldTime.value === 0) clearInterval(handle)
      }, 1000)
    }, (message) => {
      ElMessage.error(message)
      coldTime.value = 0
    })

  })
}

// 修改邮箱
function modifyEmail() {
  emailFormRef.value.validate(isValid => {
    if (isValid) {
      post('/api/user/modify', emailForm, () => {
        ElMessage.success('邮箱更新成功')
        store.user.email = emailForm.email
        emailForm.code = ''
      }, (message) => {
        ElMessage.error(message)
      })
    }
  })
}


// 表单验证规则
const rules = {
  username: [
    {validator: validateForm, trigger: ['blur', 'change']},
    {min: 2, max: 12, message: '用户名长度应在 2-12 个字符之间', trigger: ['blur', 'change']},
  ],
  phone: [
    {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: ['blur', 'change']}
  ],
  qq: [
    {pattern: /^\d{5,12}$/, message: '请输入正确的QQ号码', trigger: ['blur', 'change']}
  ],
  wx: [
    {pattern: /^[a-zA-Z0-9_-]{6,20}$/, message: '请输入正确的微信号', trigger: ['blur', 'change']}
  ],
  email: [
    {required: true, message: '请输入邮箱地址', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
    {pattern: /^\d{6}$/, message: '验证码为6位数字', trigger: ['blur', 'change']}
  ]
}

// 头像上传前验证
function beforeAvatarUpload(rawFile) {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('头像图片必须为 JPG/PNG 格式!')
    return false
  } else if (rawFile.size / 1024 > 100) {
    ElMessage.error('头像图片大小不能超过 100KB!')
    return false
  }
  return true
}

// 头像上传成功回复信息
function uploadSuccess(resp) {
  ElMessage.success('头像上传成功')
  store.user.avatar = resp.data
}
</script>

<template>
  <div style="display: flex">
    <div class="settings-left">
      <Card :icon="User" title="账号信息设置" desc="在这里编辑您的个人信息，您可以在隐私设置中选择是否展示这些信息 ">
        <el-form :model="baseForm" :rules="rules" ref="baseFormRef" label-position="top"
                 style="margin: 0 10px 10px 10px"
                 v-loading="loading.form"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="baseForm.username" maxlength="12"/>
          </el-form-item>
          <el-radio-group v-model="baseForm.gender">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="baseForm.phone" maxlength="11"/>
          </el-form-item>
          <el-form-item label="QQ号" prop="qq">
            <el-input v-model="baseForm.qq" maxlength="13"/>
          </el-form-item>
          <el-form-item label="微信" prop="wx">
            <el-input v-model="baseForm.wx" maxlength="20"/>
          </el-form-item>
          <el-form-item label="个人简介">
            <el-input type="textarea" :rows="6" v-model="baseForm.desc" maxlength="200"/>
          </el-form-item>

        </el-form>
        <div>
          <el-button @click="saveDetails" v-loading="loading.base" type="success">保存用户信息</el-button>
        </div>
      </Card>
      <Card style="margin-top: 10px" :icon="Message" title="电子邮件设置" desc="您可以在这里修改电子邮件地址">
        <el-form :model="emailForm" :rules="rules" ref="emailFormRef" label-position="top" @validate="onValidate"
                 style="margin: 0 10px 10px 10px">
          <el-form-item label="电子邮件" prop="email">
            <el-input v-model="emailForm.email"/>
          </el-form-item>
          <el-form-item prop="code">
            <el-row style="width: 100%" :gutter="10">
              <el-col :span="18">
                <el-input placeholder="请输入验证码" v-model="emailForm.code"></el-input>
              </el-col>
              <el-col :span="6">
                <el-button :icon="Select" type="success" @click="sendEmailCode"
                           :disabled="!isEmailValid || coldTime > 0"
                           style="width: 100%" plain> {{ coldTime > 0 ? '请稍后 ' + coldTime + ' 秒' : '获取验证码' }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <div>
            <el-button type="success" @click="modifyEmail">更新电子邮件</el-button>
          </div>
        </el-form>
      </Card>
    </div>
    <div class="settings-right">
      <div style="position: sticky;top: 20px">
        <card>
          <div style="text-align: center;padding: 5px 15px 0 15px">
            <el-avatar :size="70" :src="store.avatarUrl"/>
            <div style="margin: 5px 0">
              <el-upload
                  :action="axios.defaults.baseURL +'/api/image/avatar'"
                  :show-file-list="false"
                  :before-upload="beforeAvatarUpload"
                  :on-success="uploadSuccess"
                  :headers="accessHeader()"
              >
                <el-button size="small" round>修改头像</el-button>
              </el-upload>
            </div>
            <div style="font-weight: bold">你好, {{ store.user.username }}</div>
          </div>
          <el-divider style="margin: 10px 0"/>
          <div style="color: gray;font-size: 14px">
            {{ desc || '暂无简介~' }}
          </div>
        </card>
        <card style="margin-top: 10px;font-size: 14px">
          <div>账号注册时间: {{ registerTime }}</div>
          <div style="color: grey">欢迎加入我们的学习论坛！</div>
        </card>
      </div>
    </div>
  </div>
</template>

<style scoped>
.settings-left {
  width: 500px;
  flex: 1;
  margin: 20px;
}

.settings-right {
  width: 300px;
  margin: 20px 30px 20px 0;
}
</style>