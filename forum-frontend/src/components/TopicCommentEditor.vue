<script setup>
import {Delta, QuillEditor} from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {apiForumCommentAdd} from "@/net/api/forum";

const props = defineProps({
    show: Boolean,
    tid: String,
    quote: Object,
})

const content = ref()

const emit = defineEmits(["close"]);

const init = () => content.value = new Delta()

// 发表评论
function submitComment() {
    if (deltaToText(content).length > 2000) {
        ElMessage.error('评论字数不能超过2000')
    }
    apiForumCommentAdd({
        tid: props.tid,
        quote: props.quote ? props.quote.id : -1,
        content: JSON.stringify(content.value),
    }, success => {
        ElMessage.success('发表评论成功')
        emit("comment")
    })
}

// 将delta转为简单文本
function deltaToSimpleText(delta) {
    let str = ''
    for (let op of JSON.parse(delta).ops) {
        str += op.insert
    }
    if (str.length > 35) str = str.substring(0, 35) + "..."
    return str;
}

// 将delta转为文本（去除空格）
function deltaToText(delta) {
    if (!delta?.ops) return ""
    let str = ""
    for (let op of delta.ops)
        str += op.insert
    return str.replace(/\s/g, "")
}
</script>

<template>
    <div>
        <el-drawer :close-on-click-modal="false"
                   :title="quote ? `发表对评论: ${deltaToSimpleText(quote.content)} 的回复` : '发表帖子回复'"
                   :model-value="show"
                   direction="btt"
                   size="290" @open="init"
                   @close="emit('close')">
            <div>
                <quill-editor v-model:content="content" placeholder="请发表友善的评论..." style="height: 120px"/>
            </div>
            <div style="margin-top: 10px;display: flex">
                <div style="flex: 1;font-size: 13px;color: grey">
                    字数统计: {{deltaToText(content).length}}（最大支持2000字）
                </div>
                <el-button plain type="success" @click="submitComment">发表评论</el-button>
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