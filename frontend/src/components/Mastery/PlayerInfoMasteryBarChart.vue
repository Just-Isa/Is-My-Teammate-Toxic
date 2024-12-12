<template>
    <svg id="chart-container" v-if="playerMasteryService.playerMasteryState.playerMastery"></svg>
</template>

<script setup lang="ts">
import { useUserService } from '../../services/UserService';
import { useTheme } from 'vuetify/lib/framework.mjs';
import { usePlayerMasteryService } from '@/services/PlayerMasteryService';
import { useLolChampsService } from '@/services/LolChampService';
import { onMounted } from 'vue';
import { IChampionMastery } from '@/services/PlayerMasteryService';

    const lolChampsService = useLolChampsService();
    const playerMasteryService = usePlayerMasteryService();
    const userService = useUserService();

    onMounted(async () => {
        console.log(playerMasteryService.playerMasteryState.playerMastery)
    })
</script>

<style>
</style>
