<template>
  <Navigation class="navbar"></Navigation>
  <div v-show="loadingService.loadingState.isLoading" class="loading-animation">
    <img src="../assets/loading-spin.svg">
  </div>
  <div
  v-for="g in lolGameService.matchHistoryState.LolGames"
  v-show="!loadingService.loadingState.isLoading"
  :key="g"
  >
    <!-- DEATH HEATMAP -->
    <div v-if="hasDeathData(g)">
        <DeathHeatmap :g="g"></DeathHeatmap>
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
  <div v-show="!loadingService.loadingState.isLoading">
    <v-row>
      <v-col cols="3"></v-col>
      <v-col cols="6">
        <LolGamesInfoList/>
      </v-col>
      <v-col cols="3"></v-col>
    </v-row>
  </div>
</template>

<script setup lang="ts">
import { useLolGameService } from "@/services/LolGameService";
import { useUserService } from "@/services/UserService";
import { useLolChampsService } from '@/services/LolChampService';
import { usePlayerMasteryService } from '@/services/PlayerMasteryService';
import { useLoadingService } from '@/services/LoadingService';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import LolGamesTable from '../components/LolGamesTable.vue';
import LolGamesInfoList from "@/components/LolGamesInfoList.vue";
import PlayerInfo from "../components/PlayerInfo.vue";
import Navigation from "../components/Navigation.vue";
import searchweb from 'vue-material-design-icons/SearchWeb.vue';
import DeathHeatmap from '../components/DeathHeatmap/DeathHeatmap.vue';

const lolGameService = useLolGameService();
const userService = useUserService();
const lolChampService = useLolChampsService();
const playerMasteryService = usePlayerMasteryService();
const loadingService = useLoadingService();

const inputName = ref("");
const inputRegion = ref("");
const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};
const regionFlipped: {[code: string] : string;} = {"EUN":"eun1", "EUW":"euw1", "NA":"na1"};

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

function hasDeathData(g: any) {
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

      try {
        loadingService.loadingStarted();
        lolGameService.resetPlayerInfo();
        lolGameService.resetGames();
        await userService.getUserDTO(inputName.value, regionFlipped[inputRegion.value])
        await lolChampService.getAllChamps();
        await lolGameService.getMatchHistory();
        await playerMasteryService.getPlayerMastery();
      } catch {
        console.log("This should never happen!")
      } finally {
        loadingService.loadingFinished();
      }

    } else {
      alert("Check name and Region please")
    }
}

</script>

<style>

.search-container-card {
  max-height: 200px;
  padding: 30px;
  max-width: 400px;
  min-width: 400px;
}

.search-and-info {
  margin: 2% 0 2% 0;
  align-items: center;
  display: flex;
  gap: 20px;
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

}

@media only screen and (max-width: 400px) {
  .search-inputs {
    display: flex;
    flex-direction: column;
    max-width: 200px;
  }
}
</style>
