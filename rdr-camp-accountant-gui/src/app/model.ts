
export interface IpieChartData{
  salesName: string;
  data?: number[];
  labels?: string[];

}
export class PieChartData implements IpieChartData{
  constructor(
    public salesName: string,
    public data?: number[],
    public labels?: string[]
  ) {
  }
}

export class PrimeNGPeiChartModel {
  constructor(
    public datasets?: DataSet[],
    public labels?: string[]
  ) {
  }
}
export class DataSet {
  constructor(public data?: number[], public backgroundColor?: string[]) {
  }
}

export interface IPlayerStats{
   playerDonations : PieChartData[];
   playersDelivery : PieChartData[] ;
   allTimeDonationContribution : PieChartData[];
   currentSaleStatus : number ;
   userLedgerOperations : PlayerLedgerOperation[];
   playerTotalLedgerContributions : PlayerTotalLedgerContribution[] ;
   campChestInteractions: CampChestInteractions[];
}
export class PlayerStats implements IPlayerStats{
  constructor(public playerDonations : PieChartData[],
              public playersDelivery : PieChartData[],
              public allTimeDonationContribution : PieChartData[] ,
              public currentSaleStatus : number,
              public userLedgerOperations: PlayerLedgerOperation[],
              public playerTotalLedgerContributions: PlayerTotalLedgerContribution[],
              public campChestInteractions: CampChestInteractions[]) {
  }


}
export interface IPlayerLedgerOperation{
  user : string ;
  Amount : number ;
  transaction : string ;
  transactionDate : Date ;
}

export class PlayerLedgerOperation implements IPlayerLedgerOperation{
  constructor(public user: string, public Amount: number, public transaction: string , public  transactionDate: Date) {
  }
}

export interface IPlayerTotalLedgerContribution{
  player : string ;
  ledgerContribution : number;
  taxContribution : number ;
}

export class PlayerTotalLedgerContribution implements IPlayerTotalLedgerContribution{
  constructor(public player: string, public ledgerContribution: number,public taxContribution: number) {
  }
}

export interface ICampChestInteractions{
  player : string ;
  actionValue : number ;
  actions : string ;
  item : string ;
  timestamp : Date ;
}

export class CampChestInteractions implements ICampChestInteractions{
  constructor(public actionValue: number,public actions: string,public item: string,public player: string,public timestamp: Date) {
  }
}


