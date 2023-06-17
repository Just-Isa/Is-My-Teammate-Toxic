import { reactive, readonly } from "vue";
import { IUser, User } from "@/domain/IUser";

export interface IUserState {
    user: IUser,
    userRegion: string,
    TFTGames: string[],
    LolGames: string[],
    errorMessage: string
}

const userState  = reactive<IUserState>({
   user: new User("", "", "", 0, "", 0, 0),
   userRegion: "",
   LolGames : [],
   TFTGames : [],
   errorMessage: ""
});

const regionDict: {[code:string]:string} = {
    "euw1":"europe",
    "na1":"americas",
    "eun1":"europe"
}

async function getUserDTO(username: string, region: string){
    const KEY = "?api_key=RGAPI-9657b1a9-d822-434a-a315-977997030281";
    const DEST = "https://"+region+".api.riotgames.com/lol/summoner/v4/summoners/by-name/" + username + KEY
    return fetch(DEST, {
        method: "GET",
      })
    .then((response) => {
        if (!response.ok) {
            userState.errorMessage = response.statusText;
            return;
        }
        return response.json();
    })
    .then((jsondata) => {
        userState.user = jsondata;
    })
    .then(() => {
        userState.userRegion = regionDict[region]
    })
    .catch((e) => {
        userState.errorMessage = e;
    });
}

//number of games capped to 5 for now
function getTFTGames(amountOfGames: number) {
    const KEY = "&api_key=RGAPI-9657b1a9-d822-434a-a315-977997030281";
    const DEST = "https://"+userState.userRegion+".api.riotgames.com/tft/match/v1/matches/by-puuid/" + 
        userState.user.puuid + "/ids?count="+ amountOfGames + KEY 
    return fetch(DEST, {
        method: "GET",
      })
    .then((response) => {
        if (!response.ok) {
            userState.errorMessage = response.statusText;
            return;
        }
        return response.json();
    })
    .then((jsondata) => {
        userState.TFTGames = jsondata;
    })
    .catch((e) => {
        userState.errorMessage = e;
    });
}

//number of games capped to 5 for now
async function getLolGames(amountOfGames: number) {
    
    const KEY = "&api_key=RGAPI-9657b1a9-d822-434a-a315-977997030281";
    const DEST = "https://"+userState.userRegion+".api.riotgames.com/lol/match/v5/matches/by-puuid/" + 
        userState.user.puuid + "/ids?count="+ amountOfGames + KEY 
    return fetch(DEST, {
        method: "GET",
      })
    .then((response) => {
        if (!response.ok) {
            userState.errorMessage = response.statusText;
            return;
        }
        return response.json();
    })
    .then((jsondata) => {
        userState.LolGames = jsondata;
    })
    .catch((e) => {
        userState.errorMessage = e;
    });
}


export function useUserService() {
    return { 
        userState: readonly(userState), 
        getUserDTO, 
        getTFTGames, 
        getLolGames
    }
}