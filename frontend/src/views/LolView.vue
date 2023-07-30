<template>
  <div class="complete-content-container" id="complete-content-container"  style="display: none;"></div>
  <Navigation class="navbar"></Navigation>
  <div v-for="g in lolGameService.matchHistoryState.LolGames">
    <div v-if="
              lolGameService.gameState.gameDetails[g] && 
              lolGameService.gameState.gameDetails[g].relevantPlayerInfo && 
              (
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deaths2minBeforeEnd ||
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPost10minPre2min ||
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPre10min
              )"
              class="heatmap-container" 
              :id="'heatmap-container-'+g" 
              style="display: none;">
            <v-btn v-on:click="hideHeatmap(g)" class="close-heatmap" color="white" variant="outlined">close</v-btn>
            <!-- DEATHS PRE 2 MIN -->
            <DeathHeatmap :deaths="lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deaths2minBeforeEnd" when="late-deaths"></DeathHeatmap>
            <!-- DEATHS POST 10 MIN AND PRE 2 MIN -->
            <DeathHeatmap :deaths="lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPost10minPre2min" when="midgame-deaths"></DeathHeatmap>
            <!-- DEATHS PRE 10 MIN -->
            <DeathHeatmap :deaths="lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPre10min" when="early-deaths"></DeathHeatmap>
            <div v-if="              
              teamType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.team] == 'Red'
              ">
              <baseB style="transform: translate(15px , 705px); font-size: 25px; width:220px; color: lightblue;"/>  
            </div>
            <div v-else-if="
              teamType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.team] == 'Blue'
              ">
              <baseB style="transform: translate(715px,  10px); font-size: 25px; color: red;"/>  
            </div>
            <div v-else style="color: white;">
              {{ teamType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.team] }}
            </div>
            
    </div>
  </div>
  <div class="top-bar">
    <v-row>
      <v-col cols="4"></v-col>
      <v-col cols="2" class="summoner-col">
        <v-text-field 
          variant="solo-filled" 
          class="summoner-name-input" 
          type="text" 
          required 
          v-model="inputName" 
          label="Summoner" 
          style="text-align: center;"
        /> 
      </v-col>
      <v-col cols="1" class="region-col">
        <v-select 
          class="select-region" 
          variant="solo-filled" 
          v-model="inputRegion" 
          :items="Object.values(regions)">
        </v-select>
      </v-col>
      <v-col cols="1" class="search-button-col">
        <v-btn 
          icon 
          size="large" 
          theme="dark" 
          variant="elevated" 
          class="search-button" 
          v-on:click="getUserFromService">
            <searchweb/>  
        </v-btn> 
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
                lolGameService.gameState.gameDetails[g] && 
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo"> 
                  {{ gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] }} 
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
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo" >
                  {{ lolGameService.gameState.gameDetails[g].relevantPlayerInfo.lane }}
              </td>
              <td v-if="
                lolGameService.gameState.gameDetails[g] && 
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo &&
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo.win == true">
                  <span class="win-circle">
                    <v-tooltip
                      activator="parent"
                      location="end"
                    >Player won!</v-tooltip>
                  </span> 
              </td>
              <td v-else-if="
                lolGameService.gameState.gameDetails[g] && 
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo &&
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
                lolGameService.gameState.gameDetails[g] && 
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo && 
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
                lolGameService.gameState.gameDetails[g] && 
                lolGameService.gameState.gameDetails[g].relevantPlayerInfo && 
                (
                  gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] == 'Normal' ||
                  gameType[lolGameService.gameState.gameDetails[g].relevantPlayerInfo.gameQueueType] == 'Ranked Solo/Duo'
                ) &&
                (
                  lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deaths2minBeforeEnd ||
                  lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPost10minPre2min ||
                  lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPre10min
                )"
                >
                <v-btn icon variant="flat" style="padding-bottom: 9px;" v-on:click="revealHeatmap(g)">
                  <deathlyHallows style="transform: scale(2.5); "/>
                </v-btn>
              </td>
            </tr>
        </tbody>
      </v-table>
    </div>
    </v-col>
  </v-row>
  <div class="footer bg-deep-purple">
    <p>Shoutouts to Racer, Phen & Toon (and simpleflips ig)</p>
  </div>
</template>

<script setup lang="ts">
import searchweb from 'vue-material-design-icons/SearchWeb.vue';
import deathlyHallows from 'vue-material-design-icons/DeathlyHallows.vue';
import skull from 'vue-material-design-icons/Skull.vue';
import baseB from 'vue-material-design-icons/HomeFloorB.vue';
import { useLolGameService } from "@/services/LolGameService";
import { useUserService } from "@/services/UserService";
import PlayerInfo from "@/components/PlayerInfo.vue";
import Navigation from "@/components/Navigation.vue";
import { ref } from 'vue';
import DeathHeatmap from '@/components/DeathHeatmap.vue';

const lolGameService = useLolGameService();
const userService = useUserService();

const inputName = ref("");
const inputRegion = ref("");
const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};

const gameType: {[code: string] : string;} = 
  {
    "TEAM_BUILDER_DRAFT_UNRANKED_5X5":"Normal", 
    "TEAM_BUILDER_RANKED_SOLO":"Ranked Solo/Duo",
    "ARAM":"ARAM",
    "BOT_5X5_INTRO":"Bot game",
    "RANKED_FLEX_SR":"Ranked Flex",
    "CHERRY":"Arena"
  }

const teamType : {[code: string] : string} = 
  {
    "BLUE":"Blue",
    "RED":"Red"
  }

async function getUserFromService() {
    if(inputName.value) {
      lolGameService.resetPlayerInfo();
      await userService.getUserDTO(inputName.value, inputRegion.value)
      await lolGameService.getMatchHistory();
    } else {
        console.log("No Name given")
    }
}

function hideHeatmap(gameID: string) {
  var id: string = "heatmap-container-"+gameID; 
  var heatmapContainerElement = document.getElementById(id);
  var darkerBackroung = document.getElementById("complete-content-container");
  if (heatmapContainerElement && darkerBackroung) {
    if (heatmapContainerElement.style.display == '') {
      heatmapContainerElement.style.display = 'none';
      darkerBackroung.style.display = 'none'; 
    }
  }
}

function revealHeatmap(gameID: string) {
  var id: string = "heatmap-container-"+gameID; 
  var heatmapContainerElement = document.getElementById(id);
  var darkerBackroung = document.getElementById("complete-content-container");
  if (heatmapContainerElement && darkerBackroung) {
    if (heatmapContainerElement.style.display == 'none') {
      heatmapContainerElement.style.display = '';
      darkerBackroung.style.display = ''; 
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
  background-color: #bbb;
  border-radius: 50%;
  display: inline-block;
}

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
  text-align: center;
  margin: 20px 100px 20px 100px;
  margin-top: 20px;
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

.death-button {
  font-size: xx-large;
  padding-bottom: 8px; 
}

.container {
    display: flex;
    size: 100%;
}

.heatmap-container {
  position: absolute;
  z-index: 15;
  min-height: 750px;
  min-width: 755px;
  margin-top: 20px;
  left: 40%;
  top: 20%;
  margin: -100px 0 0 -150px;
  background-image: url('https://ddragon.leagueoflegends.com/cdn/10.18.1/img/map/map11.png');
  background-size: cover;
}

.close-heatmap {
  position: fixed;
  margin-top: 10px;
  margin-left: 10px;
}

.complete-content-container {
  z-index: 14;
  position: fixed;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-color: rgba(0, 0, 0, 0.7);
}

</style>
