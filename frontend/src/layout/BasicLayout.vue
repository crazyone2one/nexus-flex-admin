<script setup lang="ts">
import {useAppStore} from '/@/stores'
import type {MenuOption} from "naive-ui";
import {computed, h, onMounted, onUnmounted} from "vue";
// import {useRoute} from "vue-router";
import {useWindowSize} from '@vueuse/core'

const {height} = useWindowSize()
const appStore = useAppStore()
// const route = useRoute()
// 菜单配置
const menuOptions = computed<MenuOption[]>(() => [
  {key: '/dashboard', label: '工作台', icon: () => h('div', {class: 'i-ion:home-outline'})},
  {key: '/demo', label: 'demo', icon: () => h("div", {class: 'i-ion:social-chrome-outline'})},
])
// const currentTitle = computed(() => route.meta.title as string || '')
// 响应式监听
const handleResize = () => {
  const width = window.innerWidth
  appStore.setMobile(width < 768)
}

onMounted(() => {
  handleResize()
  window.addEventListener('resize', handleResize)
})
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<template>
  <n-layout class="layout-container" position="absolute">
    <!-- 顶部 Header -->
    <n-layout-header class="nav" bordered
                     style="--side-padding: 64px;
                     grid-template-columns:calc(272px - var(--side-padding)) 1fr auto ;
                     height: 64px">
      <n-text class="logo-area">
        <n-icon size="32" color="#18a058">
          <div class="i-ion:logo-ionic"/>
        </n-icon>
        <span v-show="!appStore.isMobile" class="logo-text">Nexus Flex</span>
      </n-text>
      <n-flex justify="space-between">
        <!--        <n-flex justify="center">-->
        <!--          <n-button v-if="appStore.isMobile" quaternary circle @click="appStore.toggleCollapsed">-->
        <!--            <template #icon>-->
        <!--              <n-icon>-->
        <!--                <div class="i-ion:menu-outline"/>-->
        <!--              </n-icon>-->
        <!--            </template>-->
        <!--          </n-button>-->
        <!--                    <n-breadcrumb v-else style="margin-left: 10px;">-->
        <!--                      <n-breadcrumb-item>首页</n-breadcrumb-item>-->
        <!--                      <n-breadcrumb-item>{{ currentTitle }}</n-breadcrumb-item>-->
        <!--                    </n-breadcrumb>-->
        <!--        </n-flex>-->
      </n-flex>
      <div class="nav-end">
        <!--        <n-button quaternary circle @click="appStore.toggleTheme">-->
        <!--          <template #icon>-->
        <!--            <n-icon>-->
        <!--              <div :class="appStore.isDark ? 'i-ion:sunny-outline' : 'i-ion:moon-outline'"/>-->
        <!--            </n-icon>-->
        <!--          </template>-->
        <!--        </n-button>-->
        <!--        <n-divider vertical/>-->
        <n-dropdown>
          <div style="cursor: pointer; display: flex; align-items: center; gap: 8px;">
            <n-avatar round size="small" src="https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg"/>
            <span v-if="!appStore.isMobile" style="font-size: 14px;">Admin</span>
          </div>
        </n-dropdown>
      </div>
    </n-layout-header>
    <n-layout has-sider position="absolute" :style="{ height: height-32 + 'px' }" style="top: 64px">
      <!-- 侧边栏 -->
      <n-layout-sider
          :collapsed="appStore.collapsed"
          :collapsed-width="64"
          :width="200"
          collapse-mode="width"
          show-trigger="arrow-circle"
          bordered
          :native-scrollbar="false"
          @collapse="appStore.toggleCollapsed"
          @expand="appStore.toggleCollapsed"
      >
        <n-menu
            :options="menuOptions"
            :collapsed="appStore.collapsed && !appStore.isMobile"
            :collapsed-width="64"
            :collapsed-icon-size="22"
        />
      </n-layout-sider>
      <!-- 内容区 -->
      <n-layout-content>
        <n-card :bordered="false" style="min-height: 100%;">
          <router-view/>
        </n-card>
      </n-layout-content>
    </n-layout>
    <!--    <n-layout-footer bordered style="text-align: center; padding: 16px; color: #999;">-->
    <!--      © 2026 Nexus Flex. Built with Vue3 + TS.-->
    <!--    </n-layout-footer>-->
  </n-layout>

</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.nav, .logo-area {
  align-items: center;
}

.nav {
  display: grid;
  grid-template-rows: calc(var(--header-height) - 1px);
  padding: 0 var(--side-padding);
}

.nav-end {
  align-items: center;
  display: flex;
}

.logo-area {
  cursor: pointer;
  display: flex;
  font-size: 18px;
}

.logo-area > i {
  height: 32px;
  margin-right: 12px;
  width: 32px;
}

/* 移动端适配调整 */
@media (max-width: 768px) {
  .sidebar {
    box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);
  }
}
</style>