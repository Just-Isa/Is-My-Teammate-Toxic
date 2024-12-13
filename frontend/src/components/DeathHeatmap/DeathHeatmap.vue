<template>
  <div class="dark-content-overlay" id="dark-content-overlay" style="display: none;"></div>
    <div
      class="heatmap-container"
      :id="'heatmap-container-'+props.g"
      style="display: none;"
      >
      <v-btn v-on:click="hideHeatmap(props.g)" class="close-heatmap" color="white" variant="outlined">close</v-btn>
      <!-- DEATHS PRE 2 MIN -->
      <DeathHeatmapSkulls :deaths="deaths2minBeforeEnd" when="late-deaths"></DeathHeatmapSkulls>
      <!-- DEATHS POST 10 MIN AND PRE 2 MIN -->
      <DeathHeatmapSkulls :deaths="deathsPost10minPre2min" when="midgame-deaths"></DeathHeatmapSkulls>
      <!-- DEATHS PRE 10 MIN -->
      <DeathHeatmapSkulls :deaths="deathsPre10min" when="early-deaths"></DeathHeatmapSkulls>
      <div v-if="
        teamType[teamColor] == 'Blue'
        ">
        <baseB style="transform: translate(15px , 665px); font-size: 30px; width:220px; color: lightblue;"/>
      </div>
      <div v-else-if="
        teamType[teamColor] == 'Red'
        ">
        <baseB style="transform: translate(705px,  -23px); font-size: 30px; color: red;"/>
      </div>
      <div v-else style="color: white;">
        {{ teamType[teamColor] }}
    </div>
  </div>
</template>

<script setup lang="ts">
import baseB from 'vue-material-design-icons/HomeFloorB.vue';
import DeathHeatmapSkulls from './DeathHeatmapSkulls.vue';
import { useLolGameService } from '@/services/LolGameService';
import { onMounted, onUnmounted } from 'vue';
const lolGameService = useLolGameService();

const props = defineProps(['g']);

const teamColor = lolGameService.gameState.gameDetails[props.g].relevantPlayerInfo.team
const deathsPost10minPre2min = lolGameService.gameState.gameDetails[props.g].relevantPlayerInfo.toxicityDTO.deathsPost10minPre2min;
const deathsPre10min = lolGameService.gameState.gameDetails[props.g].relevantPlayerInfo.toxicityDTO.deathsPre10min;
const deaths2minBeforeEnd = lolGameService.gameState.gameDetails[props.g].relevantPlayerInfo.toxicityDTO.deaths2minBeforeEnd;

const teamType : {[code: string] : string} =
  {
    "BLUE":"Blue",
    "RED":"Red"
  }

function hideHeatmap(gameID: string) {
  const id = `heatmap-container-${gameID}`;
  const heatmapContainerElement = document.getElementById(id);
  const darkerBackground = document.getElementById("dark-content-overlay");
  const legend = document.getElementById("heatmap-legend");

  if (heatmapContainerElement && darkerBackground) {
    if (heatmapContainerElement.style.display === '') {
      // Hide all elements and re-enable scrolling
      heatmapContainerElement.style.display = 'none';
      darkerBackground.style.display = 'none';
      document.documentElement.style.overflow = '';
      document.body.style.overflow = '';
    }
  }
}

function handleClickOutside(event: MouseEvent) {
  const heatmapContainer = document.getElementById('dark-content-overlay');
  if (heatmapContainer && heatmapContainer.contains(event.target as Node)) {
    hideHeatmap(props.g);
  } else {
    return;
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style>

.dark-content-overlay {
  position:fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: black;
  z-index: 1000;
  min-width: 100%;
  min-height: 100%;
  opacity: 0.8;
}

.heatmap-container {
  position: fixed;
  z-index: 1001;
  width: 750px;
  height: 750px;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background-image: url('https://ddragon.leagueoflegends.com/cdn/10.18.1/img/map/map11.png');
  background-size: cover;
  background-color: rgba(0, 0, 0, 0.5);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

@media only screen and (max-width: 600px) {
  .top-bar {
    display: flex;
    flex-direction: column;
    max-width: 280px;
  }

  .main-data-table {
    text-align: center;
    margin-top: 20px;
    width: 80%;
  }
}
</style>
