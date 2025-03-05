<script setup>
import {Document} from "@element-plus/icons-vue";
import {computed, reactive, ref} from "vue";
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import {Check} from "@element-plus/icons";
import axios from "axios";
import {accessHeader, post} from "@/net";
import {ElMessage} from "element-plus";
import {Delta, Quill, QuillEditor} from "@vueup/vue-quill";
import ImageResize from "quill-image-resize-vue";
import {ImageExtend, QuillWatch} from "quill-image-super-solution-module";
import ColorDot from "@/components/ColorDot.vue";
import {useStore} from "@/store";

const props = defineProps({
    show: Boolean,
    defaultTitle: {
        default: '',
        type: String
    },
    defaultText: {
        default: '',
        type: String
    },
    defaultType: {
        default: null,
        type: Number
    },
    defaultButton: {
        default: '立即发表主题',
        type: String
    },
    submitButton: {
        default: '立即发表主题',
        type: String
    },
    submit: {
        default: (editor, success) => {
            post('/api/forum/create-topics', {
                type: editor.type.id,
                title: editor.title,
                content: editor.text,
            }, () => {
                ElMessage.success('帖子发布成功!')
                success()
            })
        },
        type: Function,
    }

})

const store = useStore()

const emit = defineEmits(["close", "success"]);
// 富文本编辑器引用
const refEditor = ref()
// 富文本编辑器配置项
const editor = reactive({
    type: null,
    title: '',
    text: '',
    loading: false,
})

// 初始化富文本编辑器内容
function initEditor() {
    if (props.defaultText) {
        editor.text = new Delta(JSON.parse(props.defaultText))
    } else {
        refEditor.value.setContents('', 'user')
    }
    editor.title = props.defaultTitle
    editor.type = findTypeById(props.defaultType)
}

// 根据id查找类型
function findTypeById(id) {
    for (let type of store.forum.types) {
        if (type.id === id) {
            return type
        }
    }
}

//计算富文本长度
const contentLength = computed(() => deltaToText(editor.text).length)

//提取富文本内容为纯文本
function deltaToText(delta) {
    if (!delta.ops) return ""
    let str = ""
    for (let op of delta.ops)
        str += op.insert
    return str.replace(/\s/g, "")
}


// 提交帖子
function submitTopic() {
    const text = deltaToText(editor.text)
    if (text.length > 20000) {
        ElMessage.warning('帖子内容过长，请精简后再提交!')
        return
    }
    if (!editor.title) {
        ElMessage.warning('请输入帖子标题!')
        return
    }
    if (!editor.type) {
        ElMessage.warning('请选择帖子类型!')
        return
    }
    props.submit(editor, () => emit('success'))
}

Quill.register('modules/imageResize', ImageResize)
Quill.register('modules/ImageExtend', ImageExtend)

// 富文本编辑器配置项
const editorOption = {
    modules: {
        toolbar: {
            container: [
                "bold", "italic", "underline", "strike", "clean",
                {color: []}, {'background': []},
                {size: ["small", false, "large", "huge"]},
                {header: [1, 2, 3, 4, 5, 6, false]},
                {list: "ordered"}, {list: "bullet"}, {align: []},
                "blockquote", "code-block", "link", "image",
                {indent: '-1'}, {indent: '+1'}
            ],
            handlers: {
                'image': function () {
                    QuillWatch.emit(this.quill.id)
                }
            }
        },
        imageResize: {
            modules: ['Resize', 'DisplaySize']
        },
        ImageExtend: {
            action: axios.defaults.baseURL + '/api/image/cache',
            name: 'file',
            size: 5,
            loading: true,
            accept: 'image/png, image/jpeg',
            response: (resp) => {
                if (resp.data) {
                    return axios.defaults.baseURL + '/images' + resp.data
                } else {
                    return null
                }
            },
            methods: 'POST',
            headers: xhr => {
                xhr.setRequestHeader('Authorization', accessHeader().Authorization);
            },
            start: () => editor.uploading = true,
            success: () => {
                ElMessage.success('图片上传成功!')
                editor.uploading = false
            },
            error: () => {
                ElMessage.warning('图片上传失败，请联系管理员!')
                editor.uploading = false
            }
        }
    }
}
</script>

<template>
    <div>
        <el-drawer :model-value="show"
                   direction="btt"
                   @open="initEditor"
                   :size="650"
                   :close-on-click-modal="false"
                   @close="emit('close')">
            <template #header>
                <div>
                    <div style="font-weight: bold">发表新的帖子</div>
                    <div style="font-size: 13px">发表内容之前，请遵守相关法律法规，不要出现骂人等爆粗口的不文明行为。</div>
                </div>
            </template>
            <div style="display: flex;gap: 10px">
                <div style="width: 150px">
                    <el-select placeholder="选择主题类型..." v-model="editor.type"
                               :disabled="!store.forum.types.length" value-key="id">
                        <el-option v-for="item in store.forum.types" :value="item" :label="item.name">
                            <div>
                                <color-dot :color="item.color"/>
                                <span style="margin-left: 5px">{{item.name}}</span>
                            </div>
                        </el-option>
                    </el-select>

                </div>
                <div style="flex: 1">
                    <el-input v-model="editor.title" placeholder="请输入帖子标题" :prefix-icon="Document" maxlength="30"
                              style="height: 100%"></el-input>
                </div>

            </div>
            <div style="margin-top: 5px;font-size: 13px;color: gray">
                <color-dot :color="editor.type ? editor.type.color : '#dadada'"/>
                <span style="margin-left: 5px">{{editor.type ? editor.type.desc : '请在上方选择一个帖子的类型'}}</span>
            </div>
            <div style="margin-top: 15px;height: 440px;overflow: hidden;border-radius: 5px"
                 v-loading="editor.uploading"
                 element-loading-text="这种上传图片，请稍后...">
                <quill-editor v-model:content="editor.text" style="height: calc(100% - 45px)"
                              content-type="delta"
                              ref="refEditor"
                              placeholder="今天想分享点什么呢？" :options="editorOption"/>
            </div>
            <div style="display: flex;justify-content: space-between; margin-top: 10px">
                <div style="color: gray;font-size: 13px">
                    当前字数{{contentLength}} (最大支持20000字)
                </div>
                <div>
                    <el-button type="primary" :icon="Check" @click="submitTopic" plain>{{submitButton}}</el-button>
                </div>
            </div>
        </el-drawer>

    </div>
</template>

<style scoped>

:deep(.el-drawer) {
    width: 800px;
    margin: auto;
    border-radius: 10px;
}

:deep(.el-drawer__header) {
    margin: 0;
}


</style>