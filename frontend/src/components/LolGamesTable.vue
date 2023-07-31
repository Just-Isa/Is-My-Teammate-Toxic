<template>
    <div v-if="lolGameService.matchHistoryState.LolGames.length > 0">
      <v-table
        fixed-header
        height="550px"
        class="main-data-table"
        >
        <thead>
          <tr class="main-data-table-th">
            <th class="text-center">Queue</th>
            <th class="text-center">Date</th>
            <th class="text-center">Duration</th>
            <th class="text-center">Champ</th>
            <th class="text-center">K/D/A</th>
            <th class="text-center">Lane or AFK</th>
            <th class="text-center">Win</th>
            <th class="text-center">toxicityValues</th>
            <th class="text-center">Death Heatmap</th>
          </tr>
        </thead>
        <tbody>
            <tr v-for="g in lolGameService.matchHistoryState.LolGames">
              <td v-if="
                checkGameStateAndPlayerInfoExist(g)"> 
                  {{ gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] }} 
              </td>
              <td v-if="lolGameService.gameState.gameDetails[g]">{{ lolGameService.gameState.gameDetails[g].dateOfGame }}</td>
              <td v-if="lolGameService.gameState.gameDetails[g]">{{ lolGameService.gameState.gameDetails[g].gameDuration }}</td>
              <td v-if="
                checkGameStateAndPlayerInfoExist(g)"> 
                  {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.champName }}
              </td>
              <td v-if="
                checkGameStateAndPlayerInfoExist(g)"> 
                  {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.kills }} / 
                  {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.deaths }} / 
                  {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.assists }}  
              </td>
              <td v-if="
                checkGameStateAndPlayerInfoExist(g)" >
                  {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.lane }}
              </td>
              <td v-if="
                checkGameStateAndPlayerInfoExist(g) &&
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo.win == true">
                  <span class="win-circle">
                    <v-tooltip
                      activator="parent"
                      location="end"
                    >Player won!</v-tooltip>
                  </span> 
              </td>
              <td v-else-if="
                checkGameStateAndPlayerInfoExist(g) &&
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo.win == false">
                  <span class="lose-circle">
                    <v-tooltip
                      activator="parent"
                      location="end"
                    >Player lost!</v-tooltip>
                  </span> 
              </td>
              <td v-if="!lolGameService.gameState.gameDetails[g]">  
                <v-btn v-on:click="lolGameService.getGame(g)" variant="outlined">Load more details</v-btn>
              </td>
              <td v-if="
                checkGameStateAndPlayerInfoExist(g) && 
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO && 
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.toxicityValues.length > 0"> 
                <p v-for="v in lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.toxicityValues">
                  {{ v }} 
                </p>
              </td>
              <td v-else>
                <span class="win-circle">
                  <v-tooltip
                    activator="parent"
                    location="end"
                  >No Toxicity found!</v-tooltip>
                </span> 
              </td>
              <td v-if="
                checkGameStateAndPlayerInfoExist(g) && 
                checkNormalOrRanked(g) &&
                checkExistenceOfDeathArrays(g)
                "
                >
                <v-btn icon variant="flat" style="padding-bottom: 9px;" v-on:click="revealHeatmap(g)">
                  <deathlyHallows style="transform: scale(2.5); "/>
                </v-btn>
              </td>
            </tr>
        </tbody>
      </v-table>
    </div>
</template>

<script setup lang="ts">
import deathlyHallows from 'vue-material-design-icons/DeathlyHallows.vue';
import { useLolGameService } from '@/services/LolGameService';
 
const lolGameService = useLolGameService();

const gameType: {[code: string] : string;} = 
  {
    "TEAM_BUILDER_DRAFT_UNRANKED_5X5":"Normal", 
    "TEAM_BUILDER_RANKED_SOLO":"Ranked Solo/Duo",
    "ARAM":"ARAM",
    "BOT_5X5_INTRO":"Bot game",
    "RANKED_FLEX_SR":"Ranked Flex",
    "CHERRY":"Arena"
  }

function checkGameStateAndPlayerInfoExist(g: string) : boolean {
  return (lolGameService.gameState.gameDetails[g] != undefined && lolGameService.gameState.gameDetails[g].relevantPlayerInfo != undefined);
}

function checkExistenceOfDeathArrays(g: string) : boolean {
  return (
    lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deaths2minBeforeEnd != undefined ||
    lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPost10minPre2min != undefined ||
    lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPre10min != undefined
    );
}

function checkNormalOrRanked(g: string) : boolean {
  return(
    gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] == 'Normal' ||
    gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] == 'Ranked Solo/Duo' ||
    gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] == 'Ranked Flex'
  );
}

function revealHeatmap(gameID: string) {
  var id: string = "heatmap-container-"+gameID; 
  var heatmapContainerElement = document.getElementById(id);
  var darkerBackroung = document.getElementById("complete-content-container");
  var legend = document.getElementById("heatmap-legend");
  if (heatmapContainerElement && darkerBackroung && legend) {
    if (heatmapContainerElement.style.display == 'none') {
      heatmapContainerElement.style.display = '';
      darkerBackroung.style.display = ''; 
      legend.style.display = '';
    }
  }
}
</script>

<style>

.win-circle {
  height: 25px;
  width: 25px;
  background-color: greenyellow;
  border-radius: 50%;
  display: inline-block;
}

.lose-circle {
  height: 25px;
  width: 25px;
  background-color: red;
  border-radius: 50%;
  display: inline-block;
}
</style>