<script setup>
import {ref} from "vue";
import LightCard from "@/components/LightCard.vue";
import router from "@/router";
import TopicTag from "@/components/TopicTag.vue";
import {apiForumCollectDelete, apiForumCollects} from "@/net/api/forum";
import {ElMessage} from "element-plus";

defineProps({
    show: false,
})

const emit = defineEmits(['close'])

const list = ref([])

// 初始化收藏帖子数据
function init() {
    apiForumCollects(data => list.value = data)
}

// 删除帖子收藏
function deleteCollect(index, tid) {
    apiForumCollectDelete(tid, () => {
        ElMessage.success('删除帖子收藏成功')
        list.value.splice(index, 1)
    })
}
</script>

<template>
    <el-drawer :model-value="show" @close="emit('close')" @open="init" title="我的帖子收藏列表">
        <div class="collect-list">
            <light-card v-for="(item,index) in list" class="topic-card"
                        @click="router.push(`/index/topic-detail/${item.id}`)">
                <topic-tag :type="item.type"/>
                <div class="topic-title">
                    <b>{{item.title}}</b>
                </div>
                <el-link type="danger" @click.stop="deleteCollect(index,item.id)">删除</el-link>
            </light-card>
        </div>
    </el-drawer>
</template>

<style scoped lang="less">
.collect-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.topic-card {
    background-color: rgba(128, 128, 128, 0.2);
    transition: .3s;
    display: flex;
    justify-content: space-between;

    .topic-title {
        margin-left: 5px;
        font-size: 14px;
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    &:hover {
        cursor: pointer;
        transform: scale(1.02);
    }
}
</style>