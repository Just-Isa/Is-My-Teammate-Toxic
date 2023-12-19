<template>
    <div class="complete-content-container" id="complete-content-container" style="display: none;"></div>
    <Navigation class="navbar"></Navigation>
    <div v-show="!playerMasteryService.playerMasteryState.finishedGettingMasteries && clickedSearch && !lolGameService.matchHistoryState.finishedGettingGames" class="loading-animation">
      <img src="../assets/loading-spin.svg">
    </div>
    <div class="top-bar">
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
      <div class="search-button-col">
        <v-btn
          icon
          size="large"
          theme="dark"
          variant="elevated"
          class="search-button"
          v-on:click="getUserFromService"
        >
          <searchweb />
        </v-btn>
      </div>
    </div>
    <div v-show="playerMasteryService.playerMasteryState.finishedGettingMasteries && playerMasteryService.playerMasteryState.finishedGettingMasteries">
      <PlayerInfoMastery />
      <v-row>
        <v-col cols="1"></v-col>
        <v-col cols="11">
        <MasteryTable />
        </v-col>
      </v-row>
      <Footer/>
    </div>
  </template>

  <script setup lang="ts">
  import searchweb from 'vue-material-design-icons/SearchWeb.vue';
  import { useLolChampsService } from '@/services/LolChampService';
  import { useLolGameService } from "@/services/LolGameService";
  import { useUserService } from "@/services/UserService";
  import Navigation from "../components/Navigation.vue";
  import MasteryTable from '../components/Mastery/MasteryTable.vue';
  import Footer from "@/components/Footer.vue";
  import { ref, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  import { usePlayerMasteryService } from '@/services/PlayerMasteryService';
  import PlayerInfoMastery from '@/components/Mastery/PlayerInfoMastery.vue';

  const lolGameService = useLolGameService();
  const userService = useUserService();
  const playerMasteryService = usePlayerMasteryService();
  const lolChampService = useLolChampsService();

  const inputName = ref("");
  const inputRegion = ref("");
  const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};
  const regionFlipped: {[code: string] : string;} = {"EUN":"eun1", "EUW":"euw1", "NA":"na1"};

  var clickedSearch: boolean = false;
  const route = useRoute();

  onMounted(async () => {
    if (route.query.name && route.query.tag && route.query.region) {
      await setParams(route.query.name as string, route.query.tag as string, route.query.region as string);
      playerMasteryService.resetPlayerMasteryState();
      playerMasteryService.getPlayerMastery();
      await lolChampService.getAllChamps();
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
        lolGameService.resetPlayerInfo();
        lolGameService.resetGames();
        playerMasteryService.resetPlayerMasteryState();
        clickedSearch = true;
        await userService.getUserDTO(inputName.value, regionFlipped[inputRegion.value])
        await playerMasteryService.getPlayerMastery();
        await lolGameService.getMatchHistory();
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
        max-width: 280px;
      }

      .main-data-table {
        text-align: center;
        margin-top: 20px;
        width: 80%;
        margin-left: 60px;
      }
    }


    @media only screen and (max-width: 400px) {
      .top-bar {
        display: flex;
        flex-direction: column;
        max-width: 200px;
      }
    }
  </style>
