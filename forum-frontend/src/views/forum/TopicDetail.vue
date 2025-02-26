<script setup>

import {useRoute} from "vue-router";
import {get} from "@/net";
import axios from "axios";
import {computed, reactive} from "vue";
import {Female, ArrowLeft, Male} from "@element-plus/icons-vue";
import {QuillDeltaToHtmlConverter} from 'quill-delta-to-html';
import Card from "@/components/Card.vue";
import router from "@/router";
import TopicTag from "@/components/TopicTag.vue";

const route = useRoute()

const tid = route.params.tid

// 帖子详情数据
const topic = reactive({
  data: null,
  comments: [],
})

// 获取帖子详情
get(`/api/forum/topic-detail?tid=${tid}`, data => {
  topic.data = data;
})

const content = computed(() => {
  const ops = JSON.parse(topic.data.content).ops
  const converter = new QuillDeltaToHtmlConverter(ops, {inlineStyles: true});
  return converter.convert();
})
</script>

<template>
  <div class="topic-page">
    <div class="topic-header">
      <card style="display: flex;width: 100%">
        <el-button :icon="ArrowLeft" type="info" size="small"
                   @click="router.push('/index')">返回列表
        </el-button>
        <div style="text-align: center;flex: 1">
          <topic-tag :type="topic.data.type" />
          <span style="font-weight: bold;margin-left: 10px">{{ topic.data.title }}</span>
        </div>
      </card>
    </div>
    <div class="topic-main" v-if="topic.data">
      <div class="topic-main-left">
        <el-avatar :src="axios.defaults.baseURL + '/images' + topic.data.user.avatar" :size="50"/>
        <div>
          <div style="font-size: 18px;font-weight: bold">
            {{ topic.data.user.username }}
            <span style="color:hotpink" v-if="topic.data.user.gender === 1">
              <el-icon><Female/></el-icon>
            </span>
            <span style="color:blue" v-if="topic.data.user.gender === 0">
              <el-icon><Male/></el-icon>
            </span>
          </div>
          <div class="desc">
            {{ topic.data.user.email }}
          </div>
          <el-divider style="margin: 10px 0"/>
          <div style="text-align: center">
            <div class="desc" v-if="topic.data.user.wx">微信:{{ topic.data.user.wx }}</div>
            <div class="desc" v-if="topic.data.user.qq">QQ:{{ topic.data.user.qq }}</div>
            <div class="desc" v-if="topic.data.user.phone">手机:{{ topic.data.user.phone }}</div>
          </div>
          <el-divider style="margin: 10px 0"/>
          <div>
            <div class="desc">{{ topic.data.user.desc }}</div>
          </div>
        </div>
      </div>
      <div class="topic-main-right">
        <div class="topic-content" v-html="content"></div>
      </div>
    </div>
    <div class="topic-comments">

    </div>
  </div>
</template>

<style scoped lang="less">

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
    width: 600px;
    padding: 10px 20px;

    .topic-content {
      font-size: 14px;
      line-height: 22px;
      opacity: 0.8;
    }
  }
}
</style>