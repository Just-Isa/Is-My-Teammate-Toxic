<template>
  <div class="container" v-if="lolGameService.matchHistoryState.LolGames.length > 0">
    <v-card v-for="g in categorizedGames" :key="g.gameId" justify-center
      variant="flat"
      class="info-card"
      max-width="80%"
      min-width="80%"
       :style="getRowStyle(g)"
      >
      <template v-if="g.isComplete">
        <v-card-title>
          <div class="info-card-title">
            <v-row>
              <v-col cols="4">
                {{ gameType[g.gameDetails.relevantPlayerInfo.gameQueueType] }}
              </v-col>
              <v-col cols="4">
                <span>{{ formatDate(g.gameDetails.dateOfGame) }}</span>
              </v-col>
              <v-col cols="4">
                <span>{{ g.gameDetails.gameDuration }}</span>
              </v-col>
            </v-row>
          </div>
          <hr/>
        </v-card-title>
        <v-card-text>
          <div class="info-card-text">
            <v-row>
              <v-col cols="4">

              {{ g.gameDetails.relevantPlayerInfo.champName }}

              {{
                gameType[g.gameDetails.relevantPlayerInfo.gameQueueType] !== 'ARAM'
                  ? (g.gameDetails.relevantPlayerInfo.lane as unknown as string).toLowerCase()
                  : '---'
              }}
              </v-col>
              <v-col cols="4">
                {{ g.gameDetails.relevantPlayerInfo.kills }} /
                {{ g.gameDetails.relevantPlayerInfo.deaths }} /
                {{ g.gameDetails.relevantPlayerInfo.assists }}
              </v-col>
              <v-col cols="4">
                <p v-for="(value, index) in g.gameDetails.relevantPlayerInfo.toxicityDTO.toxicityValues" :key="index">
                  {{ value }}
                </p>
                <span
                  v-if="!g.gameDetails.relevantPlayerInfo.toxicityDTO.toxicityValues.length"
                  :class="g.gameDetails.relevantPlayerInfo.toxicityDTO.toxicityValues.length ? 'toxic' : 'not-toxic'"
                  >
                  No Toxicity found!
                </span>
              </v-col>
            </v-row>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn
              class="heatmap-button"
              variant="elevated"
              elevation="10"
              v-on:click="revealHeatmap(g.gameId)"
          >
              Deaths
          </v-btn>
        </v-card-actions>
    </template>
    <template v-else>
      {{ g.gameId }}
      <v-card-actions class="info-card-empty" v-on:click="lolGameService.getGame(g.gameId)">
        LOAD MORE
      </v-card-actions>
    </template>
  </v-card>
  </div>
</template>

<script setup lang="ts">
import { useLolGameService } from '@/services/LolGameService';
import { computed } from 'vue';
import { useTheme } from 'vuetify/lib/framework.mjs';

const theme = useTheme();
const lolGameService = useLolGameService();

const getRowStyle = (g: any) => {
  if (!g.gameDetails || g.gameDetails.relevantPlayerInfo === undefined) {
    return '';
  }
  if (theme.global.current.value.dark) {
    return g.gameDetails.relevantPlayerInfo.win
    ? { backgroundColor: '#54788e' } // Style for wins
    : { backgroundColor: '#8e545f' };  // Style for losses
  } else {
    return g.gameDetails.relevantPlayerInfo.win
    ? { backgroundColor: '#5a9fcf' } // Style for wins
    : { backgroundColor: '#c36470' };  // Style for losses
  }
};

const categorizedGames = computed(() => {
  return lolGameService.matchHistoryState.LolGames.map((gameId) => {
    const gameDetails = lolGameService.gameState.gameDetails[gameId];
    const isComplete =
      gameDetails &&
      gameDetails.relevantPlayerInfo &&
      gameDetails.relevantPlayerInfo.toxicityDTO;
    return { gameId, isComplete, gameDetails };
  });
});

const gameType: {[code: string] : string;} =
  {
    "TEAM_BUILDER_DRAFT_UNRANKED_5X5":"Normal",
    "TEAM_BUILDER_RANKED_SOLO":"Ranked Solo/Duo",
    "ARAM":"ARAM",
    "BOT_5X5_INTRO":"Bot game",
    "RANKED_FLEX_SR":"Ranked Flex",
    "CHERRY":"Arena",
    "QUICKPLAY_NORMAL":"Quickplay"
  }

const formatDate = (date: any): string => {
  const currentDate = new Date();
  const splitDateString = date.replace(" ", "").split("/");
  const gameDate = new Date(
      parseInt(splitDateString[2]),
      parseInt(splitDateString[1]), // Month is 0-indexed in JavaScript
      parseInt(splitDateString[0])
  );

  const timeDifference = currentDate.getTime() - gameDate.getTime();
  const daysDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
  if (daysDifference === 0) {
      // If the date is today, return 'today'
      return `today`;
  } else if (daysDifference <= 3) {
      // If the date is within the last 2 days, return "D days ago"
      return `${daysDifference} day${daysDifference > 1 ? 's' : ''} ago`;
  } else {
      // If the date is more than 2 days ago, return the full date in DD.MM.YY
      const day = gameDate.getDate().toString().padStart(2, '0');
      const month = (gameDate.getMonth() + 1).toString().padStart(2, '0');
      const year = gameDate.getFullYear().toString().slice(-2);
      return `${day}.${month}.${year}`;
  }
};

function revealHeatmap(gameID: string): void {
  const id = "heatmap-container-" + gameID;
  const heatmapContainerElement = document.getElementById(id);
  const darkerBackground = document.getElementById("dark-content-overlay");

  if (heatmapContainerElement && darkerBackground) {
    const heatmapDisplay = getComputedStyle(heatmapContainerElement).display;
    heatmapContainerElement.style.display = heatmapDisplay === "none" ? "" : "none";

    const darkerBackgroundDisplay = getComputedStyle(darkerBackground).display;
    darkerBackground.style.display = darkerBackgroundDisplay === "none" ? "" : "none";

    document.documentElement.style.overflow = "hidden";
    document.body.style.overflow = "hidden";
  }
}

</script>

<style>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.info-card-title {
  margin: 1% 0 0 1%;
  font-size: 20px;
}

.info-card-text {
  display:flex;
  gap: 34px;
  margin: 1% 0 0 1%;
  font-size: 18px;
  font-weight: bold;
}

.info-card-empty {
  cursor: pointer;
}

.heatmap-button {
  margin: 0 1% 1% auto;
}

.v-table__wrapper::-webkit-scrollbar {
  display: none;
}

.v-table__wrapper {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.not-toxic {
  color: greenyellow;
}

.toxic {
  color: red;
}

@media only screen and (max-width: 1200px) {
  .info-card {
    min-width: 100% !important;
    max-width: 100% !important;
  }
}

@media only screen and (max-width: 900px) {
  .info-card {
    min-width: 120% !important;
    max-width: 120% !important;
  }
}
</style>
