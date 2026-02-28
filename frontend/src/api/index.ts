import {createAlova} from 'alova';
import VueHook from 'alova/vue';
import adapterFetch from 'alova/fetch';
import {useAppStore, useUserStore} from "/@/stores";
import {createServerTokenAuthentication} from 'alova/client';
import {authApi} from "/@/api/methods/auth.ts";
import router from "/@/routers";
import {message} from '/@/utils/message'

const {onAuthRequired, onResponseRefreshToken} = createServerTokenAuthentication({
    refreshTokenOnSuccess: {
        isExpired: async (response, method) => {
            // const res = await response.clone().json();
            const isExpired = method.meta && method.meta.isExpired;
            return !method.url.includes('/auth/refresh') && (response.status === 401) && !isExpired;
        },
        handler: async (_response, method) => {
            method.meta = method.meta || {};
            method.meta.isExpired = true;
            const userStore = useUserStore();
            const refreshToken = userStore.refreshToken;
            if (!refreshToken) {
                // 没有 refresh token，直接跳转登录
                await userStore.logout();
                await router.push({
                    name: 'login',
                    query: {
                        ...router.currentRoute.value.query,
                        redirect: router.currentRoute.value.name as string,
                    },
                });
                throw new Error('No refresh token');
            }
            const {access_token, refresh_token} = await authApi.refreshToken();
            userStore.setTokens(access_token, refresh_token);
        }
    },
    assignToken: (method) => {
        const userStore = useUserStore();
        if (userStore.accessToken && (!method.meta?.authRole || method.meta?.authRole !== 'refreshToken')) {
            method.config.headers['Authorization'] = `Bearer ${userStore.accessToken}`;
        }
        if (userStore.refreshToken && method.meta?.authRole === 'refreshToken') {
            method.config.headers['Authorization'] = `Bearer ${userStore.refreshToken}`;
        }
    },
})

const NFR = createAlova({
    baseURL: `${window.location.origin}/${import.meta.env.VITE_API_BASE_URL}`,
    statesHook: VueHook,
    requestAdapter: adapterFetch(),
    timeout: 300 * 1000,
    beforeRequest: onAuthRequired((method) => {
            const appStore = useAppStore();
            method.config.headers = {
                ...method.config.headers,
                'PROJECT': appStore.currentProjectId,
                'ORGANIZATION': appStore.currentOrgId,
                // 'Accept-Language': currentLocale.value,
            };
            appStore.showLoading();
        }
    ),
    responded: onResponseRefreshToken({
        onSuccess: async (response, method) => {
            const appStore = useAppStore();
            appStore.hideLoading();
            if (response.status >= 400) {
                const json = await response.clone().json();
                switch (response.status) {
                    case 403:
                        message.error(json.message);
                        break;
                    default:
                        break;
                }
            }
            if (method.meta?.isBlob) {
                return response.blob();
            }
            const resData = await response.clone().json();
            if (resData.code !== 100200) {
                switch (resData.code) {
                    case 100400:
                        message.error(resData.message || '参数校验失败');
                        break;
                    case 100403:
                        message.error(resData.message || '权限不足');
                        break;
                    case 100500:
                        message.error('服务器错误');
                        break;
                    default:
                        message.error(resData.message || '请求失败');
                        break;
                }
                throw new Error(resData.message || 'Request failed');
            }
            return resData.data;
        },
        onError: async (error) => {
            console.error('Global Error:', error);
            const appStore = useAppStore();
            appStore.hideLoading();
            throw error;
        },
        onComplete: () => {
            const appStore = useAppStore();
            appStore.hideLoading();
        }
    }),
})
export default NFR;