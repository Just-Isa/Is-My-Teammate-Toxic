export interface IGetDeathPositionAndTimestamp {
    timestamp: number,
    position: {[coordinate: string]: number}
}

export interface IGetToxicitiyDTO {
    toxicityLevel: number,
    toxicityValues: string[],
    amountBaitPings: number,
    deathsPre10min: IGetDeathPositionAndTimestamp[],
    deathsPost10minPre2min: IGetDeathPositionAndTimestamp[],
    deaths2minBeforeEnd: IGetDeathPositionAndTimestamp[]
}