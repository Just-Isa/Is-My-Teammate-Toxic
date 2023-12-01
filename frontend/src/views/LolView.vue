<template>
  <div class="complete-content-container" id="complete-content-container" style="display: none;"></div>
  <Navigation class="navbar"></Navigation>
  <div v-show="!lolGameService.matchHistoryState.finishedGettingGames && clickedSearch" class="loading-animation">
    <img src="../assets/loading-spin.svg">
  </div>
  <div
  v-for="g in lolGameService.matchHistoryState.LolGames"
  v-show="lolGameService.matchHistoryState.finishedGettingGames"
  :key="g">
    <!-- DEATH HEATMAP -->
    <div v-if="
          lolGameService.gameState.gameDetails[g] &&
          lolGameService.gameState.gameDetails[g].relevantPlayerInfo &&
          (
            lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deaths2minBeforeEnd ||
            lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPost10minPre2min ||
            lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPre10min
          )">
        <DeathHeatmap
          :g="g"
          :teamColor="lolGameService.gameState.gameDetails[g].relevantPlayerInfo.team"
          :deaths-post10min-pre2min="lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPost10minPre2min"
          :deaths-pre10min="lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPre10min"
          :deaths2min-before-end="lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deaths2minBeforeEnd">
        </DeathHeatmap>
    </div>
    <!---------------->
  </div>
  <div class="top-bar">
      <div class="summoner-col">
        <v-text-field
          variant="solo-filled"
          class="summoner-name-input"
          type="text"
          required
          v-model="inputName"
          label="Summoner"
          style="text-align: center;"
        />
      </div>
      <div class="region-col">
        <v-select
          variant="solo-filled"
          v-model="inputRegion"
          class="region-input"
          label="Region"
          :items="Object.values(regions)">
        </v-select>
      </div>
      <div class="search-button-col">
        <v-btn
          icon
          size="large"
          theme="dark"
          variant="elevated"
          class="search-button"
          v-on:click="getUserFromService">
            <searchweb/>
        </v-btn>
      </div>
  </div>
  <div v-show="lolGameService.matchHistoryState.finishedGettingGames">
    <PlayerInfo />
    <v-row>
      <v-col cols="1"></v-col>
      <v-col cols="11">
      <LolGamesTable/>
      </v-col>
    </v-row>
    <Footer/>
  </div>
</template>

<script setup lang="ts">
import DeathHeatmap from '@/components/DeathHeatmap/DeathHeatmap.vue';
import searchweb from 'vue-material-design-icons/SearchWeb.vue';
import { useLolGameService } from "@/services/LolGameService";
import LolGamesTable from '@/components/LolGamesTable.vue';
import { useUserService } from "@/services/UserService";
import PlayerInfo from "@/components/PlayerInfo.vue";
import Navigation from "@/components/Navigation.vue";
import Footer from "@/components/Footer.vue";
import { ref } from 'vue';

const lolGameService = useLolGameService();
const userService = useUserService();

const inputName = ref("");
const inputRegion = ref("");
const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};
const regionFlipped: {[code: string] : string;} = {"EUN":"eun1", "EUW":"euw1", "NA":"na1"};

var clickedSearch: boolean = false;

async function getUserFromService() {
    if(inputName.value && inputRegion.value) {
      lolGameService.resetPlayerInfo();
      lolGameService.resetGames();
      console.log(regionFlipped[inputRegion.value])
      await userService.getUserDTO(inputName.value, regionFlipped[inputRegion.value])
      await lolGameService.getMatchHistory();
      clickedSearch = true;
    } else {
      alert("Check name and Region please")
    }
}

/* MAYBE FOR LATER USE*/

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
  margin-left: 100px;
  margin-top: 20px;
  display: flex;
  justify-content: space-evenly;
}

.summoner-name-input {
  max-width: 500px;
  min-width: 300px;
}

.region-input {
  max-width: 500px;
  min-width: 300px;
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
  backdrop-filter: blur(5px);
}

.loading-animation {
  position: absolute;
  top: 50%;
  left: calc(50% - 125px);
  transform: translate(0, -50%) scale(3);
}


@media only screen and (max-width: 600px) {
    .top-bar {
      display: flex;
      flex-direction: column;
      max-width: 300px;
    }
  }

  @media only screen and (max-width: 400px) {
    .top-bar {
      display: flex;
      flex-direction: column;
      max-width: 250px;
    }
  }
</style>
