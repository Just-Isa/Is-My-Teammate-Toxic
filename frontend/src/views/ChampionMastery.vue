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
        <div class="radio-input">
          <label>
            <input value="barchart" name="value-radio" id="barchart" type="radio" @click="setFocus('barchart')" selected="true"/>
            <span>Bar</span>
          </label>
          <label>
            <input value="piechart" name="value-radio" id="piechart" type="radio" @click="setFocus('piechart')"/>
            <span>Pie</span>
          </label>
          <label>
            <input value="columnchart" name="value-radio" id="columnchart" type="radio" @click="setFocus('columnchart')"/>
            <span>Column</span>
          </label>
          <span class="selection"></span>
        </div>
        <div class="mastery-chart-container" v-show="focusedInput === 'piechart'">
          <MasteryChart type="PieChart"/>
        </div>
        <div class="mastery-chart-container" v-show="focusedInput === 'barchart'">
          <MasteryChart type="BarChart"/>
        </div>
        <div class="mastery-chart-container" v-show="focusedInput === 'columnchart'">
          <MasteryChart type="ColumnChart"/>
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
  var focusedInput = ref("barchart");

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

  function setFocus(id: string) {
    focusedInput.value = id;
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

.mastery-chart-container {
  display: flex;
  align-items: center;
  justify-content: center;
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

.mastery-info-container {
  display:flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

/* From Uiverse.io by MikeeMikee */
.radio-input input {
  display: none;
}

.radio-input {
  --container_width: 250px;
  position: relative;
  display: flex;
  align-items: center;
  border-radius: 10px;
  background-color: #212121;
  color: #fff;
  width: var(--container_width);
  overflow: hidden;
  border: 2px solid #fff;
}

.radio-input label {
  width: 100%;
  padding: 10px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1;
  font-weight: 600;
  letter-spacing: -1px;
  font-size: 14px;
}

.selection {
  display: none;
  position: absolute;
  height: 100%;
  width: calc(var(--container_width) / 3);
  z-index: 0;
  left: 0;
  top: 0;
  transition: 0.15s ease;
}

.radio-input label:has(input:checked) {
  color: #000;
}

.radio-input label:has(input:checked) ~ .selection {
  background-color: #683cb4;
  display: inline-block;
}

.radio-input label:nth-child(1):has(input:checked) ~ .selection {
  transform: translateX(calc(var(--container_width) * 0 / 3));
}

.radio-input label:nth-child(2):has(input:checked) ~ .selection {
  transform: translateX(calc(var(--container_width) * 1 / 3));
}

.radio-input label:nth-child(3):has(input:checked) ~ .selection {
  transform: translateX(calc(var(--container_width) * 2 / 3));
}


  @media only screen and (max-width: 800px) {
    .top-bar {
      display: flex;
      flex-direction: column;
      max-width: 200px;
    }
  }

  @media only screen and (max-width: 1600px) {
  }

</style>
