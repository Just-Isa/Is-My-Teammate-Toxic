<template>
    <button v-on:click="testButton">XDDD</button>
    <div class="container">
        <div class="heatmap">
            <div v-for="death in relevantGame?.toxicityDTO.deathsPost10minPre2min" style="color: white;">{{death}}</div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useLolGameService } from "@/services/LolGameService";
import { computed } from "vue";
const lolGameService = useLolGameService();

const props = defineProps({
    gameID: String
});

var relevantGame = computed(() => {
    if (props.gameID && lolGameService.gameState && lolGameService.gameState.gameDetails[props.gameID]) {
        return lolGameService.gameState.gameDetails[props.gameID].relevantPlayerInfo;
    }
});

function testButton() {
    console.log(relevantGame);
}

</script>

<style>

.container {
    display: flex;
    size: 100%;
    justify-content: center;
}

.heatmap {
    min-height: 749px;
    min-width: 743.5px;
    margin-top: 20px;
    display: inline-block;
    background-image: url('https://ddragon.leagueoflegends.com/cdn/10.18.1/img/map/map11.png');
    background-size: cover;
}
</style>
