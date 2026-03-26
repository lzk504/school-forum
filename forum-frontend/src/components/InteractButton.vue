<script setup>
const props = defineProps({
    name: String,
    checkName: String,
    color: String,
    checked: Boolean,
    disabled: Boolean
})

const onClick = ()=>{
    if(!props.disabled){
        emit('check')
    }
}

const emit = defineEmits(['check'])
</script>

<template>
    <div class="interact-button" :class="{'locked':disabled}">
         <span class="icon" :style="{'color': checked ? color : 'unset'}" @click="onClick">
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

    &.locked {
        opacity: 0.5;

        .icon:hover{
            font-weight: unset;
            cursor: not-allowed;
        }
    }
}
</style>