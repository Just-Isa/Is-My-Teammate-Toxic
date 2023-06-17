<template>
  <h1>HOME</h1>
  <div class="tft">
    <img alt="Vue logo" src="../assets/logo.png" />
  </div>
  <PlayerInfo />
  <br>
</template>

<script setup lang="ts">
import PlayerInfo from "@/components/PlayerInfo.vue";
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>