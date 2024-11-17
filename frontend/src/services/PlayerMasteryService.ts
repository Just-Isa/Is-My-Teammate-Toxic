import { reactive, readonly } from "vue";
import { useUserService } from "./UserService";

const userService = useUserService();

export interface IChampionMastery {
    championLevel: number;
    totalMasteryPoints: number;
    chestGranted: boolean;
    championPoints: number;
    championPointsSinceLastLevel: number;
    playerId: string | null;
    championPointsUntilNextLevel: number;
    tokensEarned: number;
    championId: number;
    lastPlayTime: number;
}

export interface IPlayerMasteryState {
    playerMastery: IChampionMastery[];
    totalMasteryPoints: number;
    errorMessage: string;
    finishedGettingMasteries: boolean;
}

const playerMasteryState: IPlayerMasteryState = reactive({
    playerMastery: [],
    totalMasteryPoints: 0,
    errorMessage: "",
    finishedGettingMasteries: false
});

/**
 * Initializes the playerMasteryState
 */
function getPlayerMastery() {
    try {
        const DEST = "/api/lol/playerMastery/" + userService.userState.user.accountId + "?region=" + userService.userState.userRegion;
        return fetch(DEST, {
            method: "GET",
        })
            .then((response) => {
                if (!response.ok) {
                    playerMasteryState.errorMessage = response.statusText;
                    return;
                }
                return response.json();
            })
            .then((jsondata) => {
                playerMasteryState.playerMastery = jsondata;
                playerMasteryState.totalMasteryPoints = playerMasteryState.playerMastery.reduce((acc: number, cur: IChampionMastery) => acc + cur.championPoints, 0);
                playerMasteryState.finishedGettingMasteries = true;
            })
            .catch((e) => {
                playerMasteryState.errorMessage = e;
            });
    } catch (error) {
        console.log(error);
        return;
    }
}

/**
 * Resets state
 */
function resetPlayerMasteryState() {
    playerMasteryState.playerMastery = [];
    playerMasteryState.errorMessage = "";
    playerMasteryState.finishedGettingMasteries = false;
}

export function usePlayerMasteryService() {
    return {
        playerMasteryState: readonly(playerMasteryState),
        getPlayerMastery,
        resetPlayerMasteryState
    }
}
