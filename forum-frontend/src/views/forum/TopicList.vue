<script setup>

import LightCard from "@/components/LightCard.vue";
import {
    Calendar,
    Clock,
    CollectionTag,
    Compass,
    Document,
    Edit,
    EditPen,
    Link,
    Microphone,
    Picture
} from "@element-plus/icons-vue";
import WeatherInfo from "@/components/WeatherInfo.vue";
import {computed, reactive, ref, watch} from "vue";
import {get} from "@/net";
import TopicEditor from "@/components/TopicEditor.vue";
import {useStore} from "@/store";
import ColorDot from "@/components/ColorDot.vue";
import router from "@/router";
import TopicTag from "@/components/TopicTag.vue";
import TopicCollectList from "@/components/TopicCollectList.vue";

//发帖编辑器
const editor = ref(false)

const store = useStore()

// 帖子列表数据
const topics = reactive({
    list: [],
    type: 0,
    page: 0,
    end: false,
    top: []
})

const collects = ref(false)

// 监听板块类型变化，重新获取帖子列表数据

watch(() => topics.type, () => resetList(), {immediate: true})

get('/api/forum/top-topic', data => topics.top = data)


// 获取帖子列表数据
function updateList() {
    if (topics.end) return
    get(`/api/forum/list-topic?page=${topics.page}&type=${topics.type}`, data => {
        if (data) {
            data.forEach(d => topics.list.push(d))
            topics.page++
        }
        if (!data || data.length < 10)
            topics.end = true
    })
}

// 帖子创建成功，重置列表数据
function onTopicsCreate() {
    editor.value = false
    resetList()
}

// 重置帖子列表
function resetList() {
    topics.page = 0
    topics.end = false
    topics.list = []
    updateList()
}

// 今日日期
const today = computed(() => {
    const date = new Date()
    return `${date.getFullYear()} 年 ${date.getMonth() + 1} 月 ${date.getDate()} 日`
})

// 天气信息列表
const weather = reactive({
    location: {},
    now: {},
    hourly: [],
    success: false
})

