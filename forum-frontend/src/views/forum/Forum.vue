<script setup>
import {useStore} from '@/store'
import {get} from "@/net";

const store = useStore()
// 获取板块类型数据
get('/api/forum/types', data => {
            const array = []
            array.push({name: '全部', id: 0, color: 'linear-gradient(45deg, white, red, orange, gold, green, blue)'})
            data.forEach(d => array.push(d))
            store.forum.types = array
        }
)
</script>

<template>
    <div>
        <router-view v-slot="{ Component }">
            <transition mode="out-in" name="el-fade-in-linear">
                <keep-alive include="TopicList">
                    <component :is="Component"/>
                </keep-alive>
            </transition>
        </router-view>
        <!--回到顶部按钮-->
        <el-backtop :bottom="70" :right="20" target=".main-content-page .el-scrollbar__wrap"></el-backtop>
    </div>
</template>

<style scoped>

</style>