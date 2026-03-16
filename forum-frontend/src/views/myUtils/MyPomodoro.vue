<script setup>
import {ElMessage, ElMessageBox} from 'element-plus';
import Card from "@/components/Card.vue";
import {Check, CircleClose, Clock, List, RefreshLeft, Timer} from "@element-plus/icons";
import {computed, reactive} from "vue";

// 计时器状态
const timerState = reactive({
    minutes: 25,
    seconds: 0,
    isRunning: false,
    timerId: null,
    selectedDuration: 25,
    totalTime: 25 * 60, // 总秒数，用于进度计算
    elapsedSeconds: 0   // 已用秒数
});

// 任务列表
const tasks = reactive({
    list: [],
    current: ''
});

// 计算属性
const progress = computed(() => {
    if (timerState.totalTime === 0) return 0;
    return (timerState.elapsedSeconds / timerState.totalTime) * 100;
});

const displayTime = computed(() => {
    return `${timerState.minutes.toString().padStart(2, '0')}:${timerState.seconds.toString().padStart(2, '0')}`;
});

const isCompleted = computed(() => {
    return timerState.minutes === 0 && timerState.seconds === 0 && timerState.elapsedSeconds > 0;
});

// 开始计时
function startTimer() {
    if (!timerState.isRunning) {
        timerState.isRunning = true;
        timerState.timerId = setInterval(() => {
            timerState.elapsedSeconds++;
            if (timerState.seconds === 0) {
                if (timerState.minutes === 0) {
                    completeTimer();
                    return;
                }
                timerState.minutes--;
                timerState.seconds = 59;
            } else {
                timerState.seconds--;
            }
        }, 1000);
    }
}

// 完成计时器
function completeTimer() {
    stopTimer();
    ElMessageBox.alert('番茄钟时间结束！恭喜你完成了一个专注时段！', '时间到', {
        confirmButtonText: '确定',
        type: 'success',
        callback: () => {
            // 自动标记当前任务为完成
            markCurrentTaskCompleted();
        }
    });
}

// 标记当前任务为完成
function markCurrentTaskCompleted() {
    if (tasks.list.length > 0 && !tasks.list[tasks.list.length - 1].completed) {
        tasks.list[tasks.list.length - 1].completed = true;
        tasks.list[tasks.list.length - 1].completedTime = new Date().toLocaleString();
        ElMessage.success('任务已自动标记为完成！');
    }
}

// 停止计时
function stopTimer() {
    timerState.isRunning = false;
    if (timerState.timerId) {
        clearInterval(timerState.timerId);
        timerState.timerId = null;
    }
}

// 暂停/继续
function toggleTimer() {
    if (timerState.isRunning) {
        stopTimer();
    } else {
        startTimer();
    }
}

// 更新选择的时长
function updateDuration(value) {
    timerState.selectedDuration = value;
    timerState.totalTime = value * 60;
    if (!timerState.isRunning) {
        timerState.minutes = value;
        timerState.seconds = 0;
        timerState.elapsedSeconds = 0;
    }
}

// 重置计时器
function resetTimer() {
    stopTimer();
    timerState.minutes = timerState.selectedDuration;
    timerState.seconds = 0;
    timerState.totalTime = timerState.selectedDuration * 60;
    timerState.elapsedSeconds = 0;
}

// 添加任务
function addTask() {
    if (tasks.current.trim()) {
        tasks.list.push({
            name: tasks.current,
            completed: false,
            id: Date.now(),
            duration: `${timerState.selectedDuration}分钟`,
            createdAt: new Date().toLocaleString()
        });
        tasks.current = '';
        ElMessage.success('任务添加成功！');
    }
}

// 切换任务状态
function toggleTask(index) {
    tasks.list[index].completed = !tasks.list[index].completed;
    if (tasks.list[index].completed) {
        tasks.list[index].completedTime = new Date().toLocaleString();
        ElMessage.success('任务已完成！');
    } else {
        delete tasks.list[index].completedTime;
        ElMessage.info('任务标记为未完成');
    }
}

// 删除任务
function deleteTask(index) {
    ElMessageBox.confirm('确定要删除这个任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        tasks.list.splice(index, 1);
        ElMessage.success('任务已删除');
    }).catch(() => {
        // 取消删除
    });
}

