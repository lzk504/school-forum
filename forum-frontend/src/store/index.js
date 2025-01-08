import {defineStore} from "pinia";

//用户信息存储
export const useStore = defineStore("general", {
    state: () => {
        return {
            user: {
                username: "",
                email: "",
                role: "",
                registerTime: null
            }
        }
    }
})