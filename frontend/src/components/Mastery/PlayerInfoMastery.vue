<template>
    <v-card
        class="player-info-container mx-auto"
        max-width="344"
        min-width="200"
        variant="outlined"
        v-if="userService.userState.user.puuid"
    >
        <v-card-item>
            <div :class="[theme.global.name.value == 'light' ? 'dark-text-class' : 'light-text-class']" class="player-info-card text-center">
                <div class="text-overline mb-1" color="white">
                    Player info
                </div>
                <div class="text-h6 mb-1">
                    {{ userService.userState.user.name + '#' + userService.userState.user.tag }}
                </div>
                <div class="text-h6 mb-1">
                    Level {{ userService.userState.user.level }}
                </div>
                <div class="text-h6 mb-1">
                    <v-btn class=" mb-3" v-on:click="userService.generateQRCode()">Generate QR-Code</v-btn>
                    <img v-if="userService.userState.user.qrCode" :src="userService.userState.user.qrCode" alt="QR Code">
                </div>
            </div>
        </v-card-item>
    </v-card>
</template>

<script setup lang="ts">
import { useUserService } from '../../services/UserService';
import { useLolGameService } from '../../services/LolGameService';
import { useTheme } from 'vuetify/lib/framework.mjs';
import { ref } from 'vue';

const theme = useTheme()
const userService = useUserService();
const lolGameService = useLolGameService();

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
