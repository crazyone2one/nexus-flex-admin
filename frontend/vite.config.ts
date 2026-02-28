import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import path from "path";
import UnoCSS from 'unocss/vite'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {NaiveUiResolver} from 'unplugin-vue-components/resolvers'
// https://vite.dev/config/
export default defineConfig(({mode}) => {
    const env = loadEnv(mode, process.cwd(), '')
    const isProduction = mode === 'production'
    return {
        define: {
            // 提供从 env var 派生的显式应用程序级常量。
            __APP_ENV__: JSON.stringify(env.APP_ENV),
        },
        server: !isProduction ? {
            proxy: {
                '/front': {
                    target: 'http://127.0.0.1:8080/',
                    changeOrigin: true,
                    rewrite: (path: string) => path.replace(new RegExp('^/front'), ''),
                },
            },
            watch: {
                usePolling: true
            }
        } : {},
        plugins: [UnoCSS(), vue(),
            // 自动导入 Vue API (ref, computed 等)
            AutoImport({
                imports: [{
                    'naive-ui': [
                        'useDialog',
                        'useMessage',
                        'useNotification',
                        'useLoadingBar'
                    ]
                }],
                dts: 'src/auto-imports.d.ts',
                // 如果使用了 eslint，可以开启此选项自动生成配置
                eslintrc: {enabled: false}
            }),
            // 自动导入 Naive UI 组件
            Components({
                resolvers: [NaiveUiResolver()],
                dts: 'src/components.d.ts'
            })
        ],
        resolve: {
            alias: [{
                find: /\/@\//,
                replacement: path.resolve(__dirname, '.', 'src') + '/',
            }],
        },
        build: {
            rolldownOptions: {
                output: {}
            }
        }
    }
})
