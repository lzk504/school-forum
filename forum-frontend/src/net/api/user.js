import {get} from "@/net";
import {useStore} from "@/store";

// 获取用户信息
export const getUserInfo = (loadingRef) => {
    loadingRef.value = true
    const store = useStore();
    get('/api/user/info', (data) => {
        store.user = data
        loadingRef.value = false
    })
}
