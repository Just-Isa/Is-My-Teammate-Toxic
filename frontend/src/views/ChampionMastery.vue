<template>
    <div class="complete-content-container" id="complete-content-container" style="display: none;"></div>
    <Navigation class="navbar"></Navigation>
    <div v-show="
      loadingService.loadingState.isLoading && clickedSearch"
      class="loading-animation"
      >
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
      <PlayerInfoMastery />
    </div>
    <div v-show="!loadingService.loadingState.isLoading"  v-if="playerMasteryService.playerMasteryState.playerMastery.length > 0">
      <div class="mastery-info-container">
        <div class="mastery-table-container">
          <MasteryTable />
        </div>
        <div class="mastery-charts">
          <div class="mastery-chart-container">
            <MasteryChart type="PieChart"/>
          </div>
          <div class="mastery-chart-container">
            <MasteryChart type="BarChart"/>
          </div>
          <div class="mastery-chart-container">
            <MasteryChart type="ColumnChart"/>
          </div>
        </div>
      </div>
    </div>
  </template>

  <script setup lang="ts">
  import searchweb from 'vue-material-design-icons/SearchWeb.vue';
  import { useLolChampsService } from '@/services/LolChampService';
  import { useLolGameService } from "@/services/LolGameService";
  import { useUserService } from "@/services/UserService";
  import Navigation from "../components/Navigation.vue";
  import MasteryTable from '../components/Mastery/MasteryTable.vue';
  import { ref, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  import { usePlayerMasteryService } from '@/services/PlayerMasteryService';
  import PlayerInfoMastery from '@/components/Mastery/PlayerInfoMastery.vue';
  import { useLoadingService } from '@/services/LoadingService';
  import MasteryChart from '@/components/Mastery/MasteryChart.vue';

  const lolGameService = useLolGameService();
  const userService = useUserService();
  const playerMasteryService = usePlayerMasteryService();
  const lolChampService = useLolChampsService();
  const route = useRoute();
  const loadingService = useLoadingService();

  const inputName = ref("");
  const inputRegion = ref("");
  const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};
  const regionFlipped: {[code: string] : string;} = {"EUN":"eun1", "EUW":"euw1", "NA":"na1"};

  var clickedSearch: boolean = false;

  onMounted(async () => {
    inputRegion.value = regions['euw1'];
    await lolChampService.getAllChamps();

    if (route.query.name && route.query.tag && route.query.region) {
      await setParams(route.query.name as string, route.query.tag as string, route.query.region as string);
      await getUserFromService();
    } else if (userService.userState.user.name && userService.userState.user.tag && userService.userState.userRegion) {
      inputName.value = userService.userState.user.name + "#" + userService.userState.user.tag;
      inputRegion.value = regions[userService.userState.userRegion.toLowerCase()];
    }
  });

  async function setParams(name: string, tag: string, region: string) {
    inputName.value = name + "#" + tag;
    inputRegion.value = (regions[(region as string).toLowerCase()]);
    return new Promise((resolve) => setTimeout(resolve, 1000))
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
  max-height: 200px;
  padding: 30px;
  max-width: 400px;
  min-width: 400px;
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
  margin-top: 20px;
  margin-bottom: 20px;
  align-items: center;
  display: flex;
  gap: 20px;
  flex-direction: row;
  justify-content: center;
}

.summoner-name-input {
  max-width: 250px;
  min-width: 250px;
}

.mastery-table-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

.mastery-charts {
  justify-content: space-evenly;
  margin-left: 5%;
  display: flex;
  gap: 10px;
}

.mastery-chart-container {
  border: 1px solid #363636;
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
  left: calc(50% - 125px);
  transform: translate(0, 50%) scale(3);
}

  @media only screen and (max-width: 800px) {
    .top-bar {
      display: flex;
      flex-direction: column;
      max-width: 200px;
    }
  }

  @media only screen and (max-width: 1600px) {
    .mastery-charts {
      justify-content: space-evenly;
      margin-left: 5%;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10px;
    }
  }

</style>
