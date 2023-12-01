export interface IUser {
    accountId: string,
    id : string,
    name: string,
    profileIconId: number,
    puuid: string,
    revisionDate: number,
    level: number,
    platform: string
}

export class User implements IUser {
    accountId: string;
    id : string;
    name: string;
    profileIconId: number;
    puuid: string;
    revisionDate: number;
    level: number;
    platform: string;

    constructor(
        accountId: string,
        id : string,
        name: string,
        tag: string,
        profileIconId: number,
        puuid: string,
        revisionDate: number,
        platform: string,
        level: number,
    ){
        this.accountId = accountId;
        this.id = id;
        this.name = name;
        this.profileIconId = profileIconId;
        this.puuid = puuid;
        this.revisionDate = revisionDate;
        this.level = level;
        this.platform = platform;
    }
}
