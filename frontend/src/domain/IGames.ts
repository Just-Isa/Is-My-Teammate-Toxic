import { IGetToxicitiyDTO } from "./ICalculation";

export enum LaneType {
    AFK,
    BOT,
    INVALID,
    JUNGLE,
    MID,
    NONE,
    TOP,
    UTILITY,
    TROLL,
    NOLANE
}

export enum TeamType {
    SUBTEAM,
    BLUE,
    RED,
    AI
}

export enum GameQueueType {
    CUSTOM,
    CHERRY,
    NORMAL_3X3_BLIND_PICK_OLD,
    NORMAL_5X5_BLIND_PICK_OLD,
    NORMAL_5X5_DRAFT,
    RANKED_SOLO_5X5,
    RANKED_PREMADE_5X5,
    RANKED_PREMADE_3X3,
    RANKED_TEAM_3X3,
    RANKED_TEAM_5X5,
    ODIN_5X5_BLIND,
    ODIN_5X5_DRAFT,
    BOT_5X5,
    BOT_ODIN_5X5,
    BOT_5X5_INTRO_OLD,
    BOT_5X5_BEGINNER_OLD,
    BOT_5X5_INTERMEDIATE_OLD,
    BOT_3X3_BEGINNER_OLD,
    GROUP_FINDER_5X5,
    ARAM_5X5_OLD,
    ONEFORALL_5X5,
    FIRSTBLOOD_1X1,
    FIRSTBLOOD_2X2,
    HEXAKILL_6X6_SR,
    URF_5X5,
    ONE_FOR_ALL_MIRROR,
    BOT_URF_5X5,
    NIGHTMARE_BOT_5X5_RANK1,
    NIGHTMARE_BOT_5X5_RANK2,
    NIGHTMARE_BOT_5X5_RANK5,
    ASCENSION_5X5,
    HEXAKILL,
    BILGEWATER_ARAM_5X5,
    KING_PORO_5X5,
    COUNTER_PICK,
    BILGEWATER_5X5,
    NEXUS_SIEGE_OLD,
    DEFINITELY_NOT_DOMINION_5X5,
    ALL_RANDOM_URF,
    SNOW_BATTLE_ARURF,
    OVERCHARGE,
    ARAM_5X5,
    TEAM_BUILDER_DRAFT_UNRANKED_5X5,
    TEAM_BUILDER_DRAFT_RANKED_5X5,
    TEAM_BUILDER_RANKED_SOLO,
    NORMAL_5V5_BLIND_PICK,
    RANKED_FLEX_SR,
    ARAM,
    NORMAL_3X3_BLIND_PICK,
    RANKED_FLEX_TT,
    ASSASSINATE_5X5,
    DARKSTAR_3X3,
    CLASH,
    BOT_3X3_INTERMEDIATE,
    BOT_3X3_INTRO,
    BOT_3X3_BEGINNER,
    BOT_5X5_INTRO,
    BOT_5X5_BEGINNER,
    BOT_5X5_INTERMEDIATE,
    NEXUS_SIEGE,
    NIGHTMARE_BOT_5X5_VOTE,
    NIGHTMARE_BOT_5X5,
    INVASION_NORMAL,
    INVASION_ONSLAUGHT,
    NEXUS_BLITZ,
    ODYSSEY_INTRO,
    ODYSSEY_CADET,
    ODYSSEY_CREWMEMBER,
    ODYSSEY_CAPTAIN,
    ODYSSEY_ONSLAUGHT,
    TUTORIAL_MODULE_1,
    TUTORIAL_MODULE_2,
    TUTORIAL_MODULE_3,
    TEAMFIGHT_TACTICS,
    TEAMFIGHT_TACTICS_RANKED,
    TEAMFIGHT_TACTICS_TUTORIAL,
    TEAMFIGHT_TACTICS_SIMULATION,
    TEAMFIGHT_TACTICS_HYPER_ROLL,
    TEAMFIGHT_TACTICS_HYPER_ROLL_1V0,
    TEAMFIGHT_TACTICS_DOUBLE_UP_2V0,
    TEAMFIGHT_TACTICS_DOUBLE_UP_4V0,
    TEAMFIGHT_TACTICS_DOUBLE_UP,
    TEAMFIGHT_TACTICS_DOUBLE_UP_1V7_BOTS,
    TEAMFIGHT_TACTICS_DOUBLE_UP_WORKSHOP,
    TEAMFIGHT_TACTICS_FORTUNE_FAVOR
}

export interface IRelevantPlayerInfo {
    champName: string,
    baitPings: number,
    kills: number,
    deaths: number,
    assists: number,
    lane: LaneType,
    team: TeamType,
    win: boolean,
    gameQueueType: GameQueueType
    toxicityDTO: IGetToxicitiyDTO
}

export interface IGameInfo {
    dateOfGame: string,
    gameDuration: number,
    gameType: GameQueueType,
    relevantPlayerInfo: IRelevantPlayerInfo
}

export interface GameListByTypes {
    NormalGames: string[],
    AramGames: string[],
    RankedGames: string[]
}

export class GameInfo implements IGameInfo {
    dateOfGame: string;
    gameDuration: number;
    gameType: GameQueueType;
    relevantPlayerInfo: IRelevantPlayerInfo;

    constructor(
        dateOfGame: string,
        gameDuration: number,
        gameType: GameQueueType,
        relevantPlayerInfo: IRelevantPlayerInfo
    ){
        this.dateOfGame = dateOfGame;
        this.gameDuration = gameDuration;
        this.gameType = gameType;
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
    team: TeamType;
    win: boolean;
    gameQueueType: GameQueueType;
    toxicityDTO: IGetToxicitiyDTO;
    
    constructor (
        champName: string,
        baitPings: number,
        kills: number,
        deaths: number,
        assists: number,
        lane: LaneType,
        team: TeamType,
        win: boolean,
        gameQueueType: GameQueueType,
        toxicityDTO: IGetToxicitiyDTO

    ) {
        this.champName = champName;
        this.baitPings = baitPings;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.lane = lane;
        this.team  = team;
        this.win = win;
        this.gameQueueType = gameQueueType;
        this.toxicityDTO = toxicityDTO;
    }
}
