<script setup>

import {useRoute} from "vue-router";
import {get, post} from "@/net";
import {reactive, ref} from "vue";
import {ArrowLeft, CircleCheck, EditPen, Female, Male, Plus, Star} from "@element-plus/icons-vue";
import {QuillDeltaToHtmlConverter} from 'quill-delta-to-html';
import Card from "@/components/Card.vue";
import router from "@/router";
import TopicTag from "@/components/TopicTag.vue";
import InteractButton from "@/components/InteractButton.vue";
import {ElMessage} from "element-plus";
import {useStore} from "@/store";
import TopicEditor from "@/components/TopicEditor.vue";
import TopicCommentEditor from "@/components/TopicCommentEditor.vue";
import {ChatSquare, Delete} from "@element-plus/icons";

const route = useRoute()
const store = useStore()

const tid = route.params.tid

// 帖子详情数据
const topic = reactive({
    data: null,
    like: false,
    collect: false,
    comments: null,
    page: 1
})


const edit = ref(false)

const comment = reactive({
    show: false,
    text: '',
    quote: null
})

// 获取帖子详情
const init = () => get(`/api/forum/topic-detail?tid=${tid}`, data => {
    topic.data = data
    topic.like = data.interact.like
    topic.collect = data.interact.collect
    loadComments(1)
})

init()

//帖子内容富文本
function convertToHtml(content) {
    const ops = JSON.parse(content).ops
    const converter = new QuillDeltaToHtmlConverter(ops, {inlineStyles: true});
    return converter.convert();
}


// 点赞和收藏
function interact(type, message) {
    get(`/api/forum/interact?tid=${tid}&type=${type}&state=${!topic[type]}`, () => {
        topic[type] = !topic[type]
        if (topic[type])
            ElMessage.success(`${message}成功！`)
        else
            ElMessage.success(`已取消${message}！`)
    })
}

//更新帖子内容
function updateTopic(editor) {
    post('/api/forum/update-topic', {
        id: tid,
        type: editor.type.id,
        title: editor.title,
        content: editor.text
    }, () => {
        ElMessage.success('更新帖子的内容成功')
        edit.value = false;
        init()
    })
}

// 加载评论
function loadComments(page) {
    topic.comments = null
    topic.page = page
    get(`api/forum/comments?tid=${tid}&page=${page - 1}`, data => topic.comments = data)
}

// 添加评论
function onCommentAdd() {
    comment.show = false
    loadComments(Math.floor(++topic.data.comments / 10) + 1)
}

// 删除评论
function deleteComment(id) {
    get(`/api/forum/delete-comment?id=${id}`, () => {
        ElMessage.success('删除评论成功')
        loadComments(topic.page)
    })
}
</script>

