<template>
    <div style="display: none; color: white;" class="legend" id="heatmap-legend">
      <baseB style="font-size: 25px; color: red;"/> Red Base
      |
      <baseB style="font-size: 25px; color: lightblue;"/> Blue Base
      <br>
      <skull style="color: wheat;"/> Late Deaths  |
      <skull style="color: yellow;"/> Midgame Deaths  |
      <skull style="color: red;"/> Early Deaths
    </div>
</template>

<script setup lang="ts">
import skull from 'vue-material-design-icons/Skull.vue';
import baseB from 'vue-material-design-icons/HomeFloorB.vue';
</script>

<style>
.legend {
  text-align: center;
  margin-left: 36%;
  margin-top: 50px;
  position: absolute;
  z-index: 14;
  top: 2%;
  font-size: 25px;
}
</style>
