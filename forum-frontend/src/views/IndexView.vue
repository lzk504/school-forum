<script setup>
import {get} from "@/net";
import {ref} from "vue";
import {useStore} from "@/store";
import {
  Bell,
  ChatDotSquare, DataLine,
  Document, Files,
  Location, Lock, Monitor, Notebook,
  Notification, Operation,
  Position,
  School,
  Umbrella, User
} from "@element-plus/icons-vue";

const store = useStore()
const loading = ref(true)

get('/api/user/info', (res) => {
  store.user = res
  loading.value = false
})
</script>

<template>
  <div class="main-container" v-loading="loading" element-loading-text="正在进入，请稍后...">
    <el-container style="height: 100%" v-if="!loading">
      <el-header class="main-container-header">
        <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
        <div style="flex: 1" class="user-info">
          <div class="profile">
            <div>{{ store.user.username }}</div>
            <div>{{ store.user.email }}</div>
          </div>
          <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
        </div>
      </el-header>
      <el-container>
        <el-aside width="230px">
          <el-scrollbar style="min-height: calc(100vh - 55px)">
            <el-menu style="min-height: calc(100vh - 55px)"
                     default-active="1-1">
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
                <el-menu-item>
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
              </el-sub-menu>>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        <el-main>Main</el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="less" scoped>

.main-container {
  height: 100vh;
  width: 100vw;

  .main-container-header {
    border-bottom: 1px solid var(--el-border-color);
    height: 55px;
    display: flex;
    align-items: center;
    box-sizing: border-box;
  }

  .logo {
    height: 32px;
  }

  .user-info {
    display: flex;
    justify-content: flex-end;
    align-items: center;

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
        color: gray;
      }
    }

  }
}
</style>
