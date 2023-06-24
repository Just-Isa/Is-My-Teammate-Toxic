<template>
    <br>
    <div v-if="userService.userState.user.puuid">
        <div style="font-size: large;">
            <br>
            Name -> {{ userService.userState.user.name }} 
            <br>
            Level -> {{ userService.userState.user.level }} 
            <br>
            Region -> {{ userService.userState.user.platform }}
            <br>
            Average Toxicity in  {{ Object.keys(lolGameService.gameState.gameDetails).length }} games -> {{ lolGameService.toxicityInMatches }}
            <br>
        </div>
    </div>
    <br>
</template>

<script setup lang="ts">
import { useUserService } from '@/services/UserService';
import { useLolGameService } from '@/services/LolGameService';

const userService = useUserService();
const lolGameService = useLolGameService();

</script>
