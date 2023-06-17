export interface IUser {
    accountId: string,
    id : string,
    name: string,
    profileIconId: number,
    puuid: string,
    revisionDate: number,
    summonerLevel: number,
}

export class User implements IUser {
    accountId: string;
    id : string;
    name: string;
    profileIconId: number;
    puuid: string;
    revisionDate: number;
    summonerLevel: number;

    constructor(
        accountId: string,
        id : string,
        name: string,
        profileIconId: number,
        puuid: string,
        revisionDate: number,
        summonerLevel: number,
    ){
        this.accountId = accountId;
        this.id = id;
        this.name = name;
        this.profileIconId = profileIconId;
        this.puuid = puuid;
        this.revisionDate = revisionDate;
        this.summonerLevel = summonerLevel;
    }
}