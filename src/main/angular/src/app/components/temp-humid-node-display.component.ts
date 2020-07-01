import { Component, Input } from '@angular/core';
import { ComponentType, RawData } from '../models/node';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Label, Color } from 'ng2-charts';

@Component({
  selector: 'app-temp-humid-display',
  template: `
    <div class="row  rounded p-3 bg-blue">
      <!-- <h1
        style="text-align: center; margin: 5px 0; font-size: large; font-weight: bolder;"
      >
        Port {{ node?.pinNumber }}
      </h1> -->
      <div class="info">
        <img src="../../assets/temperature.png" width="80px" height="80px" />

        <div class="cell">
          <h5>Temperature</h5>
          <div>{{ (latestData?.data?.split('_'))[0] | number: '1.0-2' }}°C</div>
        </div>

        <div class="cell">
          <h5>Humidity</h5>
          <div>{{ (latestData?.data?.split('_'))[1] }}%</div>
        </div>
      </div>

      <div class="chart col-12 col-sm-9">
        <canvas
          baseChart
          [datasets]="lineChartData"
          [labels]="lineChartLabels"
          [options]="lineChartOptions"
          [colors]="lineChartColors"
          chartType="line"
        >
        </canvas>
      </div>
    </div>
  `,
  styles: [
    `
      .info {
        display: flex;
        flex-direction: column;
        flex: 1;
        justify-content: space-around;
        align-items: center;
      }
      .bg-blue {
        background-color: #ebf6ff;
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

      .chart {
        display: flex;
        justify-content: space-around;
        align-items: center;
      }
    `,
  ],
})
export class TempHumidNodeDisplayComponent {
  public lineChartLabels: Label[] = [];

  private humidityHistory: number[] = [];

  // tslint:disable-next-line:variable-name
  private _raw: RawData[] = [];

  public lineChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      xAxes: [{}],
      yAxes: [
        {
          id: 'y-axis-0',
          position: 'left',
          gridLines: {
            color: 'rgba(232,41,41,0.1)',
          },
          ticks: {
            fontColor: '#b00000',
            min: 20,
            max: 45,
          },
        },
        {
          id: 'y-axis-1',
          position: 'right',
          gridLines: {
            color: 'rgba(51,97,255,0.1)',
          },
          ticks: {
            fontColor: '#004cb0',
            min: 0,
            max: 100,
          },
        },
      ],
    },
  };

  public lineChartColors: Color[] = [
    // {
    //   // red
    //   backgroundColor: 'rgba(255,0,0,0.3)',
    //   borderColor: 'red',
    //   pointBackgroundColor: 'rgba(148,159,177,1)',
    //   pointBorderColor: '#fff',
    //   pointHoverBackgroundColor: '#fff',
    //   pointHoverBorderColor: 'rgba(148,159,177,0.8)',
    // },
    // {
    //   // grey
    //   backgroundColor: 'rgba(148,159,177,0.2)',
    //   borderColor: 'rgba(148,159,177,1)',
    //   pointBackgroundColor: 'rgba(148,159,177,1)',
    //   pointBorderColor: '#fff',
    //   pointHoverBackgroundColor: '#fff',
    //   pointHoverBorderColor: 'rgba(148,159,177,0.8)',
    // },
  ];

  private temperatureHistory: number[] = [];

  public lineChartData: ChartDataSets[] = [
    {
      data: this.temperatureHistory,
      label: 'Temperature',
    },
    {
      data: this.humidityHistory,
      label: 'Humidity',
      yAxisID: 'y-axis-1',
    },
  ];

  public get latestData(): RawData {
    return this.raw[0];
  }

  @Input()
  public set raw(data: RawData[]) {
    this._raw = data;
    this.temperatureHistory = this.raw
      .filter((v) => v.type === ComponentType.TEMP_HUMID)
      .map((v) => v.data.split('_')[0])
      // tslint:disable-next-line:radix
      .map(Number)
      .reverse();
    this.lineChartData[0].data = this.temperatureHistory;
    this.humidityHistory = this.raw
      .filter((v) => v.type === ComponentType.TEMP_HUMID)
      .map((v) => v.data.split('_')[1])
      // tslint:disable-next-line:radix
      .map(Number)
      .reverse();
    this.lineChartData[1].data = this.humidityHistory;

    this.lineChartLabels = this.raw
      ?.filter((v) => v.type === ComponentType.TEMP_HUMID)
      .reverse()
      .map((v) => v.createdAt)
      .map((time) => time.toDate())
      .map((v) => {
        return (
          v.getHours() +
          ':' +
          this.getTimeStr(v.getMinutes()) +
          ':' +
          this.getTimeStr(v.getSeconds())
        );
      });
  }

  private getTimeStr(num: number): string {
    if (num < 10) return '0' + num;
    else return num.toString();
  }

  public get raw(): RawData[] {
    return this._raw;
  }

  private getMax(): number {
    let max = 10;
    for (const i of this.raw) {
      if (Number(i.data) > max) {
        max = Number(i.data);
      }
    }
    return max;
  }
}
