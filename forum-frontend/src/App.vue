<script setup>
import {useDark, useToggle} from '@vueuse/core'
import {onMounted, provide, ref} from "vue";
import {isUnauthorized} from "@/net";
import {getUserInfo} from "@/net/api/user";

useDark({
    selector: 'html',
    attribute: 'class',
    valueDark: 'dark',
    valueLight: 'light'
})

useDark({
    onChanged(dark) {
        useToggle(dark)
    }
})

const loading = ref(false);

provide('userLoading', loading)

onMounted(() => {
    if (!isUnauthorized()) {
        getUserInfo(loading);
    }
})

</script>

<template>
    <header>
        <div class="wrapper">
            <router-view/>
        </div>
    </header>
</template>

<style scoped>
header {
    line-height: 1.5;
}
</style>
