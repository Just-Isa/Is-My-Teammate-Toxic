<template>
    <v-card
        class="player-info-container mx-auto"
        max-width="344"
        min-width="200"
        variant="outlined"
        v-if="userService.userState.user.puuid"
    >
        <v-card-item>
        <div v-bind:class="[theme.global.name.value == 'light' ? 'dark-text-class' : 'light-text-class' ]" class="player-info-card text-center">
            <div class="text-overline mb-1" color="white">
            Player info
            </div>
            <div class="text-h6 mb-1">
                {{ userService.userState.user.name }}
            </div>
            <div class="text-h6 mb-1">
            Level {{ userService.userState.user.level }}
            </div>
            <div class="text-h6 mb-1">
                <v-tooltip
                    activator="parent"
                    location="end"
                  >Above 0.1 is bad (keep toxic reasons in mind)</v-tooltip>
            toxicity in
                <div style="color: lightblue; display: inline;;">{{ Object.keys(lolGameService.gameState.gameDetails).length }}</div>
            games
                <div style="color: lightcoral; display: inline;">{{ lolGameService.toxicityInMatches.value }}</div>
            </div>
            <div class="text-h6 mb-1" v-if="lolGameService.boughtAccount.value == true">
                Probably a bought Account
                {{ lolGameService.boughtAccount.value }}
            </div>
            <!--<div class="text-h6 mb-1 mt-3">
        <v-btn :variant="'elevated'" @click="loadFullLobby">Live Game</v-btn>
            </div>
            <div class="text-h6 mb-1 mt-4">
            <v-btn :variant="'elevated'" @click="loadStats">Stats...</v-btn>
            </div>-->
        </div>
        </v-card-item>
    </v-card>
</template>

<script setup lang="ts">
import { useUserService } from '@/services/UserService';
import { useLolGameService } from '@/services/LolGameService';
import { useTheme } from 'vuetify/lib/framework.mjs';

const theme = useTheme()


const userService = useUserService();
const lolGameService = useLolGameService();

function loadFullLobby() {
    console.log("TODO")
}

function loadStats() {
    console.log("TODO")
}

</script>

<style>
.player-info-container {
    margin: 25px 0 25px 0;
}
.dark-text-class {
    color: black;
}

.light-text-class {
    color: white;
}


@media only screen and (max-width: 600px) {
    .player-info-container {
        max-width: 200px;
        width:250px;
        left: 35px;
    }
  }
</style>
