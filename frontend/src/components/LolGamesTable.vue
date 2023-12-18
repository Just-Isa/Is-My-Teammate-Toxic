<template>
  <div v-if="lolGameService.matchHistoryState.LolGames.length > 0">
    <v-table fixed-header height="550px" class="main-data-table">
      <thead>
        <tr class="main-data-table-th text-center">
          <th class="text-center">Queue</th>
          <th class="text-center">Date</th>
          <th class="text-center">Duration</th>
          <th class="text-center">Champ</th>
          <th class="text-center">Lane</th>
          <th class="text-center">K/D/A</th>
          <th class="text-center">Win</th>
          <th class="text-center">Toxicity</th>
          <th class="text-center">Deaths</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="g in lolGameService.matchHistoryState.LolGames" :key="g">
          <td v-if="checkGameStateAndPlayerInfoExist(g)">
            {{ gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] }}
          </td>
          <td v-if="lolGameService.gameState.gameDetails[g]">
            {{ formatDate(lolGameService.gameState.gameDetails[g].dateOfGame) }}
          </td>
          <td v-if="lolGameService.gameState.gameDetails[g] ">
            {{ lolGameService.gameState.gameDetails[g].gameDuration }}
          </td>
          <td v-if="checkGameStateAndPlayerInfoExist(g)">
            {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.champName }}
          </td>
          <td v-if="checkGameStateAndPlayerInfoExist(g) && gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] !== 'ARAM' ">
            {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.lane }}
          </td>
          <td v-else-if="checkGameStateAndPlayerInfoExist(g) && gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] === 'ARAM'">
             ---
          </td>
          <td v-if="checkGameStateAndPlayerInfoExist(g)">
            {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.kills }} /
            {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.deaths }} /
            {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.assists }}
          </td>
          <td v-if="checkGameStateAndPlayerInfoExist(g) && lolGameService.gameState.gameDetails[g].relevantPlayerInfo.win">
            <span class="win-circle">
              <v-tooltip activator="parent" location="end">Player won!</v-tooltip>
            </span>
          </td>
          <td v-else-if="checkGameStateAndPlayerInfoExist(g) && !lolGameService.gameState.gameDetails[g].relevantPlayerInfo.win">
            <span class="lose-circle">
              <v-tooltip activator="parent" location="end">Player lost!</v-tooltip>
            </span>
          </td>
          <td v-if="!lolGameService.gameState.gameDetails[g]">
            <v-btn v-on:click="lolGameService.getGame(g)" variant="outlined">More</v-btn>
          </td>
          <td v-if="checkGameStateAndPlayerInfoExist(g) && checkToxicityDtoExistence(g) && lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.toxicityValues.length > 0">
            <p v-for="(v, index) in lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.toxicityValues" :key="index">
              {{ v }}
            </p>
          </td>
          <td v-else-if="checkGameStateAndPlayerInfoExist(g) && checkToxicityDtoExistence(g) && lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.toxicityValues.length === 0">
            <span class="win-circle">
              <v-tooltip activator="parent" location="end">No Toxicity found!</v-tooltip>
            </span>
          </td>
          <td v-if="checkGameStateAndPlayerInfoExist(g) && checkNormalOrRanked(g) && checkExistenceOfDeathArrays(g)">
            <v-btn icon variant="flat" class="heatmap-button" v-on:click="revealHeatmap(g)">
              <deathlyHallows style="transform: scale(2.5); "/>
            </v-btn>
            <div class="mobile-info">Not for mobile yet</div>
          </td>
          <td v-else>---</td>
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
    "CHERRY":"Arena",
    "QUICKPLAY_NORMAL":"Quickplay"
  }

function checkGameStateAndPlayerInfoExist(g: string): boolean {
  return lolGameService.gameState.gameDetails[g] !== undefined && lolGameService.gameState.gameDetails[g].relevantPlayerInfo !== undefined;
}

function checkExistenceOfDeathArrays(g: string): boolean {
  const { deaths2minBeforeEnd, deathsPost10minPre2min, deathsPre10min } = lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO;
  return deaths2minBeforeEnd !== undefined && deathsPost10minPre2min !== undefined && deathsPre10min !== undefined;
}

function checkNormalOrRanked(g: string): boolean {
  const gameQueueType = lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType;
  return ["Normal", "Ranked Solo/Duo", "Ranked Flex", "Quickplay"].includes(gameType[gameQueueType]);
}

function checkToxicityDtoExistence(g: string): boolean {
  return lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO !== undefined;
}

function formatDate(date: string): string {
  const currentDate = new Date();
  const splitDateString = date.replace(" ", "").split("/");
  const gameDate = new Date(parseInt(splitDateString[2]), parseInt(splitDateString[1]), parseInt(splitDateString[0]));
  const timeDifference = Math.abs(currentDate.getTime() - gameDate.getTime());
  const daysDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
  return `${daysDifference} days ago`;
}

function revealHeatmap(gameID: string): void {
  const id = "heatmap-container-" + gameID;
  const heatmapContainerElement = document.getElementById(id);
  const darkerBackground = document.getElementById("complete-content-container");
  const legend = document.getElementById("heatmap-legend");
  if (heatmapContainerElement && darkerBackground && legend) {
    if (heatmapContainerElement.style.display === "none") {
      heatmapContainerElement.style.display = "";
      darkerBackground.style.display = "";
      legend.style.display = "";
    }
  }
}
</script>

<style>

.below50kMastery{
  color: lightgreen;
}

.below500kAbove10kMastery{
  color: lightpink
}

.below100kAbove50kMastery{
  color: lightskyblue;
}

.above500kMastery{
  color: red;
}

.mobile-info {
  display: none;
}

th {
  font-size: 20px;
}

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

.header-image {
  margin-top: 10px;
  max-width: 45px;
  max-height: 45px;
}

.heatmap-button {
  padding-bottom: 10px;
}

@media only screen and (max-width: 600px) {
    .main-data-table {
      max-width: 310px;
      height: 400px;
    }

    th {
      font-size: 18px;
    }

    td{
      font-size: 15px;
      max-width: 20px;
    }

    .v-table__wrapper {
      max-width: 310px;
      height: 300px;
      max-height: 400px;
    }

    .heatmap-button {
      display: none;
    }

    .mobile-info {
      display: inline;
    }
  }
</style>
