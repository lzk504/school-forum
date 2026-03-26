<script setup>
import {reactive, ref, watchEffect} from "vue";
import {
    apiForumTopicAllList,
    apiForumTopicDelete,
    apiForumTopicLocked,
    apiForumTopicTop,
    apiForumTypeList
} from "@/net/api/forum";
import {useStore} from "@/store";
import {ElMessage, ElMessageBox} from "element-plus";

const store = useStore()
const topicList = reactive({
    list: [],
    page: 1,
    size: 10,
    total: 0,
})

const types = ref([])
const findType = (type) => types.value.find(item => item.id === type)

const deleteTopic = row => {
    const {id, username} = row;
    ElMessageBox.confirm(`您确定要删除${username},的帖子吗？`, "删除帖子", {
        callback: value => {
            if (value === 'confirm') {
                apiForumTopicDelete(id, () => {
                    refreshList()
                    ElMessage.success("删除成功")
                }, () => {
                    ElMessage.error("删除失败")
                })
            }
        }
    })
}

const setTopTopic = (tid, status) => {
    apiForumTopicTop({tid, status}, data => {
        ElMessage.success("帖子置顶状态修改成功")
        refreshList()
    })
}

const setTopicLocked = (tid, status) => {
    apiForumTopicLocked({tid, status}, data => {
        ElMessage.success("帖子锁定状态修改成功")
        refreshList()
    })
}

const refreshList = () => {
    apiForumTopicAllList(topicList.page, topicList.size, data => {
        topicList.list = data.list;
        topicList.total = data.total;
    })
}

watchEffect(() => {
    refreshList()
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
            <el-table-column label="帖子类型">
                <template #default="{row}">
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
                    <el-button plain size="small" type="info">屏蔽</el-button>
                    <el-button plain size="small" type="primary" v-if="row.locked" @click="setTopicLocked(row.id,false)">
                        解锁
                    </el-button>
                    <el-button plain size="small" type="success" v-else @click="setTopicLocked(row.id,true)">锁定
                    </el-button>
                    <el-button v-if="row.top" plain size="small" type="warning" @click="setTopTopic(row.id,false)">取消
                    </el-button>
                    <el-button v-else plain size="small" type="success" @click="setTopTopic(row.id,true)">置顶
                    </el-button>
                    <el-button plain size="small" type="danger" @click="deleteTopic(row)">删除</el-button>
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