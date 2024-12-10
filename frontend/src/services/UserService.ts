import { reactive, readonly } from "vue";
import { IUser, User } from "@/domain/IUser";

export interface IUserState {
    user: IUser,
    userRegion: string,
    errorMessage: string
}

const userState = reactive<IUserState>({
    user: new User("", "", "", "", 0, "", 0, "", 0, ""),
    userRegion: "",
    errorMessage: "",
});

function generateQRCode() {
    const DEST = "api/qr/generate/" + userState.user.name + "?region=" + userState.userRegion + "&summonerTag=" + userState.user.tag;
    return fetch(DEST, {
        method: "GET",
    })
        .then((response) => {
            if (!response.ok) {
                userState.user.qrCode = "";
                console.log("Error generating QR code:", response.statusText);
            }
            return response.text(); // Assuming the response is text (base64 string)
        })
        .then((base64Image) => {
            userState.user.qrCode = `data:image/png;base64, ${base64Image}`;
        })
        .catch((error) => {
            userState.user.qrCode = "";
            console.error('Error generating QR code:', error);
        });
}

async function getUserDTO(username: string, region: string) {
    const DEST = "/api/user/" + username.replace('#', '-') + "?region=" + region;
    return fetch(DEST, {
        method: "GET",
    })
        .then((response) => {
            if (!response.ok) {
                userState.errorMessage = response.statusText;
                throw new Error("No User");
            }
            return response.json();
        })
        .then((jsondata) => {
            userState.user = jsondata;
            return jsondata.leagueShard;
        })
        .then((leagueShard) => {
            userState.userRegion = leagueShard;
        })
        .catch((e) => {
            emptyState(e);
            alert("No summoner with this name exists!")
            return;
        });
}

function emptyState(e: string) {
    userState.user = new User("", "", "", "", 0, "", 0, "", 0, "");
    userState.userRegion = "";
    userState.errorMessage = e;
}

export function useUserService() {
    return {
        userState: readonly(userState),
        getUserDTO,
        generateQRCode
    }
}
