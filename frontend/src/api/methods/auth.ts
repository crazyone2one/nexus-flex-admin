import NFR from "/@/api";
import type {UserInfo} from "/@/stores/modules/user";

export const authApi = {
    login: (data: { username: string, password: string }) => {
        const method = NFR.Post<{access_token: string, refresh_token: string}>('/auth/login', data)
        method.meta = {authRole: null,};
        return method;
    },
    getUserInfo: () => NFR.Get<UserInfo>('/system/user/info', {}),
    logout: () => NFR.Post('/auth/logout', {}, {meta: {authRole: 'logout'}}),
    refreshToken: () => {
        const method = NFR.Post<{access_token: string, refresh_token: string}>('/auth/refresh', {});
        method.meta = {authRole: 'refreshToken'};
        return method;
    },
    getPublicKey: () => NFR.Get('/auth/get-key', {meta: {authRole: null}}),
}