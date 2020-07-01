import {Component, Input} from '@angular/core';
import {ComponentType, RawData} from '../models/node';
import {ChartDataSets} from 'chart.js';
import {Label} from 'ng2-charts';

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

        <canvas
          baseChart
          [datasets]="lineChartData"
          [labels]="lineChartLabels"
          chartType="line"
        >
        </canvas>
      </div>
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

  public lineChartLabels: Label[] = [];

  // public lineChartColors: Color[] = [
  //   {
  //     borderColor: 'black',
  //     backgroundColor: 'rgba(255,0,0,0.3)',
  //   },
  // ];
  private humidityHistory: number[] = [];

  // tslint:disable-next-line:variable-name
  private _raw: RawData[] = [];

  private temperatureHistory: number[] = [];

  public lineChartData: ChartDataSets[] = [{
    data: this.temperatureHistory,
    label: 'Temperature'
  }, {
    data: this.humidityHistory,
    label: 'Humidity'
  }];

  public get latestData(): RawData {
    return this.raw[0];
  }

  @Input()
  public set raw(data: RawData[]) {
    this._raw = data;
    this.temperatureHistory = this.raw.filter(v => v.type === ComponentType.TEMP_HUMID)
      .map(v => v.data.split('_')[0])
      // tslint:disable-next-line:radix
      .map(Number)
      .reverse();
    this.lineChartData[0].data = this.temperatureHistory;
    this.humidityHistory = this.raw.filter(v => v.type === ComponentType.TEMP_HUMID)
      .map(v => v.data.split('_')[1])
      // tslint:disable-next-line:radix
      .map(Number)
      .reverse();
    this.lineChartData[1].data = this.humidityHistory;

    this.lineChartLabels = this.raw?.filter(v => v.type === ComponentType.TEMP_HUMID)
      .map(v => v.createdAt)
      .map(time => time.toDate())
      .map(v => {
        return v.getHours() + ':' + v.getMinutes() + ':' + v.getSeconds();
      });
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
