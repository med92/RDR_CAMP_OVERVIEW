import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {PieChartData, PlayerLedgerOperation, PlayerStats, PrimeNGPeiChartModel} from "./model";
import {Observable} from "rxjs";
import {DropdownModule} from "primeng/dropdown";
import {ChartModule} from "primeng/chart";
import {FormsModule} from "@angular/forms";
import {RouterModule, RouterOutlet} from "@angular/router";
import {CommonModule} from "@angular/common";
import {Table, TableModule} from "primeng/table";
import {MultiSelectModule} from "primeng/multiselect";
import {ButtonModule} from "primeng/button";
import {InputMaskModule} from "primeng/inputmask";
import {InputSwitchModule} from "primeng/inputswitch";
import {InputTextModule} from "primeng/inputtext";
import {InputTextareaModule} from "primeng/inputtextarea";
import {InputNumberModule} from "primeng/inputnumber";
import {TabMenuModule} from "primeng/tabmenu";

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [
    DropdownModule,
    ChartModule,
    FormsModule,
    RouterOutlet,
    RouterModule,
    HttpClientModule,
    CommonModule,
    TableModule,
    MultiSelectModule,
    ButtonModule,
    InputMaskModule,
    InputSwitchModule,
    InputTextModule,
    InputTextareaModule,
    InputNumberModule,
    TabMenuModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'rdr-camp-accountant-gui';
  stats: PlayerStats = new PlayerStats([],[], [], 0, [], [], []) ;
  optionsDonation: any;
  optionsDelivery: any;
  optionsAllTime: any;
  listOfSalesDelivery: string[] = [] ;
  selectedSaleDelivery: string = "CURRENT SALE";
  dataDelivery: PrimeNGPeiChartModel = new PrimeNGPeiChartModel([],[]);
  listOfSalesDonation: string[] = [] ;
  selectedSaleDonation: string = "CURRENT SALE";
  dataDonation: PrimeNGPeiChartModel = new PrimeNGPeiChartModel([],[]);
  listOfSalesAllTime: string[] = [] ;
  dataAllTime: PrimeNGPeiChartModel = new PrimeNGPeiChartModel([],[]);
  loading: boolean = false;

  representatives = [
    { name: 'Amy Elsner', image: 'amyelsner.png' },
    { name: 'Anna Fali', image: 'annafali.png' },
    { name: 'Asiya Javayant', image: 'asiyajavayant.png' },
    { name: 'Bernardo Dominic', image: 'bernardodominic.png' },
    { name: 'Elwin Sharvill', image: 'elwinsharvill.png' },
    { name: 'Ioni Bowcher', image: 'ionibowcher.png' },
    { name: 'Ivan Magalhaes', image: 'ivanmagalhaes.png' },
    { name: 'Onyama Limba', image: 'onyamalimba.png' },
    { name: 'Stephen Shaw', image: 'stephenshaw.png' },
    { name: 'Xuxue Feng', image: 'xuxuefeng.png' }
  ] ;
  constructor(private readonly httpClient: HttpClient) {
  }

  ngOnInit() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    this.loadDonationDeliveryPieChart();
  }

  public loadDonationDeliveryPieChart(sale? : string){
    this.getContributions().subscribe({
      next: (ps : PlayerStats) => {
        this.stats = ps ;
        let dataDel = ps.playersDelivery ;
        this.listOfSalesDelivery = dataDel.map(p => p.salesName) ;
        this.populatePieCharDataFromRestServiceDelivery(dataDel.find((x) => { if(sale !== undefined) {  return x.salesName === sale }
          return x.salesName === "CURRENT SALE" })) ;

        let dataDon = ps.playerDonations ;
        this.listOfSalesDonation = dataDon.map(p => p.salesName) ;
        this.populatePieCharDataFromRestServiceDonation(dataDon.find((x) => { if(sale !== undefined) {  return x.salesName === sale }
          return x.salesName === "CURRENT SALE" })) ;

        let dataAllTime = ps.allTimeDonationContribution;
        this.listOfSalesAllTime = dataAllTime.map(p => p.salesName) ;
        this.populatePieCharDataFromRestServiceAllTime(dataAllTime.find((x) => { if(sale !== undefined) {  return x.salesName === sale }
          return x.salesName === "ALL TIME CONTRIBUTION" })) ;
      }
    });
  }

  public getContributions(): Observable<any>{
    return this.httpClient.get("http://localhost:8080/api/camp/accountant") ;
  }
  public populatePieCharDataFromRestServiceDonation(pieCharData? : PieChartData): void {
    let total = 0 ;
    if (pieCharData?.data !==  undefined && pieCharData?.data.length> 0){
      total  =  pieCharData?.data!.reduce((accumulator: number, input: number): number => accumulator + input);
      if(pieCharData?.labels !==  undefined){
        for(let i = 0; i< pieCharData.labels.length; i++){
          pieCharData.labels[i] = pieCharData.labels[i] + " " + this.round((pieCharData.data[i]/total)*100,1) + "%";
        }
      }
    }
    this.dataDonation.labels = pieCharData?.labels ;
    this.dataDonation.datasets= [] ;
    this.dataDonation.datasets.push ( {
      data: pieCharData!.data,
      backgroundColor: this.generateColors(pieCharData?.data?.length)
    } );
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    this.optionsDonation = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor,
          },
        },
      },
    };
  }
  public populatePieCharDataFromRestServiceDelivery(pieCharData? : PieChartData): void {
    let total = 0 ;
    if (pieCharData?.data !==  undefined && pieCharData?.data.length> 0){
      total  =  pieCharData?.data!.reduce((accumulator: number, input: number): number => accumulator + input);
      if(pieCharData?.labels !==  undefined){
        for(let i = 0; i< pieCharData.labels.length; i++){
          pieCharData.labels[i] = pieCharData.labels[i] + " " + this.round((pieCharData.data[i]/total)*100,1) + "%";
        }
      }
    }
    this.dataDelivery.labels = pieCharData?.labels ;
    this.dataDelivery.datasets= [] ;
    this.dataDelivery.datasets.push ( {
      data: pieCharData!.data,
      backgroundColor: this.generateColors(pieCharData?.data?.length)
    } );
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    this.optionsDelivery = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor,
          },
        },
      },
    };
  }

  public populatePieCharDataFromRestServiceAllTime(pieCharData? : PieChartData): void {
    let total = 0 ;
    if (pieCharData?.data !==  undefined && pieCharData?.data.length> 0){
      total  =  pieCharData?.data!.reduce((accumulator: number, input: number): number => accumulator + input);
      if(pieCharData?.labels !==  undefined){
        for(let i = 0; i< pieCharData.labels.length; i++){
          pieCharData.labels[i] = pieCharData.labels[i] + " " + this.round((pieCharData.data[i]/total)*100,1) + "%";
        }
      }
    }
    this.dataAllTime.labels = pieCharData?.labels ;
    this.dataAllTime.datasets= [] ;
    this.dataAllTime.datasets.push ( {
      data: pieCharData!.data,
      backgroundColor: this.generateColors(pieCharData?.data?.length)
    } );
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    this.optionsAllTime = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor,
          },
        },
      },
    };
  }
  public generateColors(i?: number): string[]{
    i == undefined ? i = 0 : i ;
    let s: string[] = [] ;
    for( let x = 0 ; x< i ; x ++ ){
      s.push(this.random_rgba());
    }
    return s ;
  }

  public random_rgba() : string{
    let o = Math.round, r = Math.random, s= 255 ;
    return "rgba(" + o(r()*s) + "," + o(r()*s) + "," + o(r()*s) + "," + r().toFixed(1) +")" ;
  }


  onChangeDonation(newValue: any) {
    this.populatePieCharDataFromRestServiceDonation(this.stats.playerDonations.find((x) => { if(newValue !== undefined) {  return x.salesName === newValue }
      return x.salesName === "CURRENT SALE" })) ;
  }
  onChangeDelivery(newValue: any) {
    this.populatePieCharDataFromRestServiceDelivery(this.stats.playersDelivery.find((x) => { if(newValue !== undefined) {  return x.salesName === newValue }
      return x.salesName === "CURRENT SALE" })) ;
  }
  clear(table: Table) {
    table.clear();
  }
   round(value : number, precision : number) {
    var multiplier = Math.pow(10, precision || 0);
    return Math.round(value * multiplier) / multiplier;
  }
}
