<script setup>
import {reactive, ref, watchEffect} from "vue";
import {apiForumTopicAllList, apiForumTypeList} from "@/net/api/forum";
import {useStore} from "@/store";

const store = useStore()
const topicList = reactive({
    list: [],
    page: 1,
    size: 10,
    total: 0,
})

const types = ref([])
const findType = (type) => types.value.find(item => item.id === type)

watchEffect(() => {
    apiForumTopicAllList(topicList.page, topicList.size, data => {
        topicList.list = data.list;
        topicList.total = data.total;
    })
})

apiForumTypeList(data => {
    types.value = data;
})
</script>

<template>
    <div class="forum-admin">
        <div class="title">
            <el-icon>
                <User/>
            </el-icon>
            论坛帖子列表
        </div>
        <div class="desc">
            在这里管理论坛的所有帖子
        </div>
        <el-table :data="topicList.list" height="400">
            <el-table-column align="center" label="帖子ID" prop="id" width="100"/>
            <el-table-column align="center" label="帖子标题" prop="title" show-overflow-tooltip/>
            <el-table-column label="帖子类型" >
                <template #default="{row}" >
                    <div class="topic-type">
                        <div :style="{backgroundColor:findType(row.type)?.color ?? '#bababa'}" class="type-dot"></div>
                        <div>{{findType(row.type)?.name ?? '未知类型'}}</div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="帖子作者">
                <template #default="{row}">
                    <div class="table-username">
                        <el-avatar :size="30" :src="store.avatarUserUrl(row.avatar)"></el-avatar>
                        <div>
                            {{row.username}}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="发帖时间">
                <template #default="{row}">
                    {{new Date(row.time).toLocaleDateString()}}
                </template>
            </el-table-column>
            <el-table-column align="center" fixed="right" label="操作">
                <template #default="{row}">
                    <el-button size="small" type="info">屏蔽</el-button>
                    <el-button size="small" type="primary">锁定</el-button>
                    <el-button size="small" type="danger">删除</el-button>
                </template>
            </el-table-column>

        </el-table>
        <div class="pagination">
            <el-pagination v-model:current-page="topicList.page"
                           v-model:page-size="topicList.size"
                           :total="topicList.total"

                           layout="total,sizes,prev,pager,next,jumper"/>
        </div>
    </div>
</template>

<style lang="less" scoped>
.forum-admin {
    .title {
        font-weight: bold;
    }

    .desc {
        color: #bababa;
        font-size: 13px;
        margin-bottom: 10px;
    }

    .fill-table {
        flex: 1;
    }

    .table-username {
        height: 30px;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .topic-type {
        display: flex;
        align-items: center;
        gap: 7px;

        .type-dot {
            height: 7px;
            width: 7px;
            border-radius: 50%;
        }
    }

    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: right;
    }
}
</style>