<script setup>
import {EditPen, User} from "@element-plus/icons-vue";
import {apiUserDetailTotal, apiUserList, apiUserSave} from "@/net/api/user";
import {reactive, watchEffect} from "vue";
import {useStore} from "@/store";
import {ElMessage} from "element-plus";

const store = useStore()

const editor = reactive({
    id: 0,
    display: false,
    temp: {},
    loading: false,
})

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

// 打开用户编辑器
function openUserEditor(user) {
    editor.id = user.id;
    editor.display = true;
    editor.loading = true;
    apiUserDetailTotal(editor.id, data => {
        editor.temp = {...data, ...user};
        editor.loading = false;
    })


}

// 保存用户详情
function saveUserDetail() {
    editor.display = false;
    apiUserSave(editor.temp, () => {
        const result = user.data.find(item => item.id === editor.id)
        Object.assign(result, editor.temp)
        ElMessage.success("保存成功")
    })
}

watchEffect(() => {
    apiUserList(user.page, user.size, data => {
        user.total = data.total
        user.data = data.list
    })
})


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
            <el-form>
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
            </el-form>
            <template #footer>
                <div>
                    <div style="text-align: center">
                        <el-button type="success" @click="saveUserDetail">保存</el-button>
                        <el-button type="info" @click="editor.display=false">取消</el-button>
                    </div>
                </div>
            </template>

        </el-drawer>
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