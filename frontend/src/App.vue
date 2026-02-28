<script setup lang="ts">
import {darkTheme,lightTheme , createDiscreteApi, zhCN} from 'naive-ui'
import {useAppStore} from './stores'
// import {initMessage} from '/@/utils/message'
import type { ConfigProviderProps } from 'naive-ui'
import {computed, ref} from "vue";

const appStore = useAppStore()

</script>

<template>
  <n-config-provider :theme="appStore.isDark ? darkTheme : null" :locale="zhCN">
    <n-message-provider>
      <n-dialog-provider>
        <router-view/>
      </n-dialog-provider>
    </n-message-provider>
  </n-config-provider>
</template>

<style scoped>
</style>
