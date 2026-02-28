import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import 'virtual:uno.css'
import pinia from "/@/stores";
import router from "/@/routers";
// 通用字体
import 'vfonts/Lato.css'
// 等宽字体
import 'vfonts/FiraCode.css'

const bootstrap = async () => {
    const app = createApp(App)
    app.use(pinia)
    app.use(router)
    app.mount('#app')
}
bootstrap().then(() => {
})
