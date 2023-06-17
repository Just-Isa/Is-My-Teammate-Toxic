<template>
  <h1>TFT</h1>
  <div class="tft">
    <img alt="Vue logo" src="../assets/tft.png" />
  </div>
  <input type="text" required v-model="inputName" placeholder="Summoner" style="text-align: center;"/> |
    <select v-model="selectedRegion" placeholder="NA">
      <option v-for="key, value in Object.entries(regions)" :selected="key[1] == 'EUW'" :value="key[0]">{{key[1]}}</option>
    </select> |
  <button v-on:click="getUserFromService">Search</button>
  <PlayerInfo />
  
  
  <div v-for="g in userService.userState.TFTGames">
    {{ g }}
  </div>
</template>

<script setup lang="ts">
import PlayerInfo from "@/components/PlayerInfo.vue";
import { useUserService } from "@/services/UserService";
import { ref } from "vue";

const userService = useUserService();

const inputName = ref("");
const selectedRegion = ref("");
const regions : {[code: string] : string;} = {"eun1":"EUN", "euw1":"EUW", "na1":"NA"};

async function getUserFromService() {
    if(inputName.value) {
      await userService.getUserDTO(inputName.value, selectedRegion.value)
      userService.getTFTGames(20);
      userService.getLolGames(20);
    } else {
        console.log("No Name given")
    }
}
</script>
