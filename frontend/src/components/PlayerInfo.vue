<template>
    <br>
    <div v-if="userService.userState.user.puuid">
        <div style="font-size: large;">
            <br>
            Name -> {{ userService.userState.user.name }} 
            <br>
            Level -> {{ userService.userState.user.summonerLevel }} 
            <br>
            Region -> {{ userService.userState.userRegion }}
        </div>
    </div>
    <br>
</template>

<script setup lang="ts">
import { useUserService } from '@/services/UserService';

const userService = useUserService();

</script>
