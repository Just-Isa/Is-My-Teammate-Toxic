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
        </div>
    </div>
    <br>
</template>

<script setup lang="ts">
import { useUserService } from '@/services/UserService';

const userService = useUserService();

</script>
