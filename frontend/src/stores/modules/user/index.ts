import {defineStore} from "pinia";
import {ref} from "vue";
import {authApi} from "/@/api/methods/auth.ts";

export interface UserInfo {
    id: number;
    username: string;
    permissions: string[];
}

const useUserStore = defineStore('user', () => {
    const accessToken = ref<string>(localStorage.getItem('access_token') || '');
    const refreshToken = ref<string>(localStorage.getItem('refresh_token') || '');
    const userInfo = ref<UserInfo | null>(null);
    const setTokens = (newAccess: string, newRefresh: string) => {
        accessToken.value = newAccess;
        refreshToken.value = newRefresh;
        localStorage.setItem('access_token', newAccess);
        localStorage.setItem('refresh_token', newRefresh);
    };
    const clearToken = () => {
        accessToken.value = '';
        refreshToken.value = '';
        localStorage.setItem('access_token', '');
        localStorage.setItem('refresh_token', '');
    }
    const login = async (username: string, password: string) => {
        const {access_token, refresh_token} = await authApi.login({username, password});
        setTokens(access_token, refresh_token);
    }
    const logout = async () => {
        try {
            await authApi.logout();
        } catch (e) {
            console.error('Logout api error', e);
        } finally {
            // 无论接口是否成功，本地都清除
            accessToken.value = '';
            refreshToken.value = '';
            userInfo.value = null;
            localStorage.removeItem('access_token');
            localStorage.removeItem('refresh_token');
            localStorage.removeItem('permissions');
            location.reload();
        }
    }
    const getUserInfo = async () => {
        const data = await authApi.getUserInfo();
        userInfo.value = data
        localStorage.setItem('permissions', JSON.stringify(data.permissions));
        return data
    }
    return {
        accessToken, refreshToken, userInfo, setTokens, login, logout, clearToken, getUserInfo
    }
})
export default useUserStore