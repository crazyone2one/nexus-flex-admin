<script setup lang="ts">
import {reactive, ref} from 'vue';
import {useRouter} from 'vue-router';
import {useAppStore, useUserStore} from "/@/stores";

const userStore = useUserStore()
const appStore = useAppStore()
const router = useRouter()

const form = reactive({username: 'admin', password: '123456'});
const rules = {
  username: {required: true, message: '请输入账号', trigger: 'blur'},
  password: {required: true, message: '请输入密码', trigger: 'blur'}
}
const loading = ref(false)
const handleLogin = async () => {
  await userStore.login(form.username, form.password)
  await userStore.getUserInfo()
  const {redirect, ...othersQuery} = router.currentRoute.value.query;
  console.log(redirect)
  await router.push({
    name: redirect as string || 'Dashboard',
    query: {
      ...othersQuery,
      orgId: appStore.currentOrgId,
      pId: appStore.currentProjectId,
    }
  })
}
</script>

<template>
  <div class="login-container">
    <n-card title="欢迎登录" style="width: 400px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
      <n-form :model="form" :rules="rules" ref="formRef">
        <n-form-item path="username" label="账号">
          <n-input v-model:value="form.username" placeholder="admin"/>
        </n-form-item>
        <n-form-item path="password" label="密码">
          <n-input v-model:value="form.password" type="password" placeholder="123456"/>
        </n-form-item>
        <n-button type="primary" block @click="handleLogin" :loading="loading">
          登录
        </n-button>
      </n-form>
    </n-card>
  </div>
</template>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}
</style>