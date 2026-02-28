import {defineStore} from 'pinia';
import {ref} from "vue";

const useAppStore = defineStore('app', () => {
    // 侧边栏折叠状态
    const collapsed = ref<boolean>(false)
    // 移动端标记
    const isMobile = ref<boolean>(false)
    const isDark = ref(false)
    const loading = ref<boolean>(false)
    const loadingTip = ref<string>('')
    const currentOrgId = ref<string>('')
    const currentProjectId = ref<string>('')
    const toggleCollapsed = () => {
        collapsed.value = !collapsed.value
    }
    const setMobile = (val: boolean) => {
        isMobile.value = val
        collapsed.value = val;
    }
    const toggleTheme = () => {
        isDark.value = !isDark.value
    }
    const setCurrentOrgId = (id: string) => {
        currentOrgId.value = id
    }
    const setCurrentProjectId = (id: string) => {
        currentProjectId.value = id
    }
    const showLoading = (tip = '') => {
        loading.value = true
        loadingTip.value = tip || 'Loading...'
    }
    const hideLoading = () => {
        loading.value = false
    }
    return {
        collapsed,
        isMobile,
        isDark,
        loading,
        loadingTip,
        currentOrgId,
        currentProjectId,
        toggleCollapsed,
        setMobile,
        setCurrentOrgId,
        setCurrentProjectId,
        toggleTheme,
        showLoading, hideLoading
    }
})
export default useAppStore