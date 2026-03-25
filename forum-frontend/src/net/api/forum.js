import {get, post} from "@/net";
import {ElMessage} from "element-plus";

// 获取论坛帖子置顶列表
export const apiForumTopicTopList = (success) => {
    get('/api/forum/top-topic', success)
}
// 获取论坛帖子列表
export const apiForumTopicList = (page, type, success) => {
    get(`/api/forum/list-topic?page=${page}&type=${type}`, success)
}

// 获取论坛首页天气信息
export const apiForumWeather = (longitude, latitude, success) => {
    get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`, success)
}

// 创建论坛帖子
export const apiForumTopicCreate = (data, success) => {
    post('/api/forum/create-topics', data, success)
}

// 获取论坛帖子收藏列表
export const apiForumCollects = (success) => {
    get('/api/forum/collects', success)
}

// 删除论坛帖子收藏
export const apiForumCollectDelete = (tid, success) => {
    get(`/api/forum/interact?tid=${tid}&type=collect&state=false`, success)
}

// 获取论坛帖子类型列表
export const apiForumTypeList = (success) => {
    get('/api/forum/types', success)
}
// 获取论坛帖子详情
export const apiForumTopic = (tid, success) => {
    get(`/api/forum/topic-detail?tid=${tid}`, success)
}

// 论坛帖子点赞、收藏等交互
export const apiForumInteract = (tid, type, topic, message) => {
    get(`/api/forum/interact?tid=${tid}&type=${type}&state=${!topic[type]}`, () => {
        topic[type] = !topic[type]
        if (topic[type])
            ElMessage.success(`${message}成功！`)
        else
            ElMessage.success(`已取消${message}！`)
    })
}

// 更新帖子内容
export const apiForumTopicUpdate = (data, success) => {
    post('/api/forum/update-topic', data, success)
}

// 获取帖子评论
export const apiForumComments = (tid, page, success) => {
    get(`api/forum/comments?tid=${tid}&page=${page}`, success)
}

// 删除帖子评论
export const apiForumCommentDelete = (id, success) => {
    get(`/api/forum/delete-comment?id=${id}`, success)
}

// 添加帖子评论
export const apiForumCommentAdd = (data, success) => {
    post('/api/forum/add-comment', data, success)
}

//获取全部帖子管理员
export const apiForumTopicAllList = (page,size,success) => {
    get(`/api/admin/forum/list?page=${page}&size=${size}`, success)
}