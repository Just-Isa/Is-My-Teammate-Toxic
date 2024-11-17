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
    <div v-if="hasToxicityData(g)">
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
  <div class="search-and-info">
    <v-card
        variant="elevated"
        class="search-container-card">
      <div class="search-inputs-button">
        <div class="search-inputs">
          <div class="summoner-col">
            <v-text-field
              variant="solo-filled"
              class="summoner-name-input"
              type="text"
              required
              v-model="inputName"
              label="Summoner + #TAG"
              style="text-align: center;"
            />
          </div>
          <div class="region-col">
            <v-select
              variant="solo-filled"
              v-model="inputRegion"
              class="region-input"
              label="Region"
              :items="Object.values(regions)"
            ></v-select>
          </div>
        </div>
        <div class="search-button-col">
          <v-btn
            icon
            size="large"
            theme="dark"
            variant="outlined"
            class="search-button"
            v-on:click="getUserFromService"
            >
              <searchweb />
            </v-btn>
          </div>
      </div>
    </v-card>
    <PlayerInfo/>
  </div>
  <div v-show="lolGameService.matchHistoryState.finishedGettingGames">
    <v-row>
      <v-col cols="1"></v-col>
      <v-col cols="11">
      <LolGamesTable/>
      </v-col>
    </v-row>
  </div>
</template>

<script setup lang="ts">
import DeathHeatmap from '../components/DeathHeatmap/DeathHeatmap.vue';
import searchweb from 'vue-material-design-icons/SearchWeb.vue';
import { useLolGameService } from "@/services/LolGameService";
import { useLolChampsService } from '@/services/LolChampService';
import LolGamesTable from '../components/LolGamesTable.vue';
import { useUserService } from "@/services/UserService";
import PlayerInfo from "../components/PlayerInfo.vue";
import Navigation from "../components/Navigation.vue";
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { usePlayerMasteryService } from '@/services/PlayerMasteryService';

const lolGameService = useLolGameService();
const userService = useUserService();
const lolChampService = useLolChampsService();
const playerMasteryService = usePlayerMasteryService();

const inputName = ref("");
const inputRegion = ref("");
const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};
const regionFlipped: {[code: string] : string;} = {"EUN":"eun1", "EUW":"euw1", "NA":"na1"};

var clickedSearch: boolean = false;
const route = useRoute();

onMounted(async () => {
  inputRegion.value = (regions['euw1']);
  if (
    route.query.name &&
    route.query.tag &&
    route.query.region)
  {
    await setParams();
    getUserFromService();
  }else if(
    userService.userState.user.name &&
    userService.userState.user.tag &&
    userService.userState.userRegion
    )
  {
    inputName.value = userService.userState.user.name + "#" + userService.userState.user.tag;
    inputRegion.value = (regions[userService.userState.userRegion.toLowerCase()]);
  }
});

async function setParams() {
  inputName.value = route.query.name + "#" + route.query.tag;
  inputRegion.value = (regions[(route.query.region as string).toLowerCase()]);
  return new Promise((resolve) => setTimeout(resolve, 1000))
}

function hasToxicityData(g: any) {
  return lolGameService.gameState.gameDetails[g] &&
          lolGameService.gameState.gameDetails[g].relevantPlayerInfo &&
          (
            lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deaths2minBeforeEnd ||
            lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPost10minPre2min ||
            lolGameService.gameState.gameDetails[g].relevantPlayerInfo.toxicityDTO.deathsPre10min
          );
}

async function getUserFromService() {
    if(inputName.value && inputRegion.value) {
      if(!inputName.value.includes("#")) {
        alert("Please make sure to include the #TAG");
        return;
      }
      lolGameService.resetPlayerInfo();
      lolGameService.resetGames();
      await lolChampService.getAllChamps();
      await userService.getUserDTO(inputName.value, regionFlipped[inputRegion.value])
      await lolGameService.getMatchHistory();
      await playerMasteryService.getPlayerMastery();
      clickedSearch = true;
    } else {
      alert("Check name and Region please")
    }
}

</script>

<style>
.main-data-table {
  text-align: center;
  margin: 20px 100px 20px 100px;
  margin-top: 20px;
  width: 85%;
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

.search-container-card {
  margin-top: 50px;
  margin-left: 50px;
  margin-right: 50px;
  margin-bottom: 50px;
  max-width: 400px;
  max-height: 200px;
  padding: 30px;
}

.search-inputs {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.search-inputs-button {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;;
  gap:20px
}

.search-and-info {
  margin-top: 50px;
  align-items: center;
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 100px;
}

.summoner-name-input {
  max-width: 250px;
  min-width: 250px;
}

.region-input {
  max-width: 250px;
  min-width: 250px;
}

.search-button {
  font-size: xxx-large;
  padding: 0 0 8px 2px;
  margin-left: 15px;
}

.container {
    display: flex;
    size: 100%;
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

@media only screen and (max-width: 1115px) {
  .search-container-card {
    max-width: 300px;
    max-height: 300px;
    padding: 30px;
    backdrop-filter: blur(5px);
    margin-bottom: 0;
  }

  .search-inputs {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .search-inputs-button {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;;
  }

  .summoner-name-input {
    max-width: 200px;
    min-width: 200px;
  }

  .region-input {
    max-width: 200px;
    min-width: 200px;
  }

  .search-button {
    font-size: xxx-large;
    padding: 0 0 8px 2px;
    margin-left: 15px;
    margin-top: 0px;
  }

  .search-and-info {
    margin-top: 50px;
    align-items: center;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 10px;
    margin-left: 75px;
  }

  .main-data-table {
    text-align: center;
    margin-top: 20px;
    width: 80%;
    margin-left: 60px;
  }
}

@media only screen and (max-width: 400px) {
  .search-inputs {
    display: flex;
    flex-direction: column;
    max-width: 200px;
  }
}
</style>
