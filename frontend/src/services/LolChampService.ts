import { reactive, readonly } from "vue";

export interface LolChamps {
    data: { [key: string]: { name: string } },
    errorMessage: string
}

const lolChampState = reactive<LolChamps>({
    data: {},
    errorMessage: ""
});

async function getAllChamps() {
    if (Object.keys(lolChampState.data).length > 0) {
        return;
    }
    try {
        const DEST = "/api/lol/champs";
        return fetch(DEST, {
            method: "GET",
        })
            .then((response) => {
                if (!response.ok) {
                    lolChampState.errorMessage = response.statusText;
                    return;
                }
                return response.json();
            })
            .then((jsondata) => {
                lolChampState.data = jsondata;
            })
            .catch((e) => {
                lolChampState.errorMessage = e;
            });
    } catch (error) {
        console.log(error);
        return;
    }
}

export function useLolChampsService() {
    return {
        lolChampState: readonly(lolChampState),
        getAllChamps
    }
}
