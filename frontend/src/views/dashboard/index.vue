<script setup lang="ts">
import {message} from '/@/utils/message'
import {authApi} from "/@/api/methods/auth.ts";
import {useRequest} from "alova/client";

const {}=useRequest(()=>authApi.getUserInfo(),{immediate:false})
const handleTestMessage = async () => {
  await authApi.getUserInfo()
  message.info('This is a test message', {duration: 800})
};
</script>

<template>
  <n-result
      status="info"
      title="Dashboard"
      description="This is a dashboard template."
  >
    <template #footer>
      <n-button @click="handleTestMessage">I need information</n-button>
    </template>
  </n-result>
</template>

<style scoped>

</style>