// 清空已完成任务
function clearCompletedTasks() {
    const completedCount = tasks.list.filter(task => task.completed).length;
    if (completedCount > 0) {
        ElMessageBox.confirm(`确定要清空 ${completedCount} 个已完成的任务吗？`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            tasks.list = tasks.list.filter(task => !task.completed);
            ElMessage.success(`已清空 ${completedCount} 个完成任务`);
        }).catch(() => {
            // 取消清空
        });
    } else {
        ElMessage.info('没有已完成的任务需要清空');
    }
}

// 统计信息
const completedTasks = computed(() => tasks.list.filter(task => task.completed).length);
const totalTasks = computed(() => tasks.list.length);
const completionRate = computed(() => totalTasks.value > 0 ? Math.round((completedTasks.value / totalTasks.value) * 100) : 0);
</script>

<template>
    <div class="pomodoro-container">
        <div class="timer-section">
            <Card :icon="Timer" desc="专注时间 1-60 分钟" title="番茄钟">
                <!-- 进度环 -->
                <div class="progress-container">
                    <el-progress
                        :color="isCompleted ? '#67C23A' : (timerState.isRunning ? '#409EFF' : '#909399')"
                        :percentage="Math.round(progress)"
                        :show-text="false"
                        :stroke-width="15"
                        :width="200"
                        type="dashboard"
                    />
                    <div class="time-display">
                        <div class="time">{{ displayTime }}</div>
                        <div class="status">{{ timerState.isRunning ? '专注中' : (isCompleted ? '已完成' : '准备开始') }}</div>
                    </div>
                </div>

                <!-- 时长选择 -->
                <div class="duration-slider">
                    <span>{{ timerState.selectedDuration }}分钟</span>
                    <el-slider
                        v-model="timerState.selectedDuration"
                        :disabled="timerState.isRunning"
                        :max="60"
                        :min="1"
                        :step="1"
                        show-stops
                        @change="updateDuration"
                    />
                </div>

                <!-- 控制按钮 -->
                <div class="timer-controls">
                    <el-button
                        :disabled="isCompleted"
                        type="primary"
                        @click="toggleTimer"
                    >
                        {{ timerState.isRunning ? '暂停' : '开始' }}
                    </el-button>
                    <el-button
                        :disabled="!timerState.isRunning"
                        :icon="CircleClose"
                        type="warning"
                        @click="stopTimer"
                    >
                        停止
                    </el-button>
                    <el-button
                        :icon="RefreshLeft"
                        type="info"
                        @click="resetTimer"
                    >
                        重置
                    </el-button>
                </div>

                <!-- 统计信息 -->
                <div v-if="totalTasks > 0" class="statistics">
                    <div class="stat-item">
                        <span class="stat-label">总任务：</span>
                        <span class="stat-value">{{ totalTasks }}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">已完成：</span>
                        <span class="stat-value">{{ completedTasks }}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">完成率：</span>
                        <span class="stat-value">{{ completionRate }}%</span>
                    </div>
                </div>
            </Card>

            <!-- 任务列表 -->
            <Card :icon="List" desc="添加需要完成的任务" style="margin-top: 20px" title="任务列表">
                <div class="task-input">
                    <el-input
                        v-model="tasks.current"
                        placeholder="输入任务名称，按回车添加"
                        @keyup.enter="addTask"
                    >
                        <template #append>
                            <el-button @click="addTask">添加</el-button>
                        </template>
                    </el-input>
                </div>

                <!-- 任务操作栏 -->
                <div v-if="tasks.list.length > 0" class="task-actions">
                    <el-button
                        :disabled="completedTasks === 0"
                        size="small"
                        type="success"
                        @click="clearCompletedTasks"
                    >
                        清空已完成 ({{ completedTasks }})
                    </el-button>
                </div>

                <!-- 任务列表 -->
                <div class="task-list">
                    <el-empty v-if="tasks.list.length === 0" description="暂无任务"/>

                    <div v-for="(task, index) in tasks.list" :key="task.id"
                         :class="{ 'completed': task.completed }"
                         class="task-item">

                        <!-- 任务复选框 -->
                        <div class="task-checkbox">
                            <el-checkbox
                                :checked="task.completed"
                                @change="toggleTask(index)"
                            />
                        </div>

                        <!-- 任务信息 -->
                        <div class="task-info">
                            <div :class="{ 'completed-task': task.completed }" class="task-name">
                                {{ task.name }}
                            </div>
                            <div class="task-meta">
                                <span class="task-time">
                                    <el-icon><Timer /></el-icon>
                                    {{ task.duration }}
                                </span>
                                <span class="task-create-time">
                                    <el-icon><Clock /></el-icon>
                                    {{ task.createdAt }}
                                </span>
                                <span v-if="task.completed" class="task-complete-time">
                                    <el-icon><Check /></el-icon>
                                    {{ task.completedTime }}
                                </span>
                            </div>
                        </div>

                        <!-- 删除按钮 -->
                        <div class="task-actions">
                            <el-button
                                :icon="CircleClose"
                                size="small"
                                type="danger"
                                @click="deleteTask(index)"
                            >
                                删除
                            </el-button>
                        </div>
                    </div>
                </div>
            </Card>
        </div>
    </div>
