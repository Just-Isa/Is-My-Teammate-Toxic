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
            toxicity in {{ Object.keys(lolGameService.gameState.gameDetails).length }} games  {{ lolGameService.toxicityInMatches }}
            </div>
            <div class="text-h6 mb-1" v-if="lolGameService.boughtAccount">
                Probably a bought Account
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

function switchTheme() {
    theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
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