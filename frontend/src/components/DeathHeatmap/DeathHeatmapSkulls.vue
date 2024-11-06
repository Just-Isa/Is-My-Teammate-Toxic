<template>
    <div
        v-for="death in deaths"
        class="death-icons"
        :style="{transform: 'translate(' + ((((death.position['x'] / 10) / 2))) +'px,' + (740 - (((death.position['y'] / 10) / 2)))+'px)',}"
        >
        <skull :class="props.when"/>
        <v-tooltip
            activator="parent"
            location="end"
            v-if="Math.floor((death.timestamp/1000) % 60) >= 10">{{Math.floor((death.timestamp/1000/60) << 0)}}:{{Math.floor((death.timestamp/1000) % 60) }}</v-tooltip>
        <v-tooltip
          activator="parent"
          location="end"
          v-else>{{Math.floor((death.timestamp/1000/60) << 0)}}:0{{Math.floor((death.timestamp/1000) % 60) }}</v-tooltip>
    </div>

</template>

<script setup lang="ts">
    import skull from 'vue-material-design-icons/Skull.vue';
    const props = defineProps(['deaths', 'when']);
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
