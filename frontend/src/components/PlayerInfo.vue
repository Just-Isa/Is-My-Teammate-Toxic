<template>
    <v-card
        class="mx-auto"
        max-width="344"
        variant="outlined"
        v-if="userService.userState.user.puuid"
    >
        <v-card-item>
        <div>
            <div class="text-overline mb-1">
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
        </div>
        </v-card-item>
    </v-card>
</template>

<script setup lang="ts">
import { useUserService } from '@/services/UserService';
import { useLolGameService } from '@/services/LolGameService';

const userService = useUserService();
const lolGameService = useLolGameService();

</script>
