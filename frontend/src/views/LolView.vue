<template>
  <Navigation class="navbar"></Navigation>
  <div class="top-bar">
    <v-row>
      <v-col cols="4"></v-col>
      <v-col cols="2" class="summoner-col">
        <v-text-field variant="solo-filled" class="summoner-name-input" type="text" required v-model="inputName" label="Summoner" style="text-align: center;"/> 
      </v-col>
      <!-- <select v-model="selectedRegion" placeholder="NA">
        <option v-for="key, value in Object.entries(regions)" :selected="key[1] == 'EUW'" :value="key[0]">{{key[1]}}</option>
      </select> --> 
      <v-col cols="1" class="region-col">
        <v-select class="select-region" variant="solo-filled" v-model="inputRegion" :items="Object.values(regions)"></v-select>
      </v-col>
      <v-col cols="1" class="search-button-col">
        <v-btn icon size="large" variant="elevated" class="search-button" v-on:click="getUserFromService"><searchweb/></v-btn> 
      </v-col>
      <v-col cols="4"></v-col>
  </v-row>
  </div>
  <PlayerInfo />

  <v-row>
    <v-col cols="1"></v-col>
    <v-col cols="11">
    <div v-if="lolGameService.matchHistoryState.LolGames.length > 0">
      <v-table
        fixed-header
        height="600px"
        class="main-data-table"
        >
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
        <tbody>
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
      </v-table>
    </div>
    </v-col>
  </v-row>
  <div class="footer bg-deep-purple">
    <p>Shoutouts to Racer & Phen (and simpleflips ig)</p>
  </div>
</template>

<script setup lang="ts">
import PlayerInfo from "@/components/PlayerInfo.vue";
import { useUserService } from "@/services/UserService";
import { ref } from 'vue';
import { useLolGameService } from "@/services/LolGameService";
import searchweb from 'vue-material-design-icons/SearchWeb.vue';
import Navigation from "@/components/Navigation.vue";

const lolGameService = useLolGameService();
const userService = useUserService();

const inputName = ref("");
const inputRegion = ref("");
const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};

async function getUserFromService() {
    if(inputName.value) {
      await userService.getUserDTO(inputName.value, inputRegion.value)
      await lolGameService.getMatchHistory();
    } else {
        console.log("No Name given")
    }
}

</script>

<style>

.footer {
  z-index: 0;
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 30px;
  color: black;
  text-align: center;
  line-height: 30px;
}

.main-data-table {
  width: 90%;
  margin-left: 100px;
}

.summoner-col {
  display: flex;
  justify-content: center;
}

.region-col {
  display: flex;
  justify-content: center;
}

.search-button-col {
  display: flex;
  justify-content: center;
}

.header-league {
  margin-bottom: 20px;
}

.top-bar {
  margin-top: 50px;
  display: flex;
  justify-content: space-around;
}

.summoner-name-input {
  max-width: 500px;
}

.select-region {
  max-width: 100px;
}

.search-button {
  font-size: xxx-large;
  padding-bottom: 8px; 
}


</style>
