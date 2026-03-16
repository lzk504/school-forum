<script setup>
import {logout} from "@/net";
import {inject, reactive, ref} from "vue";
import {
    Bell,
    ChatDotSquare,
    Collection,
    DataLine,
    Document,
    Files,
    Location,
    Lock,
    Monitor,
    Notification,
    Operation,
    Orange,
    Position,
    School,
    Search,
    Tools,
    Umbrella,
    User,
} from "@element-plus/icons-vue";
import router from "@/router";
import LightCard from "@/components/LightCard.vue";
import {Check} from "@element-plus/icons";
import UserInfo from "@/components/UserInfo.vue";
import {apiNotificationDelete, apiNotificationDeleteAll, apiNotificationList} from "@/net/api/user";

const userMenu = [
    {
        title: '校园论坛', icon: Location, sub: [
            {title: '帖子广场', icon: ChatDotSquare, index: '/index'},
            {title: '失物招领', icon: Bell},
            {title: '校园活动', icon: Notification},
            {title: '表白墙', icon: Umbrella},
            {title: '海文考研', icon: School}
        ]
    }, {
        title: '探索与发现', icon: Position, sub: [
            {title: '成绩查询', icon: Document},
            {title: '班级课程表', icon: Files},
            {title: '教务通知', icon: Monitor},
            {title: '在线图书馆', icon: Collection},
            {title: '预约教室', icon: DataLine}
        ]
    }, {
        title: '实用工具', icon: Tools, sub: [
            {title: '番茄钟', icon: Orange, index: '/index/my-pomodoro'},
        ]
    }, {
        title: '个人设置', icon: Operation, sub: [
            {title: '个人信息设置', icon: User, index: '/index/user-setting'},
            {title: '账号安全设置', icon: Lock, index: '/index/privacy-setting'}
        ]
    }
]


const loading = inject('userLoading')

const searchInput = reactive({
    type: '1',
    text: ''
})

const notification = ref([])


// 加载通知
const loadNotification =
        () => apiNotificationList(data => notification.value = data)
loadNotification()

// 删除所有通知
function deleteAllNotification() {
    apiNotificationDeleteAll(() => loadNotification())
}

// 确认通知并跳转
function confirmNotification(id, url) {
    apiNotificationDelete(id, () => {
        loadNotification()
        window.open(url)
    })
}

function userLogout() {
    logout(() => router.push("/"))
}


</script>

<template>
    <div v-loading="loading" class="main-content" element-loading-text="正在进入，请稍后...">
        <el-container v-if="!loading" style="height: 100%">
            <el-header class="main-container-header">
                <div style="width: 320px;height: 32px">
                    <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
                </div>
                <div class="search-container">
                    <el-input v-model="searchInput.text" class="search-input" placeholder="搜索论坛相关内容...">
                        <template #prefix>
                            <el-icon>
                                <Search/>
                            </el-icon>
                        </template>
                        <template #append>
                            <el-select v-model="searchInput.type" placeholder="选择" style="width: 110px">
                                <el-option label="帖子广场" value="1"/>
                                <el-option label="失物招领" value="2"/>
                                <el-option label="校园活动" value="3"/>
                                <el-option label="表白墙" value="4"/>
                            </el-select>
                        </template>
                    </el-input>
                </div>
                <user-info>
                    <el-popover :width="350" placement="bottom" trigger="click">
                        <template #reference>
                            <el-badge :hidden="!notification.length" is-dot>
                                <div class="notification">
                                    <el-icon>
                                        <Bell/>
                                    </el-icon>
                                    <div style="font-size: 10px">消息</div>
                                </div>
                            </el-badge>
                        </template>
                        <el-empty v-if="!notification.length" :image-size="80" description="暂时没有未读消息哦~"/>
                        <el-scrollbar v-else :max-height="300">
                            <light-card v-for="item in notification" :key="item.id" :item="item"
                                        class="notification-item" @click="confirmNotification(item.id,item.url)">
                                <div>
                                    <el-tag :type="item.type" size="small">消息</el-tag>&nbsp;
                                    <span style="font-weight: bold">{{item.title}}</span>
                                </div>
                                <el-divider style="margin: 7px 0 3px 0"/>
                                <div>
                                    <div style="font-size: 13px;color: gray">
                                        {{item.content}}
                                    </div>
                                </div>
                            </light-card>
                        </el-scrollbar>
                        <div style="margin-top: 10px">
                            <el-button :icon="Check" plain size="small" style="width: 100%"
                                       type="info" @click="deleteAllNotification">
                                清除全部未读消息
                            </el-button>
                        </div>
                    </el-popover>
                </user-info>
            </el-header>
            <el-container>
                <el-aside width="230px">
                    <el-scrollbar style="height: calc(100vh - 55px)">
                        <el-menu
                                router
                                :default-active="$route.path"
                                :default-openeds="['1', '2', '3']"
                                style="min-height: calc(100vh - 55px)">
                            <el-sub-menu v-for="(menu, index) in userMenu"
                                         :index="(index + 1).toString()">
                                <template #title>
                                    <el-icon>
                                        <component :is="menu.icon"/>
                                    </el-icon>
                                    <span><b>{{menu.title}}</b></span>
                                </template>
                                <el-menu-item v-for="subMenu in menu.sub" :index="subMenu.index">
                                    <template #title>
                                        <el-icon>
                                            <component :is="subMenu.icon"/>
                                        </el-icon>
                                        {{subMenu.title}}
                                    </template>
                                </el-menu-item>
                            </el-sub-menu>
                        </el-menu>
                    </el-scrollbar>
                </el-aside>
                <el-main class="main-content-page">
                    <el-scrollbar style="height: calc(100vh - 55px)">
                        <router-view v-slot="{ Component }">
                            <transition mode="out-in" name="el-fade-in-linear">
                                <component :is="Component" style="height: 100%"/>
                            </transition>
                        </router-view>
                    </el-scrollbar>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style lang="less" scoped>
.notification-item {
    transition: background-color .3s;

    &:hover {
        opacity: 0.7;
    }
}

.notification {
    font-size: 22px;
    line-height: 14px;
    text-align: center;
    transition: color .3s;

    &:hover {
        color: grey;
        cursor: pointer;
    }
}

.main-content-page {
    padding: 0;
    background-color: #f7f8fa;
}

.dark .main-content-page {
    background-color: #212225;
}


.main-content {
    height: 100vh;
    width: 100vw;

    .main-container-header {
        border-bottom: solid 1px var(--el-border-color);
        height: 55px;
        display: flex;
        align-items: center;
        box-sizing: border-box;

        .search-container {
            flex: 1;
            padding: 0 20px;
            text-align: center;
        }

        .search-input {
            width: 100%;
            max-width: 500px;

        }
    }

    .logo {
        height: 32px;
    }

}
</style>

