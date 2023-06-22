enum LaneType {
    AFK,
    BOT,
    INVALID,
    JUNGLE,
    MID,
    NONE,
    TOP,
    UTILITY
}

export interface IRelevantPlayerInfo {
    champName: string,
    baitPings: number,
    kills: number,
    deaths: number,
    assists: number,
    lane: LaneType,
    win: boolean
}

export interface IGameInfo {
    dateOfGame: string,
    gameDuration: number,
    relevantPlayerInfo: IRelevantPlayerInfo
}

export class GameInfo implements IGameInfo {
    dateOfGame: string;
    gameDuration: number;
    relevantPlayerInfo: IRelevantPlayerInfo;

    constructor(
        dateOfGame: string,
        gameDuration: number,
        relevantPlayerInfo: IRelevantPlayerInfo
    ){
        this.dateOfGame = dateOfGame;
        this.gameDuration = gameDuration;
        this.relevantPlayerInfo = relevantPlayerInfo;
    }
}

export class RelevantPlayerInfo implements IRelevantPlayerInfo {
    champName: string;
    baitPings: number;
    kills: number;
    deaths: number;
    assists: number;
    lane: LaneType;
    win: boolean;
    
    constructor (
        champName: string,
        baitPings: number,
        kills: number,
        deaths: number,
        assists: number,
        lane: LaneType,
        win: boolean
    ) {
        this.champName = champName;
        this.baitPings = baitPings;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.lane = lane;
        this.win = win;
    }
}
