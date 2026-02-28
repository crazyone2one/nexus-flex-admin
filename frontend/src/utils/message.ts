// src/utils/message.ts
// https://www.naiveui.com/zh-CN/os-theme/components/discrete
import type {ConfigProviderProps} from 'naive-ui'
import {createDiscreteApi, darkTheme, lightTheme} from 'naive-ui'
import {computed, ref} from "vue";


const theme = ref<'light' | 'dark'>('light')
const configProviderPropsRef = computed<ConfigProviderProps>(() => ({
    theme: theme.value === 'light' ? lightTheme : darkTheme
}))
const {message, notification, dialog, loadingBar, modal} = createDiscreteApi(
    ['message', 'dialog', 'notification', 'loadingBar', 'modal'],
    {
        configProviderProps: configProviderPropsRef
    }
)
export {message, notification, dialog, loadingBar, modal}