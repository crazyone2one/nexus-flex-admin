import {defineConfig, presetIcons, presetWind3} from 'unocss'
import transformerDirectives from '@unocss/transformer-directives'

export default defineConfig({
    // ...UnoCSS options
    presets: [presetIcons({
        collections: {
            ion: () => import('@iconify-json/ion/icons.json').then(i => i.default)
        }
    }), presetWind3()],
    transformers: [transformerDirectives()],
})