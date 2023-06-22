import { reactive, readonly } from "vue";
import { IUser, User } from "@/domain/IUser";

export interface IUserState {
    user: IUser,
    userRegion: string,
    errorMessage: string
}

const userState  = reactive<IUserState>({
   user: new User("", "", "", 0, "", 0, "", 0),
   userRegion: "",
   errorMessage: ""
});

const regionDict: {[code:string]:string} = {
    "EUW":"EUW1",
    "NA":"NA1",
    "EUN":"EUN1"
}

async function getUserDTO(username: string, region: string){
    const DEST = "/api/user/"+username+"?region="+region;
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
        userState.userRegion = regionDict[region] ? regionDict[region] : "";
    })
    .catch((e) => {
        userState.errorMessage = e;
    });
}

//number of games capped to 5 for now
function getTFTGames(amountOfGames: number) {

}


export function useUserService() {
    return { 
        userState: readonly(userState), 
        getUserDTO, 
        getTFTGames
    }
}