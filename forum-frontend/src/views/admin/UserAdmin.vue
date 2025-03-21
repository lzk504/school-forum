<script setup>

import {User} from "@element-plus/icons-vue";
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
        <el-table>

        </el-table>
    </div>
</template>

<style scoped>
.user-admin {
   .title{
       font-weight: bold;
   }
    .desc {
        color:#bababa;
        font-size: 13px;
        margin-bottom: 10px;
    }
}
</style>