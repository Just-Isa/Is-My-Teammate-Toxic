<template>
  <h1>LEAGUE</h1>
  <div class="tft">
    <img alt="Vue logo" src="../assets/tft.png" style="width: 20px; height: 20px;" />
  </div>
  <input type="text" required v-model="inputName" placeholder="Summoner" style="text-align: center;"/> |
    <select v-model="selectedRegion" placeholder="NA">
      <option v-for="key, value in Object.entries(regions)" :selected="key[1] == 'EUW'" :value="key[0]">{{key[1]}}</option>
    </select> |
  <button v-on:click="getUserFromService">Search</button> |
  <PlayerInfo />
  
  <div v-if="lolGameService.matchHistoryState.LolGames.length">
    <table style="margin-left: auto; margin-right: auto; border-spacing:15px 0;">
      <thead >
        <tr>
          <th>Queue</th>
          <th>Date</th>
          <th>Duration</th>
          <th>Champ</th>
          <th>K/D/A</th>
          <th>Lane or AFK</th>
          <th>Win</th>
          <th>toxicityValues</th>
        </tr>
      </thead>
      <tbody >
          <tr v-for="g in lolGameService.matchHistoryState.LolGames">
            <td v-if="
              lolGameService.gameState.gameDetails[g] && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo"> 
                {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType }} 
            </td>
            <td v-if="lolGameService.gameState.gameDetails[g]">{{ lolGameService.gameState.gameDetails[g].dateOfGame }}</td>
            <td v-if="lolGameService.gameState.gameDetails[g]">{{ lolGameService.gameState.gameDetails[g].gameDuration }}</td>
            <td v-if="
              lolGameService.gameState.gameDetails[g] && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo"> 
                {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.champName }}
            </td>
            <td v-if="
              lolGameService.gameState.gameDetails[g] && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo"> 
                {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.kills }} / 
                {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.deaths }} / 
                {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.assists }}  
            </td>
            <td v-if="
              lolGameService.gameState.gameDetails[g] && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo"> 
                {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.lane }}
            </td>
            <td v-if="
              lolGameService.gameState.gameDetails[g] && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo"> 
                {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.win }}
            </td>
            <td v-if="!lolGameService.gameState.gameDetails[g]">  
              <button v-on:click="lolGameService.getGame(g)">
                load more details
              </button>
            </td>
            <td v-if="
              lolGameService.gameState.gameDetails[g] && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.toxicityValues.length > 0"> 
              <p v-for="v in lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.toxicityValues">
                {{ v }} 
              </p>
            </td>
          </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import PlayerInfo from "@/components/PlayerInfo.vue";
import { useUserService } from "@/services/UserService";
import { ref, watch } from 'vue';
import { useLolGameService } from "@/services/LolGameService";

const lolGameService = useLolGameService();
const userService = useUserService();

const inputName = ref("");
const selectedRegion = ref("");
const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};

async function getUserFromService() {
    if(inputName.value) {
      await userService.getUserDTO(inputName.value, regions[selectedRegion.value])
      await lolGameService.getMatchHistory();
    } else {
        console.log("No Name given")
    }
}

</script>
