<template>
  <div v-if="playerMasteryService.playerMasteryState.playerMastery.length > 0">
    <v-table fixed-header height="550px" class="main-data-table">
      <thead>
        <tr class="main-data-table-th text-center">
          <th class="text-center">Champion</th>
          <th class="text-center">Level</th>
          <th class="text-center">Points</th>
          <th class="text-center">Chest</th>

        </tr>
      </thead>
      <tbody v-if="playerMasteryService.playerMasteryState.playerMastery">
        <tr v-for="i in playerMasteryService.playerMasteryState.playerMastery" :key="i.championId">
          <td>
            {{ lolChampsService.lolChampState.data[i.championId] }}
          </td>
          <td if>
            {{ i.championLevel }}
          </td>
          <td>
            {{ i.championPoints }}
          </td>
          <td v-if="i.chestGranted">
            <span class="lose-circle">
              <v-tooltip activator="parent" location="end">Chest Aquired</v-tooltip>
            </span>
          </td>
          <td v-else>
            <span class="win-circle">
              <v-tooltip activator="parent" location="end">Chest Available</v-tooltip>
            </span>
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
