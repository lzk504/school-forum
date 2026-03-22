<script setup>
import {EditPen, User} from "@element-plus/icons-vue";
import {apiUserList, apiUserModifyPassword} from "@/net/api/user";
import {reactive, ref, watchEffect} from "vue";
import {useStore} from "@/store";
import UserEditor from "@/components/UserEditor.vue";
import {Unlock} from "@element-plus/icons";
import {ElMessage, ElMessageBox} from "element-plus";

const store = useStore()

const editorRef = ref()

const user = reactive({
    page: 1,
    size: 10,
    total: 0,
    data: []
})

function userState(user) {
    if (user.mute && user.banned) {
        return "禁言中，封禁中";
    } else if (user.mute) {
        return "禁言中";
    } else if (user.banned) {
        return "封禁中";
    } else {
        return "正常";
    }
}

function changePassword({id, username}) {
    ElMessageBox.prompt(`您确定要修改${username}的密码吗`, '修改密码', {
        inputPattern: /^[A-Za-z\d]{6,20}$/,
        inputErrorMessage: '密码必须是6-20位字母或数字',
        callback: ({action, value}) => {
            if (action === 'confirm') {
                apiUserModifyPassword({id, newPassword: value},
                        () => ElMessage.success('密码修改成功'))
            }
        }
    })
}

watchEffect(() => {
    apiUserList(user.page, user.size, data => {
        user.total = data.total
        user.data = data.list
    })
})


// 打开用户编辑器
function openUserEditor(row) {
    if (editorRef.value) {
        editorRef.value.openUserEditor(row)
    }
}


</script>

<template>
    <div class="user-admin">
        <div class="title">
            <el-icon>
                <User/>
            </el-icon>
            用户列表
        </div>
        <div class="desc">
            这里是用户列表页面，包括账号信息、封禁和禁言处理。
        </div>
        <el-table :data="user.data" height="320">
            <el-table-column label="编号" prop="id" width="80"></el-table-column>
            <el-table-column label="用户名">
                <template #default="{row}">
                    <div class="table-username">
                        <el-avatar :size="30" :src="store.avatarUserUrl(row.avatar)"></el-avatar>
                        <div>
                            {{row.username}}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column align="center" label="角色">
                <template #default="{row}">
                    <el-tag v-if="row.role==='admin'" type="danger">
                        管理员
                    </el-tag>
                    <el-tag v-else>
                        普通用户
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="邮箱" prop="email"></el-table-column>
            <el-table-column label="注册日期">
                <template #default="{row}">
                    {{new Date(row.registerTime).toLocaleDateString()}}
                </template>
            </el-table-column>
            <el-table-column label="状态">
                <template #default="{row}">
                    {{userState(row)}}
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
                <template #default="{row}">
                    <el-button :disabled="row.role === 'admin'" :icon="Unlock" size="small" type="warning"
                               @click="changePassword(row)">
                        修改密码
                    </el-button>
                    <el-button :disabled="row.role === 'admin'" :icon="EditPen" size="small" type="primary"
                               @click="openUserEditor(row)">编辑
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination v-model:current-page="user.page"
                           v-model:page-size="user.size"
                           :total="user.total"
                           layout="total,sizes,prev,pager,next,jumper"/>
        </div>
        <user-editor ref="editorRef" :user="user"/>
    </div>
</template>

<style lang="less" scoped>
.user-admin {
    .title {
        font-weight: bold;
    }

    .desc {
        color: #bababa;
        font-size: 13px;
        margin-bottom: 10px;
    }

    .table-username {
        height: 30px;
        display: flex;
        align-items: center;
        gap: 10px;

    }

    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: right;
    }

    :deep(.el-drawer__header) {
        margin-bottom: 0;
    }
}
</style>