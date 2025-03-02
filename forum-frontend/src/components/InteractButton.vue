<script setup>
defineProps({
    name: String,
    checkName: String,
    color: String,
    checked: Boolean
})

const emit = defineEmits(['check'])
</script>

<template>
    <div class="interact-button">
         <span class="icon" :style="{'color': checked ? color : 'unset'}" @click="emit('check')">
            <slot/>
        </span>
        <span class="name" :style="{ 'color': color }">
            {{checked ? checkName : name}}
        </span>
    </div>

</template>

<style scoped lang="less">
.interact-button {
    display: inline-block;
    height: 22px;

    .icon {
        vertical-align: middle;
        transition: .3s;

        &:hover {
            cursor: pointer;
            font-size: 17px;
            transform: scale(1.2);
        }
    }

    .name {
        font-size: 13px;
        margin-left: 5px;
        opacity: 0.7;
    }
}
</style>