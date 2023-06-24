import { IGetToxicitiyDTO } from "@/domain/ICalculation";
import { IGameInfo, RelevantPlayerInfo } from "@/domain/IGames";
import { computed, reactive, readonly, ref, watch } from "vue";
import { useUserService } from "./UserService";

const userService = useUserService();
const toxicityInMatches = computed(() => {
    var toxicity = 0;
    var toxicNameAmount = 0;
    Object.values(gameState.gameDetails).forEach(element => {
        if(element.relevantPlayerInfo && element.relevantPlayerInfo.toxicityDTO && element.relevantPlayerInfo.toxicityDTO.toxicityLevel) 
        {
            if (element.relevantPlayerInfo.toxicityDTO.toxicityValues.filter(x => x.toLowerCase().includes("toxicname")) && toxicNameAmount == 0) {
                toxicity += element.relevantPlayerInfo.toxicityDTO.toxicityLevel;
                toxicNameAmount += 1; 
            }
        } 
    });
    return (toxicity  / Object.keys(gameState.gameDetails).length).toFixed(2);
});

interface IGameState {
    gameDetails: {[gameid:string]: IGameInfo},
    errorMessage: string;
}

const gameState = reactive<IGameState>({
    gameDetails: {},
    errorMessage: ""
});

interface IMatchHistoryState {
   LolGames : string[],
   AramGames: string[],
   SoloDuoRankedGames: string[],
   FlexRankedGames: string[],
   NormalGames: string[],
   TFTGames : string[],
}

const matchHistoryState = reactive<IMatchHistoryState>({
    LolGames: [],
    AramGames: [],
    SoloDuoRankedGames: [],
    FlexRankedGames: [],
    NormalGames: [],
    TFTGames: []
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
        switch(jsondata.queueType) {
            case "ARAM":
                matchHistoryState.AramGames.push(gameID);
                break;
            case "TEAM_BUILDER_RANKED_SOLO":
                matchHistoryState.SoloDuoRankedGames.push(gameID);
                break;
            case "TEAM_BUILDER_DRAFT_UNRANKED_5X5":
                matchHistoryState.NormalGames.push(gameID);
                break;
            case "RANKED_FLEX_SR":
                matchHistoryState.FlexRankedGames.push(gameID);
                break;
            default:
                console.log("detected other game type: " + jsondata.queueType + "ignoring for calculation");
                break;
            }
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
    .then((jsondata : RelevantPlayerInfo) => {
        gameState.gameDetails[gameID].relevantPlayerInfo = jsondata;
    })
    .catch((e) => {
        gameState.errorMessage = e;
    });
}

//number of games capped to 5 for now
async function getMatchHistory() {
    const DEST = "/api/lol/matchhistory/"+userService.userState.user.name+"?region="+userService.userState.userRegion;
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
        matchHistoryState.LolGames = jsondata;
    })
    .catch((e) => {
        gameState.errorMessage = e;
    });
}

watch(() => matchHistoryState.LolGames.length, (newValue, oldValue) => {
    if (newValue > 10 ) {
        matchHistoryState.LolGames.slice(0, 10).forEach(game => {
          getGame(game);
        });
      } else if (newValue > 1 && newValue< 10) {
        matchHistoryState.LolGames.slice(0, matchHistoryState.LolGames.length).forEach(game => {
            getGame(game);
        });
      }
});

export function useLolGameService() {
    return {
        getGame,
        getRelevantPlayerInfo,
        getMatchHistory,
        toxicityInMatches,
        gameState: readonly(gameState),
        matchHistoryState: readonly(matchHistoryState)
    }
}
