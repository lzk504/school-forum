<script setup>

import {
    Bell,
    ChatDotSquare,
    Collection,
    DataLine,
    Document,
    Files,
    Location,
    Monitor,
    Notification,
    Position,
    School,
    Umbrella,
    User
} from "@element-plus/icons-vue";
import UserInfo from "@/components/UserInfo.vue";
import {inject, onMounted, ref} from "vue";
import router from "@/router";
import {useRoute} from "vue-router";

const adminMenu = [
    {
        title: '校园论坛管理', icon: Location, sub: [
            {title: '用户管理', icon: User, index: '/admin/user'},
            {title: '帖子广场管理', icon: ChatDotSquare, index: '/admin/forum'},
            {title: '失物招领管理', icon: Bell},
            {title: '校园活动管理', icon: Notification},
            {title: '表白墙管理', icon: Umbrella},
            {title: '合作机构管理', icon: School}
        ]
    }, {
        title: '探索与发现管理', icon: Position, sub: [
            {title: '成绩管理', icon: Document},
            {title: '课程表管理', icon: Files},
            {title: '教务通知管理', icon: Monitor},
            {title: '在线图书馆管理', icon: Collection},
            {title: '预约教室管理', icon: DataLine}
        ]
    }
]
const route = useRoute()
const pageTabs = ref([])
const loading = inject('userLoading')

// 关闭标签页，但不删除最后一个
function handleTabClose(name) {
    const index = pageTabs.value.findIndex(tab => tab.name === name);
    const isCurrent = name === route.fullPath
    pageTabs.value.splice(index, 1)
    if (pageTabs.value.length > 0) {
        if (isCurrent) {
            //删除后跳转到上一个标签页，默认切换到上一个标签页，如果没有上一个，则切换到下一个标签页
            router.push(pageTabs.value[Math.max(0, index - 1)].name)
        }
    } else {
        router.push('/admin')
    }
}

// 添加标签页，但不重复添加
function addAdminTab(menu) {
    if (!menu.index) return
    if (pageTabs.value.findIndex(tab => tab.name === menu.index) < 0) {
        pageTabs.value.push({
            title: menu.title,
            name: menu.index
        })
    }
}

// 点击标签页跳转路由
function handleTabClick({props}) {
    router.push(props.name)
}

// 初始化页面标签页，默认选中当前路由对应的菜单项
onMounted(() => {
    const initPage = adminMenu
            .flatMap(menu => menu.sub)
            .find(sub => sub.index === route.fullPath)
    console.log(initPage)
    if (initPage) {
        addAdminTab(initPage)
    }
})
</script>

<template>
    <div v-loading="loading" class="admin-content" element-loading-text="正在进入，请稍后">
        <el-container style="height: 100%">
            <el-aside class="admin-content-aside" width="230px">
                <div class="logo-box">
                    <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
                </div>
                <el-scrollbar style="height: calc(100vh - 57px)">
                    <el-menu
                            :default-active="$route.path"
                            :default-openeds="['1', '2']"
                            router
                            style="min-height: calc(100vh - 57px);border: none">
                        <el-sub-menu v-for="(menu, index) in adminMenu"
                                     :index="(index + 1).toString()">
                            <template #title>
                                <el-icon>
                                    <component :is="menu.icon"/>
                                </el-icon>
                                <span><b>{{menu.title}}</b></span>
                            </template>
                            <el-menu-item
                                    v-for="subMenu in menu.sub"
                                    :index="subMenu.index"
                                    @click="addAdminTab(subMenu)"
                            >
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
            <el-container>
                <el-header class="admin-content-header">
                    <div style="flex: 1">
                        <el-tabs :model-value='route.fullPath' closable
                                 type="card"
                                 @tab-click="handleTabClick"
                                 @tab-remove="handleTabClose">
                            <el-tab-pane
                                    v-for="tab in pageTabs"
                                    :key="tab.name"
                                    :label="tab.title"
                                    :name="tab.name">

                            </el-tab-pane>
                        </el-tabs>
                    </div>
                    <user-info/>
                </el-header>
                <el-main class="admin-content-main">
                    <router-view v-slot="{ Component }">
                        <keep-alive>
                            <component :is="Component"/>
                        </keep-alive>
                    </router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>


<style lang="less" scoped>
.admin-content {
    height: 100vh;
    width: 100vw;

    .admin-content-aside {
        border-right: solid 1px var(--el-border-color);

        .logo-box {
            text-align: center;
            padding: 15px 0 10px;
            height: 32px;

            .logo {
                height: 32px;
            }
        }
    }

    .admin-content-header {
        border-bottom: solid 1px var(--el-border-color);
        height: 50px;
        display: flex;
        align-items: center;
        box-sizing: border-box;

        :deep(.el-tabs__header) {
            height: 32px;
            margin-bottom: 0;
            border-bottom: none;
        }

        :deep(.el-tabs__item) {
            height: 32px;
            padding: 0 15px;
            border-radius: 5px;
            border: solid 1px var(--el-border-color);
        }

        :deep(.el-tabs__nav) {
            gap: 10px;
            border: none;
        }
    }

}
</style>