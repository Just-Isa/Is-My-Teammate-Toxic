<template>
    <div style="display: none; color: white;" class="legend" id="heatmap-legend">
      <baseB style="font-size: 25px; color: red;"/> Red Base
      <br>
      <baseB style="font-size: 25px; color: lightblue;"/> Blue Base  
      <br>
      <skull style="color: wheat;"/> Lategame Deaths  
      <br>
      <skull style="color: yellow;"/> Midgame Deaths  
      <br>
      <skull style="color: red;"/> Early Deaths
      <br>
      <br>
      Hover over Deaths for the Minute
    </div>
</template>

<script setup lang="ts">
import skull from 'vue-material-design-icons/Skull.vue';
import baseB from 'vue-material-design-icons/HomeFloorB.vue';
</script>

<style>
.legend {
  position: absolute;
  width: 250px;
  z-index: 14;
  top: 10%;
  left: 15%;
  font-size: 25px;
}
</style>