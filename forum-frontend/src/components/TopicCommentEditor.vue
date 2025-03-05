<script setup>
import {QuillEditor} from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import {ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const props = defineProps({
    show: Boolean,
    tid: String,
    quote: Number,
})

const content = ref()

const emit = defineEmits(["close"]);

// 发表评论
function submitComment() {
    post('/api/forum/add-comment', {
        tid: props.tid,
        quote: props.quote,
        content: JSON.stringify(content.value),
    }, success => {
        ElMessage.success('发表评论成功')
        emit("close")
    })
}
</script>

<template>
    <div>
        <el-drawer :close-on-click-modal="false"
                   :model-value="show"
                   direction="btt"
                   size="290" title="发表评论 "
                   @close="emit('close')">
            <div>
                <quill-editor v-model:content="content" placeholder="请发表友善的评论..." style="height: 120px"/>
            </div>
            <div style="margin-top: 10px;text-align: right">
                <el-button type="success" @click="submitComment">发表评论</el-button>
            </div>
        </el-drawer>
    </div>
</template>

<style scoped>
:deep(.el-drawer) {
    width: 800px;
    margin: 20px auto;
    border-radius: 10px;
}

:deep(.el-drawer__header) {
    margin: 0;
}

:deep(.el-drawer__body) {
    padding: 10px;
}
</style>