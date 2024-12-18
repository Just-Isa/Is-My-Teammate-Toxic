<template>
  <div v-if="playerMasteryService.playerMasteryState.playerMastery.length > 0">
    <v-table fixed-header height="400px" class="main-data-table">
      <thead>
        <tr class="main-data-table-th text-center">
          <th class="text-center">Champion</th>
          <th class="text-center">Level</th>
          <th class="text-center">Points</th>
        </tr>
      </thead>
      <tbody v-if="playerMasteryService.playerMasteryState.playerMastery">
        <tr v-for="i in playerMasteryService.playerMasteryState.playerMastery" :key="i.championId">
          <td class="champion-name-col">
            {{ lolChampsService.lolChampState.data[i.championId] }}
          </td>
          <td :class="
            i.championLevel >= 10 && i.championLevel < 30? 'below50kMastery' :
            i.championLevel >= 30 && i.championLevel < 100? 'below200kAbove100kMastery' :
            i.championLevel >= 100? 'above500kMastery' :
            ''
            ">
            {{ i.championLevel }}
          </td>
          <td :class="
            i.championPoints < 50000? 'below50kMastery' :
            i.championPoints > 50000 && i.championPoints < 300000? 'below50kMastery' :
            i.championPoints > 300000 && i.championPoints < 750000? 'below200kAbove100kMastery' :
            i.championPoints > 500000 ? 'above500kMastery' : ''">
            {{ i.championPoints }}
          </td>
        </tr>
      </tbody>
    </v-table>
  </div>
</template>

<script setup lang="ts">
import { usePlayerMasteryService } from '@/services/PlayerMasteryService';
import { useLolChampsService } from '@/services/LolChampService';

const lolChampsService = useLolChampsService();
const playerMasteryService = usePlayerMasteryService();


</script>

<style>

.main-data-table {
  text-align: center;
  margin: 20px 0 50px 0px;
  min-width: 1200px;
}

.below50kMastery{
  color: lightgreen;
}

.below200kAbove100kMastery{
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

.champion-name-col {
  width: 40%;
}

.heatmap-button {
  padding-bottom: 10px;
}

@media only screen and (max-width: 600px) {
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

    .mobile-info {
      display: inline;
    }
  }
</style>
