<template>
  <div class="error">
    <div class="info">404</div>
    <router-link to="/" class="a-link">&lt;返回首页</router-link>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped>
.error {
  text-align: center;
  padding-top: 200px;
  .info {
    font-size: 100px;
    color: black(171, 171, 214);
  }
}
</style>
