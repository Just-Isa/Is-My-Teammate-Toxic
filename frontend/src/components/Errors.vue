<template>
    <div v-if="userService.userState.errorMessage">{{ userService.userState.errorMessage }}</div>
    <div v-if="lolGameService.gameState.errorMessage">{{ lolGameService.gameState.errorMessage }}</div>
</template>

<script setup lang="ts">
import { useLolGameService } from '@/services/LolGameService';
import { useUserService } from '@/services/UserService';

const lolGameService = useLolGameService();
const userService = useUserService();

</script>