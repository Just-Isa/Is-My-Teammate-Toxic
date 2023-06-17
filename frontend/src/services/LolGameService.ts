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
    getTimestampData(gameID, region);
    

    if (userService.userState) {
        const KEY = "?api_key=RGAPI-9657b1a9-d822-434a-a315-977997030281";
        const DEST = "https://"+region+".api.riotgames.com/lol/match/v5/matches/" + gameID + KEY;
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
        .then((response) => {
            var playerdata =  response.info.participants.filter((x : any) => x.puuid === userService.userState.user.puuid);
            if (response != undefined) {
                gameState.gameDetails[gameID] = new GameInfo(
                                                    new Date(response.info.gameCreation).toLocaleDateString(),
                                                    response.info.gameDuration / 60,
                                                    new RelevantPlayerInfo(
                                                        playerdata[0].baitPings,
                                                        15
                                                    )
                                                )
            } else {
                gameState.errorMessage = "Empty game in GameState!";
            }
        })
        .catch((e) => {
            gameState.errorMessage = e;
        });
    } else {
        console.log("No userstate given")
    }
}


export function useLolGameService() {
    return {
        getGame,
        gameState: readonly(gameState),
        amountOfGames: amountOfGames
    }
}
