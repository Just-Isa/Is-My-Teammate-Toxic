<template>
  <div class="complete-content-container" id="complete-content-container" style="display: none;"></div>
  <div v-if="lolGameService.matchHistoryState.LolGames.length > 0">
    <v-table fixed-header height="550px" class="main-data-table">
      <thead>
        <tr class="main-data-table-th text-center">
          <th class="text-center">Queue</th>
          <th class="text-center">Date</th>
          <th class="text-center">Duration</th>
          <th class="text-center">Champ</th>
          <th class="text-center">Lane</th>
          <th class="text-center">K/D/A</th>
          <th class="text-center">Toxicity</th>
          <th class="text-center">Death Heatmap</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="g in categorizedGames" :key="g.gameId" :style="getRowStyle(g)" >
          <template v-if="g.isComplete">
            <td>
              {{ gameType[g.gameDetails.relevantPlayerInfo.gameQueueType] }}
            </td>
            <td>
              {{ formatDate(g.gameDetails.dateOfGame) }}
            </td>
            <td>
              {{ g.gameDetails.gameDuration }}
            </td>
            <td>
              {{ g.gameDetails.relevantPlayerInfo.champName }}
            </td>
            <td>
              {{
                gameType[g.gameDetails.relevantPlayerInfo.gameQueueType] !== 'ARAM'
                  ? g.gameDetails.relevantPlayerInfo.lane
                  : '---'
              }}
            </td>
            <td>
              {{ g.gameDetails.relevantPlayerInfo.kills }} /
              {{ g.gameDetails.relevantPlayerInfo.deaths }} /
              {{ g.gameDetails.relevantPlayerInfo.assists }}
            </td>
            <td>
              <p v-for="(value, index) in g.gameDetails.relevantPlayerInfo.toxicityDTO.toxicityValues" :key="index">
                {{ value }}
              </p>
              <span
                v-if="!g.gameDetails.relevantPlayerInfo.toxicityDTO.toxicityValues.length"
                :class="g.gameDetails.relevantPlayerInfo.toxicityDTO.toxicityValues.length ? 'lose-circle' : 'win-circle'"
                >
                <v-tooltip activator="parent" location="end">No Toxicity found!</v-tooltip>
              </span>
            </td>
            <td>
              <v-btn
                icon
                variant="flat"
                class="heatmap-button"
                v-on:click="revealHeatmap(g.gameId)"
              >
                <deathlyHallows style="transform: scale(2.5); "/>
              </v-btn>
              <div class="mobile-info">Not for mobile yet</div>
            </td>
        </template>

        <!-- Show only "More" button for incomplete games -->
        <template v-else>
          <td colspan="9">
            <v-btn v-on:click="lolGameService.getGame(g.gameId)" variant="outlined">Load More</v-btn>
          </td>
        </template>
        </tr>
      </tbody>
    </v-table>
  </div>
</template>

<script setup lang="ts">
import deathlyHallows from 'vue-material-design-icons/DeathlyHallows.vue';
import { useLolGameService } from '@/services/LolGameService';
import { computed } from 'vue';

const lolGameService = useLolGameService();

const getRowStyle = (g: any) => {
  if (!g.gameDetails) {
    return '';
  }
  return g.gameDetails.relevantPlayerInfo.win
    ? { backgroundColor: '#54788e' } // Style for wins
    : { backgroundColor: '#8e545f' };  // Style for losses
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

function formatDate(date: string): string {
  const currentDate = new Date();
  const splitDateString = date.replace(" ", "").split("/");
  const gameDate = new Date(parseInt(splitDateString[2]), parseInt(splitDateString[1]), parseInt(splitDateString[0]));
  const timeDifference = Math.abs(currentDate.getTime() - gameDate.getTime());
  const daysDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
  return `${daysDifference} days ago`;
}

function revealHeatmap(gameID: string): void {
  // reveals the specific heatmap container which contains the component, see LolView
  const id = "heatmap-container-" + gameID;
  const heatmapContainerElement = document.getElementById(id);
  const darkerBackground = document.getElementById("complete-content-container");

  if (heatmapContainerElement && darkerBackground) {
    heatmapContainerElement.style.display = heatmapContainerElement.style.display === "none" ? "" : "none";
    darkerBackground.style.display = heatmapContainerElement.style.display === "" ? "" : "none";
  }
}

</script>

<style>

.v-table__wrapper::-webkit-scrollbar {
  display: none;
}

.v-table__wrapper {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.below50kMastery{
  color: lightgreen;
}

.below500kAbove10kMastery{
  color: lightpink
}

.below100kAbove50kMastery{
  color: lightskyblue;
}

.above500kMastery{
  color: red;
}

.mobile-info {
  display: none;
}

/* Table Headers */
th {
  font-size: 20px;
}

/* Win & Lose Circles */
.win-circle, .lose-circle {
  height: 25px;
  width: 25px;
  border-radius: 50%;
  display: inline-block;
}

.win-circle {
  background-color: greenyellow;
}

.lose-circle {
  background-color: red;
}

.heatmap-button {
  padding-bottom: 10px;
}

@media only screen and (max-width: 600px) {
    .main-data-table {
      max-width: 310px;
      height: 400px;
    }

    th {
      font-size: 18px;
    }

    td{
      font-size: 15px;
      max-width: 20px;
    }

    .v-table__wrapper {
      max-width: 310px;
      height: 300px;
      max-height: 400px;
    }

    .heatmap-button {
      display: none;
    }

    .mobile-info {
      display: inline;
    }
  }
</style>
