import { IGameInfo } from "@/domain/IGames";
import { computed, reactive, readonly, ref, watch } from "vue";
import { useUserService } from "./UserService";

const userService = useUserService();
const amountOfGames = computed(() => userService.userState.LolGames.length);

interface IGameState {
    gameDetails: {[gameid:string]: IGameInfo},
    errorMessage: string;
}

const gameState = reactive<IGameState>({
    gameDetails: {},
    errorMessage: ""
});

async function getGame(gameID: string) {
    const DEST = "/api/lol/game/"+gameID+"?region="+userService.userState.userRegion;
    return fetch(DEST, {
        method: "GET",
      })
    .then((response) => {
        if (!response.ok) {
            gameState.errorMessage = response.statusText;
            return;
        }
        return response.json();
    })
    .then((jsondata) => {
        gameState.gameDetails[gameID] = jsondata;
    })
    .then(() => {
        getRelevantPlayerInfo(gameID);
    })
    .catch((e) => {
        gameState.errorMessage = e;
    });
}

async function getRelevantPlayerInfo(gameID: string) {
    const DEST = "/api/lol/game/"+gameID+"/timeline?region="+userService.userState.userRegion+"&summonerName="+userService.userState.user.name;
    return fetch(DEST, {
        method: "GET",
      })
    .then((response) => {
        if (!response.ok) {
            gameState.errorMessage = response.statusText;
            return;
        }
        return response.json();
    })
    .then((jsondata) => {
        gameState.gameDetails[gameID].relevantPlayerInfo = jsondata;
    })
    .catch((e) => {
        gameState.errorMessage = e;
    });
}

watch(() => userService.userState.LolGames.length, (newValue, oldValue) => {
    if (newValue > 5 ) {
        userService.userState.LolGames.slice(0, 5).forEach(game => {
          getGame(game);
        });
      } else if (newValue > 1 && newValue< 5) {
        userService.userState.LolGames.slice(0, userService.userState.LolGames.length).forEach(game => {
            getGame(game);
        });
      }
        userService.getTFTGames(20);
});

export function useLolGameService() {
    return {
        getGame,
        getRelevantPlayerInfo,
        gameState: readonly(gameState),
        amountOfGames: amountOfGames
    }
}
