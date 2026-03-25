<script setup>
import {apiEmailRecordList, apiEmailResend} from "@/net/api/email";
import {reactive, watchEffect} from "vue";
import {RefreshLeft} from "@element-plus/icons";
import {ElMessage, ElMessageBox} from "element-plus";

const emailList = reactive({
    list: [],
    total: 0,
    page: 1,
    size: 10,
})

const resendEmail = (row) => {
    const {id, email} = row
    ElMessageBox.confirm(`您确定要重新发送${email},的验证码吗？`, "重发邮件", {
        callback: value => {
            if (value === 'confirm') {
                apiEmailResend(id,()=>{
                    ElMessage.success("邮件发送成功")
                    row.status = 0
                },()=>{
                    ElMessage.error("邮件重发失败")
                    row.status = 2
                })
            }
        }
    })
    apiEmailResend(id)
}

watchEffect(() => {
    apiEmailRecordList(emailList.page, emailList.size, data => {
        emailList.list = data.list
        emailList.total = data.total
    })
})

</script>

<template>
    <div class="email-admin">
        <div class="title">
            <el-icon>
                <User/>
            </el-icon>
            论坛邮件列表
        </div>
        <div class="desc">
            在这里管理论坛的所有发送的邮件，并操作重发
        </div>
        <el-table :data="emailList.list" height="400">
            <el-table-column align="center" label="ID" prop="id" width="100"/>
            <el-table-column align="center" label="收件人" prop="email" show-overflow-tooltip />
            <el-table-column align="center" label="发送状态" prop="status">
                <template #default="{row}">
                    <el-tag v-if="row.status === 0" type="info">发送中</el-tag>
                    <el-tag v-if="row.status === 1" type="success">已发送</el-tag>
                    <el-tag v-if="row.status === 2" type="danger">发送失败</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" label="邮件主题" prop="title" show-overflow-tooltip />
            <el-table-column align="center" label="邮件内容" prop="content" show-overflow-tooltip />
            <el-table-column align="center" label="发送时间" prop="time" >
                <template #default="{row}">
                    {{new Date(row.time).toLocaleDateString()}}
                </template>
            </el-table-column>
            <el-table-column align="center" fixed="right" label="操作">
                <template #default="{row}">
                    <el-button :disabled="row.status!==2" :icon="RefreshLeft"
                               size="small"
                               type="primary"
                                @click="resendEmail(row)"
                    >重新发送
                    </el-button>
                </template>
            </el-table-column>

        </el-table>
        <div class="pagination">
            <el-pagination v-model:current-page="emailList.page"
                           v-model:page-size="emailList.size"
                           :total="emailList.total"

                           layout="total,sizes,prev,pager,next,jumper"/>
        </div>
    </div>
</template>

<style lang="less" scoped>
.email-admin {
    .title {
        font-weight: bold;
    }

    .desc {
        color: #bababa;
        font-size: 13px;
        margin-bottom: 10px;
    }

    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: right;
    }
}
</style>