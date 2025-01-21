<script setup>
import {get, logout} from "@/net";
import {reactive, ref} from "vue";
import {useStore} from "@/store";
import {
  Back,
  Bell,
  ChatDotSquare, DataLine,
  Document, Files,
  Location, Lock, Message, Monitor, Notebook,
  Notification, Operation,
  Position,
  School, Search,
  Umbrella, User
} from "@element-plus/icons-vue";
import router from "@/router";

const store = useStore()
const loading = ref(true)

const searchInput = reactive({
  type: '1',
  text: ''
})

get('/api/user/info', (res) => {
  store.user = res
  loading.value = false
})

function userLogout() {
  logout(() => router.push("/"))
}

</script>

<template>
  <div class="main-content" v-loading="loading" element-loading-text="正在进入，请稍后...">
    <el-container style="height: 100%" v-if="!loading">
      <el-header class="main-container-header">
        <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
        <div class="search-container">
          <el-input class="search-input" v-model="searchInput.text" placeholder="搜索论坛相关内容...">
            <template #prefix>
              <el-icon>
                <Search/>
              </el-icon>
            </template>
            <template #append>
              <el-select style="width: 110px" v-model="searchInput.type" placeholder="选择">
                <el-option value="1" label="帖子广场"/>
                <el-option value="2" label="失物招领"/>
                <el-option value="3" label="校园活动"/>
                <el-option value="4" label="表白墙"/>
              </el-select>
            </template>
          </el-input>
        </div>
        <div class="user-info">
          <div class="profile">
            <div>{{ store.user.username }}</div>
            <div>{{ store.user.email }}</div>
          </div>
          <el-dropdown>
            <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
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
              <el-dropdown-item @click="userLogout" divided>
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
        <el-aside width="220px">
          <el-scrollbar style="min-height: calc(100vh - 55px)">
            <el-menu
                router
                :default-active="$route.path"
                style="min-height: calc(100vh - 55px)">
              <el-sub-menu index="1">
                <template #title>
                  <el-icon>
                    <Location/>
                  </el-icon>
                  <span><b>校园论坛</b></span>
                </template>
                <el-menu-item index="1-1">
                  <template #title>
                    <el-icon>
                      <ChatDotSquare/>
                    </el-icon>
                    <span>帖子广场</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Bell/>
                    </el-icon>
                    <span>失物招领</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Notification/>
                    </el-icon>
                    <span>校园活动</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Umbrella/>
                    </el-icon>
                    <span>表白墙</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <School/>
                    </el-icon>
                    <span>海文考研</span>
                    <el-tag style="margin-left: 10px " size="small">合作机构</el-tag>
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
                    <span>成绩查询</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Files/>
                    </el-icon>
                    <span>班级课程表</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Monitor/>
                    </el-icon>
                    <span>教务通知</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Notebook/>
                    </el-icon>
                    <span>在线图书馆</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <DataLine/>
                    </el-icon>
                    <span>预约教室</span>
                  </template>
                </el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="3">
                <template #title>
                  <el-icon>
                    <Operation/>
                  </el-icon>
                  <span><b>个人中心</b></span>
                </template>
                <el-menu-item index="/index/user-setting">
                  <template #title>
                    <el-icon>
                      <User/>
                    </el-icon>
                    <span>个人信息设置</span>
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon>
                      <Lock/>
                    </el-icon>
                    <span>账户安全设置</span>
                  </template>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        <el-main class="main-content-page">
          <el-scrollbar style="height: calc(100vh - 55px)">
            <router-view v-slot="{ Component }">
              <transition name="el-fade-in-linear" mode="out-in">
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

