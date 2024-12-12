<template>
    <div
          class="heatmap-container"
          :id="'heatmap-container-'+props.g"
          style="display: none;">
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
          <baseB style="transform: translate(15px , 705px); font-size: 25px; width:220px; color: lightblue;"/>
        </div>
        <div v-else-if="
          teamType[teamColor] == 'Red'
          ">
          <baseB style="transform: translate(715px,  10px); font-size: 25px; color: red;"/>
        </div>
        <div v-else style="color: white;">
          {{ teamType[teamColor] }}
        </div>
    </div>
    <HeatmapLegend/>
</template>

<script setup lang="ts">
import HeatmapLegend from './HeatmapLegend.vue';
import baseB from 'vue-material-design-icons/HomeFloorB.vue';
import DeathHeatmapSkulls from './DeathHeatmapSkulls.vue';
import { useLolGameService } from '@/services/LolGameService';
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
  var id: string = "heatmap-container-"+gameID;
  var heatmapContainerElement = document.getElementById(id);
  var darkerBackroung = document.getElementById("complete-content-container");
  var legend = document.getElementById("heatmap-legend");
  if (heatmapContainerElement && darkerBackroung && legend) {
    if (heatmapContainerElement.style.display == '') {
      heatmapContainerElement.style.display = 'none';
      darkerBackroung.style.display = 'none';
      legend.style.display = 'none';
    }
  }
}
</script>

<style>

.heatmap-container {
  position: absolute;
  z-index: 15;
  min-height: 750px;
  min-width: 750px;
  left: 40%;
  top: 10%;
  margin: -20px 0 0 -150px;
  background-image: url('https://ddragon.leagueoflegends.com/cdn/10.18.1/img/map/map11.png');
  background-size: cover;
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
