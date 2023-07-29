import { IGameInfo, RelevantPlayerInfo } from "@/domain/IGames";
import { computed, reactive, readonly, ref, watch } from "vue";
import { useUserService } from "./UserService";


interface IGameState {
    gameDetails: {[gameid:string]: IGameInfo},
    errorMessage: string;
}

const userService = useUserService();

const gameState = reactive<IGameState>({
    gameDetails: {},
    errorMessage: ""
});

interface IMatchHistoryState {
   LolGames : string[],
   AramGames: string[],
   ArenaGames: string[],
   SoloDuoRankedGames: string[],
   FlexRankedGames: string[],
   NormalGames: string[],
   BotGames: string[],
   TFTGames : string[],
}

const matchHistoryState = reactive<IMatchHistoryState>({
    LolGames: [],
    AramGames: [],
    ArenaGames: [],
    SoloDuoRankedGames: [],
    FlexRankedGames: [],
    NormalGames: [],
    BotGames: [],
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
            case "BOT_5X5_INTRO":
                matchHistoryState.BotGames.push(gameID);
                break;
            case "CHERRY":
                matchHistoryState.ArenaGames.push(gameID);
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

function resetPlayerInfo() {
    gameState.gameDetails = {};
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
        console.log(gameState.gameDetails[gameID].relevantPlayerInfo)
    })
    .catch((e) => {
        gameState.errorMessage = e;
    });
}

//number of games capped to 5 for now
async function getMatchHistory() {
    try {
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
    } catch (error) {
        console.log(error);
        return;
    }
}


watch(() => matchHistoryState.LolGames, (newValue, oldValue) => {
    if (newValue.length > 20 ) {
        matchHistoryState.LolGames.slice(0, 20).forEach(game => {
          getGame(game);
        });
      } else if (newValue.length > 1 && newValue.length < 20) {
        matchHistoryState.LolGames.slice(0, matchHistoryState.LolGames.length).forEach(game => {
            getGame(game);
        });
      }
});

// COMPUTEDS
const boughtAccount = computed(() => {
    return matchHistoryState.BotGames.length >= 10;
});

const toxicityInMatches = computed(() => {
    var toxicity = 0;
    var toxicNameAmount = 0;
    Object.values(gameState.gameDetails).forEach(element => {
        if(element.relevantPlayerInfo && element.relevantPlayerInfo.toxicityDTO && element.relevantPlayerInfo.toxicityDTO.toxicityLevel) 
        {
            if ( toxicNameAmount == 0 && element.relevantPlayerInfo.toxicityDTO.toxicityValues.filter(x => x.toLowerCase().includes("toxicname")) ) {
                toxicity += element.relevantPlayerInfo.toxicityDTO.toxicityLevel;
                toxicNameAmount += 1; 
            }
        } 
    });
    return (toxicity  / Object.keys(gameState.gameDetails).length).toFixed(2);
});

// EXPORT
export function useLolGameService() {
    return {
        getRelevantPlayerInfo,
        getMatchHistory,
        resetPlayerInfo,
        getGame,
        matchHistoryState: readonly(matchHistoryState),
        gameState: readonly(gameState),
        toxicityInMatches,
        boughtAccount
    }
}