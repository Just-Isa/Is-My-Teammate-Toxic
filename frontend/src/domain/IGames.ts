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

interface IRelevantPlayerInfo {
    countBaitPings: number,
    ammountDeathsPre15Min: number,
}

export interface IGameInfo {
    gameCreation: string,
    gameDuration: number,
    playerInfo: RelevantPlayerInfo
}

export class GameInfo implements IGameInfo {
    gameCreation: string;
    gameDuration: number;
    playerInfo: RelevantPlayerInfo;

    constructor(
        gameCreation: string,
        gameDuration: number,
        playerInfo: RelevantPlayerInfo
    ){
        this.gameCreation = gameCreation;
        this.gameDuration = gameDuration;
        this.playerInfo = playerInfo;
    }
}

export class RelevantPlayerInfo implements IRelevantPlayerInfo {
    countBaitPings: number;
    ammountDeathsPre15Min: number;
    
    constructor (
        countBaitPings: number,
        ammountDeathsPre15Min: number,
    ) {
        this.countBaitPings = countBaitPings;
        this.ammountDeathsPre15Min = ammountDeathsPre15Min;
    }
}
