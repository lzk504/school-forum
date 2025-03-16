<script setup>
import {isRoleAdmin, logout} from "@/net";
import router from "@/router";
import {ArrowRight, Back, Message, Operation} from "@element-plus/icons-vue";
import {useStore} from "@/store";
import {computed} from "vue";
import {useRoute} from "vue-router";

const route = useRoute();
const store = useStore();

// 判断当前页面是否为管理员界面
const isAdminPage = computed(() => route.fullPath.startsWith("/admin"))

function userLogout() {
    logout(() => router.push("/"))
}
</script>

<template>
    <div class="user-info">
        <template v-if="isRoleAdmin()">
            <el-button v-if="isAdminPage"
                       size="small"
                       type="primary"
                       @click="router.push('/index')"
            >
                回到用户端
                <el-icon><ArrowRight/></el-icon>
            </el-button>
            <el-button v-else size="small" type="danger" @click="router.push('/admin')">

                前往管理端
                <el-icon><ArrowRight/></el-icon>
            </el-button>
        </template>
        <slot/>
        <div class="profile">
            <div>{{store.user.username}}</div>
            <div>{{store.user.email}}</div>
        </div>
        <el-dropdown>
            <el-avatar :src="store.avatarUrl"/>
            <template #dropdown>
                <el-dropdown-item>
                    <el-icon>
                        <Operation/>
                    </el-icon>
                    个人设置
                </el-dropdown-item>
                <el-dropdown-item>
                    <el-icon>
                        <Message/>
                    </el-icon>
                    消息列表
                </el-dropdown-item>
                <el-dropdown-item divided @click="userLogout">
                    <el-icon>
                        <Back/>
                    </el-icon>
                    退出登录
                </el-dropdown-item>
            </template>
        </el-dropdown>
    </div>
</template>

<style lang="less" scoped>
.user-info {
    width: 320px;
    display: flex;
    gap: 20px;
    justify-content: flex-end;
    align-items: center;

    .el-avatar:hover {
        cursor: pointer;
    }


    .profile {
        text-align: right;

        :first-child {
            font-size: 18px;
            font-weight: bold;
            line-height: 20px;
        }

        :last-child {
            font-size: 10px;
            color: grey;
        }
    }
}
</style>