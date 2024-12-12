<template>
  <div
      v-for="death in deaths"
      :key="death.timestamp"
      class="death-icons"
      :style="{ transform: calculateTransform(death.position) }">
      <skull :class="props.when"/>
      <v-tooltip
          activator="parent"
          location="end"
          v-if="Math.floor((death.timestamp/1000) % 60) >= 10">
          {{ formatTimestamp(death.timestamp) }}
      </v-tooltip>
      <v-tooltip
          activator="parent"
          location="end"
          v-else>
          {{ formatTimestamp(death.timestamp, true) }}
      </v-tooltip>
  </div>
</template>

<script setup lang="ts">
  import skull from 'vue-material-design-icons/Skull.vue';
  const props = defineProps(['deaths', 'when']);

  function calculateTransform(position: { x: number; y: number }): string {
      // Perform the position calculations here
      const x = (position.x / 10) / 2;  // Position calculation for x
      const y = 700 - ((position.y / 10) / 2);  // Position calculation for y
      return `translate(${x}px, ${y}px)`; // Return the calculated transform
  }

  function formatTimestamp(timestamp: number, prependZero: boolean = false): string {
      const minutes = Math.floor(timestamp / 1000 / 60);
      const seconds = Math.floor((timestamp / 1000) % 60);
      return `${minutes}:${prependZero && seconds < 10 ? '0' : ''}${seconds}`;
  }
</script>


<style>
.late-deaths {
  color: wheat;
  transform: scale(1.5);
}

.midgame-deaths {
  color: yellow;
  transform: scale(1.5);
}

.early-deaths {
  color: red;
  transform: scale(1.5);
}


.death-icons {
  position: fixed;
  color: white;
}
</style>