<template>
    <div class="topic-page" v-if="topic.data">
        <div class="topic-header">
            <card style="display: flex;width: 100%">
                <el-button :icon="ArrowLeft" type="info" size="small"
                           @click="router.push('/index')">返回列表
                </el-button>
                <div style="text-align: center;flex: 1">
                    <topic-tag :type="topic.data.type"/>
                    <span style="font-weight: bold;margin-left: 10px">{{topic.data.title}}</span>
                </div>
            </card>
        </div>
        <div class="topic-main" v-if="topic.data">
            <div class="topic-main-left">
                <el-avatar :size="50" :src="store.avatarUserUrl(topic.data.user.avatar)"/>
                <div>
                    <div style="font-size: 18px;font-weight: bold">
                        {{topic.data.user.username}}
                        <span style="color:hotpink" v-if="topic.data.user.gender === 1">
              <el-icon><Female/></el-icon>
            </span>
                        <span style="color:blue" v-if="topic.data.user.gender === 0">
              <el-icon><Male/></el-icon>
            </span>
                    </div>
                    <div class="desc">
                        {{topic.data.user.email}}
                    </div>
                    <el-divider style="margin: 10px 0"/>
                    <div style="text-align: center">
                        <div class="desc" v-if="topic.data.user.wx">微信:{{topic.data.user.wx}}</div>
                        <div class="desc" v-if="topic.data.user.qq">QQ:{{topic.data.user.qq}}</div>
                        <div class="desc" v-if="topic.data.user.phone">手机:{{topic.data.user.phone}}</div>
                    </div>
                    <el-divider style="margin: 10px 0"/>
                    <div>
                        <div class="desc">{{topic.data.user.desc}}</div>
                    </div>
                </div>
            </div>
            <div class="topic-main-right">
                <div class="topic-content" v-html="convertToHtml(topic.data.content)"></div>
                <el-divider style="margin: 10px 0"/>
                <div style="font-size: 13px;color: grey;text-align: center">
                    <div>发帖时间: {{new Date(topic.data.time).toLocaleString()}}</div>
                </div>

                <div style="text-align: right;margin-top:30px">
                    <div style="text-align: right;margin-top: 30px">
                        <interact-button name="编辑帖子" color="dodgerblue" :check="false"
                                         @check="edit = true" style="margin-right: 20px"
                                         v-if="store.user.id === topic.data.user.id"
                        >
                            <el-icon>
                                <EditPen/>
                            </el-icon>
                        </interact-button>
                        <interact-button name="点个赞吧" check-name="已点赞" color="red" :checked="topic.like"
                                         @check="interact('like', '点赞')">
                            <el-icon>
                                <CircleCheck/>
                            </el-icon>
                        </interact-button>
                        <interact-button name="收藏本帖" check-name="已收藏" color="orange" :checked="topic.collect"
                                         @check="interact('collect', '收藏')"
                                         style="margin-left: 20px">
                            <el-icon>
                                <Star/>
                            </el-icon>
                        </interact-button>
                    </div>
                </div>
            </div>
        </div>
        <transition mode="out-in" name="el-fade-linear">
            <div v-if="topic.comments">
                <div v-for="item in topic.comments" class="topic-main" style="margin-top: 10px">
                    <div class="topic-main-left">
                        <el-avatar :size="50" :src="store.avatarUserUrl(item.user.avatar) "/>
                        <div>
                            <div style="font-size: 18px;font-weight: bold">
                                {{item.user.username}}
                                <span v-if="item.user.gender === 1" style="color:hotpink">
              <el-icon><Female/></el-icon>
            </span>
                                <span v-if="item.user.gender === 0" style="color:blue">
              <el-icon><Male/></el-icon>
            </span>
                            </div>
                            <div class="desc">
                                {{item.user.email}}
                            </div>
                            <el-divider style="margin: 10px 0"/>
                            <div style="text-align: center">
                                <div v-if="item.user.wx" class="desc">微信:{{item.user.wx}}</div>
                                <div v-if="item.user.qq" class="desc">QQ:{{item.user.qq}}</div>
                                <div v-if="item.user.phone" class="desc">手机:{{item.user.phone}}</div>
                            </div>
                        </div>
                    </div>
                    <div class="topic-main-right">
                        <div style="font-size: 13px;color: grey;text-align: left">
                            <div>评论时间: {{new Date(item.time).toLocaleString()}}</div>
                        </div>
                        <div v-if="item.quote" class="comment-quote">
                            回复{{item.quote}}
                        </div>
                        <div class="topic-content" v-html="convertToHtml(item.content)"></div>
                        <div style="text-align: right">
                            <el-link :icon="ChatSquare" type="info" @click="comment.show=true;comment.quote=item">
                                &nbsp回复评论
                            </el-link>
                            <el-link v-if="item.user.id === store.user.id" :icon="Delete" style="margin-left: 20px"
                                     type="danger" @click="deleteComment(item.id)">
                                &nbsp删除评论
                            </el-link>
                        </div>
                    </div>
                </div>
                <div style="width: fit-content;margin: 20px auto">
                    <el-pagination v-model:current-page="topic.page" :page-size="10" :total="topic.data.comments"
                                   background
                                   hide-on-single-page layout="prev, pager, next" @current-change="loadComments">

                    </el-pagination>
                </div>
            </div>
        </transition>
        <topic-editor :show="edit" @close="edit = false" v-if="topic.data && store.forum.types"
                      :default-type="topic.data.type" :default-text="topic.data.content"
                      :default-title="topic.data.title" submit-button="更新帖子内容" :submit="updateTopic"/>
        <topic-comment-editor :quote="comment.quote" :show="comment.show"
                              :tid="tid"
                              @close="comment.show = false" @comment="onCommentAdd"/>
        <div class="topic-comments" @click="comment.show = true;comment.quote = null">
            <el-icon>
                <Plus/>
            </el-icon>
        </div>
    </div>
</template>

<style scoped lang="less">
.comment-quote {
    font-size: 13px;
    color: grey;
    background-color: rgba(94, 94, 94, 0.1);
    padding: 10px;
    margin-top: 10px;
    border-radius: 5px;
}

.topic-comments {
    position: fixed;
    bottom: 20px;
    right: 20px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    font-size: 18px;
    font-weight: bold;
    color: var(--el-color-primary);
    text-align: center;
    line-height: 45px;
    background: var(--el-bg-color-overlay);
    box-shadow: var(--el-box-shadow-lighter);

    &:hover {
        background: var(--el-border-color-extra-light);
        cursor: pointer;
        opacity: 0.6;
    }
}

.topic-header {
    position: sticky;
    top: 0;
    z-index: 12;
    display: flex;
    border-radius: 5px;
    margin: 0 auto;
    background-color: var(--el-bg-color);
    width: 80%;
}

.topic-page {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 10px 0;
}

.topic-main {
    display: flex;
    border-radius: 5px;
    margin: 0 auto;
    background-color: var(--el-bg-color);
    width: 80%;

    .topic-main-left {
        width: 200px;
        padding: 10px;
        text-align: center;
        border-right: 1px solid var(--el-border-color);

        .desc {
            font-size: 10px;
            color: var(--el-text-color-secondary);
        }
    }

    .topic-main-right {
        width: 100%;
        padding: 10px 20px;
        display: flex;
        flex-direction: column;

        .topic-content {
            font-size: 14px;
            line-height: 25px;
            opacity: 0.8;
        }
    }
}
</style>