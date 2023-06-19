import { GameInfo, IGameInfo, RelevantPlayerInfo } from "@/domain/IGames";
import { computed, reactive, readonly, ref, watch } from "vue";
import { useUserService } from "./UserService";
import { getTimestampData } from "./PlayerInfoService";

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

async function getGame(gameID: string, region: string) {
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
        console.log(gameState.gameDetails);
    })
    .catch((e) => {
        gameState.errorMessage = e;
    });
}


export function useLolGameService() {
    return {
        getGame,
        gameState: readonly(gameState),
        amountOfGames: amountOfGames
    }
}
