import { useUserService } from "./UserService";


export function getTimestampData(gameID: string, region: string) {
    const KEY = "?api_key=RGAPI-9657b1a9-d822-434a-a315-977997030281";
    const DEST = "https://"+region+".api.riotgames.com/lol/match/v5/matches/" + gameID + "/timeline/" + KEY;
    return fetch(DEST, {
        method: "GET",
        })
    .then((response) => {
        if (!response.ok) {
            return;
        }
        return response.json();
    })
    .then((response) => {
        console.log(response);
    })
    .catch((e) => {
    });
}