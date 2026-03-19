<script setup>
import {EditPen} from "@element-plus/icons-vue";
import {reactive} from "vue";
import {apiUserDetailTotal, apiUserSave} from "@/net/api/user";
import {ElMessage} from "element-plus";

const props = defineProps({
    user: Object
})

const editor = reactive({
    id: 0,
    display: false,
    temp: {},
    loading: false
})

defineExpose({ openUserEditor })

// 打开用户编辑器
function openUserEditor(user) {
    editor.id = user.id
    editor.display = true
    editor.loading = true
    apiUserDetailTotal(editor.id, data => {
        // 合并详情数据到用户数据中
        editor.temp = {
            id: user.id,
            username: user.username,
            email: user.email,
            role: user.role,
            avatar: user.avatar,
            registerTime: user.registerTime,
            mute: user.mute,
            banned: user.banned,
            gender: data.detail.gender,
            phone: data.detail.phone,
            qq: data.detail.qq,
            wx: data.detail.wx,
            desc: data.detail.desc,
            privacyPhone:data.privacy.phone,
            privacyEmail:data.privacy.email,
            privacyWx:data.privacy.wx,
            privacyQq:data.privacy.qq,
            privacyGender:data.privacy.gender
        }
        editor.loading = false
    })
}

// 保存用户详情
function saveUserDetail() {
    if (!editor.temp.username || !editor.temp.email) {
        ElMessage.warning('用户名和邮箱不能为空')
        return
    }

    // 构造符合后端要求的数据格式
    const saveData = {
        id: editor.temp.id,
        // 基础账户信息
        username: editor.temp.username,
        email: editor.temp.email,
        role: editor.temp.role,
        avatar: editor.temp.avatar,
        mute: editor.temp.mute,
        banned: editor.temp.banned,
        // 详情信息
        detail: {
            gender: editor.temp.gender,
            phone: editor.temp.phone,
            qq: editor.temp.qq,
            wx: editor.temp.wx,
            desc: editor.temp.desc || ''
        },
        // 隐私设置
        privacy: {
            gender: editor.temp.privacyGender,
            phone: editor.temp.privacyPhone,
            qq: editor.temp.privacyQq,
            wx: editor.temp.privacyWx,
            email:editor.temp.privacyEmail
        }
    }

    apiUserSave(saveData, () => {
        const result = props.user.data.find(item => item.id === editor.id)
        if (result) {
            Object.assign(result, editor.temp)
        }
        editor.display = false
        ElMessage.success("保存成功")
    }, (error) => {
        ElMessage.error('保存失败: ' + error)
    })
}
</script>

<template>
    <el-drawer v-model="editor.display">
        <template #header>
            <div>
                <div style="font-weight: bold">
                    <div>
                        <el-icon>
                            <EditPen/>
                        </el-icon>
                        编辑用户信息
                    </div>
                    <div style="font-size: 13px;color: gray">编辑完成后请点击下方保存按钮</div>

                </div>
            </div>
        </template>
        <div v-loading="editor.loading" element-loading-text="数据正在加载请稍后...">
            <el-form v-if="!editor.loading" label-position="top" style="height: 100%">
                <el-form-item label="用户名">
                    <el-input v-model="editor.temp.username"></el-input>
                </el-form-item>
                <el-form-item label="电子邮件">
                    <el-input v-model="editor.temp.email"></el-input>
                </el-form-item>
                <div style="display: flex;font-size: 14px;gap: 20px">
                    <div>
                        <span style="margin-right: 10px">禁言</span>
                        <el-switch v-model="editor.temp.mute"/>
                    </div>
                    <el-divider direction="vertical" style="height: 30px"></el-divider>
                    <div>
                        <span style="margin-right: 10px">封禁</span>
                        <el-switch v-model="editor.temp.banned"/>
                    </div>
                </div>
                <div style="margin-top: 10px;color: #606266;font-size: 14px">
                    注册时间{{new Date(editor.temp.registerTime).toLocaleDateString()}}
                </div>
                <el-divider/>
                <el-form-item label="性别">
                    <el-radio-group v-model="editor.temp.gender">
                        <el-radio :label="0">男</el-radio>
                        <el-radio :label="1">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input v-model="editor.temp.phone"/>
                </el-form-item>
                <el-form-item label="QQ账号">
                    <el-input v-model="editor.temp.qq"/>
                </el-form-item>
                <el-form-item label="微信账号">
                    <el-input v-model="editor.temp.wx"/>
                </el-form-item>
                <el-form-item label="个人简介">
                    <el-input v-model="editor.temp.desc" :rows="4" type="textarea"/>
                </el-form-item>
                <el-divider/>
                <div>
                    <div>隐私设置</div>
                    <el-checkbox v-model="editor.temp.privacyPhone">
                        公开展示用户手机号
                    </el-checkbox>
                    <el-checkbox v-model="editor.temp.privacyEmail">
                        公开展示电子邮箱
                    </el-checkbox>
                    <el-checkbox v-model="editor.temp.privacyWx">
                        公开展示用户的微信号
                    </el-checkbox>
                    <el-checkbox v-model="editor.temp.privacyQq">
                        公开展示用户qq号
                    </el-checkbox>
                    <el-checkbox v-model="editor.temp.privacyGender">
                        公开展示用户性别
                    </el-checkbox>
                </div>

            </el-form>
        </div>


        <template #footer>
            <div>
                <div style="text-align: center">
                    <el-button type="success" @click="saveUserDetail">保存</el-button>
                    <el-button type="info" @click="editor.display=false">取消</el-button>
                </div>
            </div>
        </template>

    </el-drawer>
</template>

<style scoped>

</style>