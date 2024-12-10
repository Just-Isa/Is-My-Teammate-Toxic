<template>
  <Navigation class="navbar"></Navigation>
  <div v-show="!lolGameService.matchHistoryState.finishedGettingGames && loading " class="loading-animation">
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
          <div class="summoner-name-input">
            <v-text-field
              variant="solo-filled"
              type="text"
              required
              v-model="inputName"
              label="Summoner + #TAG"
              style="text-align: center;"
            />
          </div>
          <div class="region-input">
            <v-select
              variant="solo-filled"
              v-model="inputRegion"
              label="Region"
              :items="Object.values(regions)"
            ></v-select>
          </div>
        </div>
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

const route = useRoute();

var loading = false;

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
      loading = true;
      if(!inputName.value.includes("#")) {
        alert("Please make sure to include the #TAG");
        return;
      }
      lolGameService.resetPlayerInfo();
      lolGameService.resetGames();
      await userService.getUserDTO(inputName.value, regionFlipped[inputRegion.value])
      await lolChampService.getAllChamps();
      await lolGameService.getMatchHistory();
      await playerMasteryService.getPlayerMastery();
      loading = false;
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

.search-container-card {
  margin: 50px 50px 50px 50px;
  max-height: 200px;
  padding: 30px;
  max-width: 400px;
  min-width: 400px;
}

.search-and-info {
  align-items: center;
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.search-inputs {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 75%;
  max-width: 75%;
}

.search-inputs-button {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;;
  gap:20px
}

.summoner-name-input {
  min-width: 100%;
}

.region-input {
  min-width: 100%;
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

.loading-animation {
  position: absolute;
  top: 60%;
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
    justify-content: flex-start;
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