// 获取天气信息
navigator.geolocation.getCurrentPosition((position) => {
            const longitude = position.coords.longitude;
            const latitude = position.coords.latitude;
            get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`, data => {
                Object.assign(weather, data)
                weather.success = true
            })
        }, error => {
            console.warn(error + '位置信息获取超时,请检查网络设置')
            get(`/api/forum/weather?longitude=116.40529&latitude=39.90499`, data => {
                Object.assign(weather, data)
                weather.success = true
            })
        }, {
            timeout: 3000,
            enableHighAccuracy: true,
        }
)


</script>

<template>
    <div class="topicList-main">
        <div style="flex:1">
            <light-card>
                <div class="create-topic" @click="editor=true">
                    <el-icon>
                        <EditPen/>
                    </el-icon>
                    点击发表主题...
                    <div>
                        <el-icon>
                            <Edit/>
                        </el-icon>
                        <el-icon>
                            <Document/>
                        </el-icon>
                        <el-icon>
                            <Compass/>
                        </el-icon>
                        <el-icon>
                            <Picture/>
                        </el-icon>
                        <el-icon>
                            <Microphone/>
                        </el-icon>
                    </div>
                </div>

            </light-card>
            <el-divider/>
            <light-card style="margin-top: 10px;display:flex;flex-direction:column;gap:10px ">
                <div v-for="item in topics.top" class="top-topic"
                     @click="router.push(`/index/topic-detail/${item.id}`)">
                    <el-tag type="info" size="small">置顶</el-tag>
                    <div>{{item.title}}</div>
                    <div>{{new Date(item.time).toLocaleString()}}</div>
                </div>
            </light-card>
            <light-card style="margin-top: 10px;display: flex;gap: 7px">
                <div :class="`type-select-card ${topics.type === item.id ? 'active' : ''}`"
                     v-for="item in store.forum.types"
                     @click="topics.type = item.id">
                    <color-dot :color="item.color"/>
                    {{item.name}}
                </div>
            </light-card>
            <transition name="el-fade-in" mode="out-in">
                <div v-if="topics.list.length">
                    <div style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px"
                         v-infinite-scroll="updateList">
                        <light-card v-for="item in topics.list" class="topic-card"
                                    @click="router.push('/index/topic-detail/'+item.id)">
                            <div style="display: flex">
                                <div>
                                    <el-avatar :size="30" :src="store.avatarUserUrl(item.avatar)"/>
                                </div>
                                <div style="margin-left: 7px;transform: translate(-2px)">
                                    <div style="font-size: 13px;font-weight: bold">{{item.username}}</div>
                                    <div style="font-size: 12px;color: gray">
                                        <el-icon>
                                            <Clock/>
                                        </el-icon>
                                        <div style="margin-left: 2px;display: inline-block;transform: translateY(-2px)">
                                            {{new Date(item.time).toLocaleString()}}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div style="margin-top: 7px">
                                <topic-tag :type="item.type"/>
                                <span style="font-weight: bold;margin-left: 7px">{{item.title}}</span>
                            </div>
                            <div class="topic-content">{{item.text}}</div>
                            <div style="display: grid;grid-template-columns: repeat(3, 1fr);grid-gap: 10px">
                                <el-image class="topic-image" v-for="img in item.images" :src="img"
                                          fit="cover"></el-image>
                            </div>
                            <div style="display: flex;gap: 20px;font-size: 13px;margin-top: 10px;opacity: 0.8">
                                <div>
                                    <el-icon style="vertical-align: middle">
                                        <CircleCheck/>
                                    </el-icon>
                                    {{item.like}}点赞
                                </div>
                                <div>
                                    <el-icon style="vertical-align: middle">
                                        <Star/>
                                    </el-icon>
                                    {{item.collect}}收藏
                                </div>
                            </div>
                        </light-card>
                    </div>
                </div>
            </transition>

        </div>
        <div style="width: 260px">
            <div style="position: sticky;top: 20px">
                <light-card>
                    <div class="collect-list-button" @click="collects=true">
                        <span><el-icon><FolderOpened/></el-icon> 查看我的收藏</span>
                        <el-icon style="transform: translateY(3px)">
                            <ArrowRightBold/>
                        </el-icon>
                    </div>
                </light-card>
                <light-card style="margin-top: 10px">
                    <div style="font-weight: bold;color: red">
                        <el-icon>
                            <CollectionTag/>
                        </el-icon>
                        论坛公告
                    </div>
                    <el-divider style="margin: 10px 0"/>
                    <div style="font-size: 14px;margin: 10px;color: grey">
                        为认真学习宣传贯彻党的二十大精神,深入贯彻习近平强军思想,
                        作为迎接办学70周年系列学术活动之一,国防科技大学将于2022年11月24日至26日在长沙举办“国防科技
                    </div>
                </light-card>
                <light-card style="margin-top: 10px">
                    <div style="font-weight: bold;">
                        <el-icon>
                            <Calendar/>
                        </el-icon>
                        天气信息
                    </div>
                    <el-divider style="margin: 10px 0"/>
                    <weather-info :data="weather"/>
                </light-card>
                <light-card style="margin-top: 10px">
                    <div class="info-text">
                        <div>当前日期</div>
                        <div>{{today}}</div>
                    </div>
                    <div class="info-text">
                        <div>当期IP地址</div>
                        <div>127.0.0.1</div>
                    </div>
                </light-card>
                <div style="font-size: 14px;margin-top: 10px;color: grey">
                    <el-icon>
                        <Link/>
                    </el-icon>
                    友情链接
                    <el-divider style="margin: 10px 0"/>
                </div>
                <div style="display: grid;grid-template-columns: repeat(2, 1fr);grid-gap: 10px;margin-top: 10px">
                    <div class="friend-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/js-design-banner.jpg"/>
                    </div>
                    <div class="friend-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/vform-banner.png"/>
                    </div>
                    <div class="friend-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/jnpfsoft.png"/>
                    </div>
                    <div class="friend-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/CRMEB-l.png"/>
                    </div>
                </div>
            </div>
        </div>
        <topic-editor :show="editor" @success="onTopicsCreate" @close="editor = false"/>
        <topic-collect-list :show="collects" @close="collects=false"/>
    </div>
</template>

<style lang="less" scoped>
.top-topic {
    display: flex;

    div:first-of-type {
        font-size: 13px;
        margin-left: 10px;
        font-weight: bold;
        opacity: 0.8;
        transition: color .3s;

        &:hover {
            cursor: pointer;
            color: gray;
        }
    }

    div:nth-of-type(2) {
        flex: 1;
        font-size: 12px;
        color: grey;
        text-align: right;

        &:hover {
            cursor: pointer;
        }
    }

}

.type-select-card {
    background-color: #f5f5f5;
    padding: 2px 5px;
    font-size: 12px;
    border-radius: 3px;
    box-sizing: border-box;
    transition: background-color .3s;

    &.active {
        border: solid 1px #ead4c4;
    }

    &:hover {
        cursor: pointer;
        background-color: #dadada;
    }
}

.dark .type-select-card {
    color: #dadada;
    border-color: #ffffff33;
    background: #ffffff33;
}

.topic-card {
    padding: 15px;
    transition: scale .2s;

    &:hover {
        scale: 1.02;
        cursor: pointer;
    }

    .topic-content {
        font-size: 13px;
        color: grey;
        margin: 10px 0;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 3;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .topic-image {
        width: 100%;
        height: 100%;
        max-height: 110px;
        border-radius: 5px;
    }
}

.topicList-main {
    display: flex;
    margin: 20px auto;
    gap: 20px;
    max-width: 800px;
    padding: 0 20px;

    .create-topic {
        background-color: #efefef;
        border-radius: 5px;
        height: 40px;
        color: grey;
        font-size: 14px;
        line-height: 40px;
        padding: 0 10px;

        &:hover {
            cursor: pointer;
        }

        div:first-of-type {
            margin-top: 10px;
            display: flex;
            gap: 13px;
            font-size: 18px;
            color: grey
        }
    }

    .dark .create-topic {
        background-color: #232323;
    }

    .info-text {
        display: flex;
        justify-content: space-between;
        color: grey;
        font-size: 14px;
    }

    .friend-link {
        border-radius: 5px;
        overflow: hidden;
    }
}

.collect-list-button {
    font-size: 13px;
    display: flex;
    justify-content: space-between;
    transition: .3s;

    &:hover {
        cursor: pointer;
        opacity: 0.6;
    }
}
</style>