import {Component, Input} from '@angular/core';
import {RawData} from '../models/node';

@Component({
  selector: 'app-temp-humid-display',
  template: `
    <div class="row border rounded p-3 bg-blue">
      <!-- <h1
        style="text-align: center; margin: 5px 0; font-size: large; font-weight: bolder;"
      >
        Port {{ node?.pinNumber }}
      </h1> -->
      <div class="col-12 col-sm-3 d-flex justify-content-center">
        <img src="../../assets/temperature.png" width="80px" height="80px"/>
      </div>
      <div class="node-display col-12 col-sm-9">
        <div class="cell">
          <h5>Temperature</h5>
          <div>{{ latestData?.data?.split('_')[0] | number: '1.0-2' }}°C</div>
        </div>

        <div class="cell">
          <h5>Humidity</h5>
          <div>{{ latestData?.data?.split('_')[1] }}%</div>
        </div>
      </div>

<!--      <div>-->
<!--        <h5>History</h5>-->
<!--        <canvas-->
<!--          style="width: 100%; height: 300px;"-->
<!--          baseChart-->
<!--          [datasets]="lineChartData"-->
<!--          [labels]="lineChartLabels"-->
<!--          [colors]="lineChartColors"-->
<!--          [options]="lineChartOptions"-->
<!--          chartType="line"-->
<!--        >-->
<!--        </canvas>-->
<!--      </div>-->
    </div>
  `,
  styles: [
      `
      .bg-blue {
        background-color: #80deea;
      }

      .cell h5 {
        text-align: center;
        font-weight: normal;
      }

      .cell div {
        text-align: center;
        font-size: 30px;
        font-weight: bolder;
      }

      .node-display {
        display: flex;
        justify-content: space-around;
        align-items: center;
      }
    `,
  ],
})
export class TempHumidNodeDisplayComponent {

  // public get lineChartLabels(): Label[] {
  //   return this.raw?.map(v => v.createdAt)
  //     .map(time => time.toDate().getTime())
  //     .map(time => time.toString()) ?? [];
  // }
  //
  // // public lineChartOptions: (ChartOptions & { annotation: any }) = {
  // //   responsive: true,
  // // };
  // public lineChartColors: Color[] = [
  //   {
  //     borderColor: 'black',
  //     backgroundColor: 'rgba(255,0,0,0.3)',
  //   },
  // ];
  //
  // public get lineChartData(): ChartDataSets[] {
  //   return [{
  //     data: this.raw.filter(v => v.type === ComponentType.TEMP_HUMID)
  //       .map(v => Number(v.data.split('_')[0]))
  //       .filter(v => v !== undefined && v !== null)
  //       .reverse(),
  //     label: 'Temperature'
  //   }, {
  //     data: this.raw.filter(v => v.type === ComponentType.TEMP_HUMID)
  //       .map(v => Number(v.data.split('_')[1]))
  //       .filter(v => v !== undefined && v !== null)
  //       .reverse(),
  //     label: 'Humidity'
  //   }];
  // }
  //
  // public get lineChartOptions(): ChartOptions {
  //   return {
  //     responsive: true,
  //     scales: {
  //       yAxes: [{
  //         display: true,
  //         ticks: {
  //           max: this.getMax()
  //         }
  //       }]
  //     }
  //   };
  // }
  //
  // private getMax(): number {
  //   let max = 10;
  //   for (const i of this.raw) {
  //     if (Number(i.data) > max) {
  //       max = Number(i.data);
  //     }
  //   }
  //   return max;
  // }
  //
  public get latestData(): RawData {
    return this.raw[0];
  }

  @Input()
  public raw: RawData[] = [];
}
