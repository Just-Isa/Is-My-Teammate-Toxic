<template>
    <v-card
        class="mx-auto"
        max-width="344"
        variant="outlined"
        v-if="userService.userState.user.puuid"
    >
        <v-card-item>
        <div v-bind:class="[theme.global.name.value == 'light' ? 'dark-text-class' : 'light-text-class' ]">
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
                    location="top right"
                  >Above 1.2 is bad</v-tooltip>
            toxicity in
                <div style="color: lightblue; display: inline;;">{{ Object.keys(lolGameService.gameState.gameDetails).length }}</div>
            games
                <div style="color: lightcoral; display: inline;">{{ lolGameService.toxicityInMatches.value }}</div>
            </div>
            <div class="text-h6 mb-1" v-if="lolGameService.boughtAccount.value == true">
                Probably a bought Account
                {{ lolGameService.boughtAccount.value }}
            </div>
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

</script>

<style>
.dark-text-class {
    color: black;
}

.light-text-class {
    color: white;
}
</style>
