<template>
    <v-card
        class="player-info-container"
        variant="elevated"
        v-if="userService.userState.user.puuid"
    >
        <v-card-item>
            <div class="info-card-qr">
                <div :class="[theme.global.name.value == 'light' ? 'dark-text-class' : 'light-text-class']" class="player-info-card text-center">
                    <div class="text-overline mb-1" color="white">
                        Player info
                    </div>
                    <div class="text-h6 mb-1">
                        {{ userService.userState.user.name }}<!---->
                        <span style="color:teal">{{ '#' + userService.userState.user.tag }}</span> - Level <span style="color:teal">{{ userService.userState.user.level }}</span>
                    </div>
                    <div class="text-h6 mb-5">
                        <v-tooltip activator="parent" location="end">Above 1% is bad (keep toxic reasons in mind)</v-tooltip>
                        Toxicity in
                        <span style="color: lightblue; display: inline;">{{ Object.keys(lolGameService.gameState.gameDetails).length }}</span>
                        games:
                        <span style="color: lightcoral; display: inline;">{{ parseFloat(lolGameService.toxicityInMatches.value) }}%</span>
                    </div>
                    <div class="text-h6 mb-5" v-if="lolGameService.boughtAccount.value">
                        Probably a bought Account
                        {{ lolGameService.boughtAccount.value }}
                    </div>
                    <v-btn variant="outlined" v-on:click="userService.generateQRCode()">Generate QR-Code</v-btn>
                </div>
                <div class="qr-code-container">
                    <img v-if="userService.userState.user.qrCode" :src="userService.userState.user.qrCode" alt="QR Code" class="qr-code-image">
                </div>
            </div>
        </v-card-item>
    </v-card>
</template>

<script setup lang="ts">
import { useUserService } from '../services/UserService';
import { useLolGameService } from '../services/LolGameService';
import { useTheme } from 'vuetify/lib/framework.mjs';

const theme = useTheme()
const userService = useUserService();
const lolGameService = useLolGameService();

</script>

<style>

.info-card-qr {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

.player-info-container {
    max-height: 200px;
    max-width: 600px;
    min-width: 200px;
    min-width: 200px;
    margin: 25px 0 25px 0;
}

.dark-text-class {
    color: black;
}

.light-text-class {
    color: white;
}

.qr-code-container {
    display: inline-block;
    transform: translateY(-35px);
    max-width: 210px;
}

.qr-code-image {
    transform: scale(0.7);
}

@media only screen and (max-width: 1120px) {
    .player-info-container {
        max-height: 450px;
        max-width: 500px;
        min-width: 200px;
        min-width: 200px;
        width:250px;
    }

    .qr-code-container {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .info-card-qr {
        display: block;
    }

    .qr-code-container {
        transform: none;
    }

    .qr-code-image {
        transform: scale(0.8);
    }
  }
</style>
