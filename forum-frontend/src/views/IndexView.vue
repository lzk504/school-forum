<script setup>
import {get, logout} from "@/net";
import {reactive, ref} from "vue";
import {useStore} from "@/store";
import {
    Back,
    Bell,
    ChatDotSquare,
    Collection,
    DataLine,
    Document,
    Files,
    Location,
    Lock,
    Message,
    Monitor,
    Notification,
    Operation,
    Position,
    School,
    Search,
    Umbrella,
    User
} from "@element-plus/icons-vue";
import router from "@/router";
import LightCard from "@/components/LightCard.vue";
import {Check} from "@element-plus/icons";

const store = useStore()
const loading = ref(true)

const searchInput = reactive({
    type: '1',
    text: ''
})

const notification = ref([])

get('/api/user/info', (res) => {
    store.user = res
    loading.value = false
})

// 加载通知
const loadNotification =
        () => get('/api/notification/list', data => notification.value = data)
loadNotification()

// 删除所有通知
function deleteAllNotification() {
    get('/api/notification/delete-all', () => loadNotification())
}

// 确认通知并跳转
function confirmNotification(id, url) {
    get(`/api/notification/delete?id=${id}`, () => {
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
                <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
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
                <div class="user-info">
                    <el-popover :width="350" placement="bottom" trigger="click">
                        <template #reference>
                            <el-badge :hidden="!notification.length" is-dot style="margin-right: 15px">
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
                                <span>个人设置</span>
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <el-icon>
                                    <Message/>
                                </el-icon>
                                <span>消息列表</span>
                            </el-dropdown-item>
                            <el-dropdown-item divided @click="userLogout">
                                <el-icon>
                                    <Back/>
                                </el-icon>
                                <span>退出登录</span>
                            </el-dropdown-item>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container>
                <el-aside width="230px">
                    <el-scrollbar style="height: calc(100vh - 55px)">
                        <el-menu
                                :default-active="$route.path"
                                router
                                style="min-height: calc(100vh - 55px)">
                            <el-sub-menu index="1">
                                <template #title>
                                    <el-icon>
                                        <Location/>
                                    </el-icon>
                                    <span><b>校园论坛</b></span>
                                </template>
                                <el-menu-item index="/index">
                                    <template #title>
                                        <el-icon>
                                            <ChatDotSquare/>
                                        </el-icon>
                                        帖子广场
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Bell/>
                                        </el-icon>
                                        失物招领
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Notification/>
                                        </el-icon>
                                        校园活动
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Umbrella/>
                                        </el-icon>
                                        表白墙
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <School/>
                                        </el-icon>
                                        海文考研
                                        <el-tag size="small" style="margin-left: 10px">合作机构</el-tag>
                                    </template>
                                </el-menu-item>
                            </el-sub-menu>
                            <el-sub-menu index="2">
                                <template #title>
                                    <el-icon>
                                        <Position/>
                                    </el-icon>
                                    <span><b>探索与发现</b></span>
                                </template>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Document/>
                                        </el-icon>
                                        成绩查询
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Files/>
                                        </el-icon>
                                        班级课程表
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Monitor/>
                                        </el-icon>
                                        教务通知
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <Collection/>
                                        </el-icon>
                                        在线图书馆
                                    </template>
                                </el-menu-item>
                                <el-menu-item>
                                    <template #title>
                                        <el-icon>
                                            <DataLine/>
                                        </el-icon>
                                        预约教室
                                    </template>
                                </el-menu-item>
                            </el-sub-menu>
                            <el-sub-menu index="3">
                                <template #title>
                                    <el-icon>
                                        <Operation/>
                                    </el-icon>
                                    <span><b>个人设置</b></span>
                                </template>
                                <el-menu-item index="/index/user-setting">
                                    <template #title>
                                        <el-icon>
                                            <User/>
                                        </el-icon>
                                        个人信息设置
                                    </template>
                                </el-menu-item>
                                <el-menu-item index="/index/privacy-setting">
                                    <template #title>
                                        <el-icon>
                                            <Lock/>
                                        </el-icon>
                                        账号安全设置
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

    .user-info {
        display: flex;
        justify-content: flex-end;
        align-items: center;

        .el-avatar:hover {
            cursor: pointer;
        }


        .profile {
            text-align: right;
            margin-right: 20px;

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
}
</style>

