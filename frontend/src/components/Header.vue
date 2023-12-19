<template>
  <div class="header bg-deep-purple">
    <p class="header-text">{{ props.name }}</p>
  </div>
</template>

<script setup lang="ts">
  const props = defineProps({
    name: String
  });
</script>

<style>
  .header {
    z-index: 0;
    position: fixed;
    top: 0;
    width: 100%;
    height: 50px;
    color: black;
    text-align: center;
    line-height: 30px;
    margin-bottom: 50px;
    font-size: 20px;
  }

  .header-text {
    line-height: 50px;
  }
</style>
