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
    </div>
</template>

<script setup lang="ts">
import skull from 'vue-material-design-icons/Skull.vue';
import baseB from 'vue-material-design-icons/HomeFloorB.vue';
</script>