</template>

<style lang="less" scoped>
.pomodoro-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;

    .timer-section {
        .progress-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-bottom: 30px;

            .time-display {
                margin-top: 10px;
                text-align: center;

                .time {
                    font-size: 48px;
                    font-weight: bold;
                    font-family: monospace;
                    color: var(--el-text-color-primary);
                    line-height: 1.2;
                }

                .status {
                    font-size: 16px;
                    color: var(--el-text-color-secondary);
                    margin-top: 5px;
                }
            }
        }

        .duration-slider {
            margin-bottom: 30px;

            :deep(.el-slider) {
                margin-top: 10px;
            }
        }

        .timer-controls {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-bottom: 20px;

            :deep(.el-button) {
                min-width: 100px;
            }
        }

        .statistics {
            display: flex;
            justify-content: space-around;
            margin-top: 20px;
            padding: 15px;
            background-color: var(--el-fill-color-light);
            border-radius: 8px;

            .stat-item {
                text-align: center;

                .stat-label {
                    font-size: 14px;
                    color: var(--el-text-color-secondary);
                }

                .stat-value {
                    font-size: 20px;
                    font-weight: bold;
                    color: var(--el-color-primary);
                }
            }
        }

        .task-input {
            margin-bottom: 15px;

            :deep(.el-input-group__append) {
                padding: 0 15px;
            }
        }

        .task-actions {
            margin-bottom: 15px;
            display: flex;
            justify-content: flex-end;
        }

        .task-list {
            .task-item {
                display: flex;
                align-items: flex-start;
                padding: 15px;
                margin-bottom: 10px;
                background-color: var(--el-fill-color-blank);
                border-radius: 8px;
                border: 1px solid var(--el-border-color-light);
                transition: all 0.3s ease;

                &:hover {
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                }

                &.completed {
                    background-color: var(--el-fill-color-lighter);
                    opacity: 0.8;
                }

                .task-checkbox {
                    margin-right: 12px;
                    margin-top: 2px;
                }

                .task-info {
                    flex: 1;

                    .task-name {
                        font-size: 16px;
                        font-weight: 500;
                        color: var(--el-text-color-primary);
                        margin-bottom: 8px;
                        line-height: 1.4;

                        &.completed-task {
                            text-decoration: line-through;
                            color: var(--el-text-color-placeholder);
                        }
                    }

                    .task-meta {
                        display: flex;
                        flex-wrap: wrap;
                        gap: 10px;
                        font-size: 12px;
                        color: var(--el-text-color-secondary);

                        span {
                            display: flex;
                            align-items: center;
                            gap: 4px;
                        }

                        .task-complete-time {
                                    color: var(--el-color-success);
                                }
                    }
                }

                .task-actions {
                    :deep(.el-button) {
                        opacity: 0;
                        transition: opacity 0.3s;

                        &:hover {
                            opacity: 1;
                        }
                    }
                }

                &:hover .task-actions :deep(.el-button) {
                    opacity: 0.8;
                }
            }
        }
    }
}

// 响应式设计
@media (max-width: 640px) {
    .pomodoro-container {
        padding: 10px;

        .timer-section {
            .timer-controls {
                flex-direction: column;
                gap: 10px;

                :deep(.el-button) {
                    width: 100%;
                }
            }

            .statistics {
                flex-direction: column;
                gap: 10px;

                .stat-item {
                    padding: 10px;
                    border: 1px solid var(--el-border-color-light);
                    border-radius: 6px;
                }
            }

            .task-list .task-item {
                flex-direction: column;
                gap: 10px;

                .task-actions {
                    width: 100%;
                    justify-content: center;
                }
            }
        }
    }
}
</style>