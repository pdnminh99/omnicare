import {Component, Input} from '@angular/core';
import {Node, RawData} from '../models/node';


@Component({
  selector: 'app-light-display',
  template: `
    <div class="card">
      <h1 style="text-align: center; margin: 5px 0; font-size: large; font-weight: bolder;">Pin {{ pinNumber }}</h1>

      <div class="node-display">

        <div class="cell">
          <h5>Type</h5>
          <div>LIGHT</div>
        </div>

        <div class="cell">
          <h5>Light Status</h5>
          <div>{{ latestData?.data || 'OFF' }}</div>
        </div>

      </div>

    </div>
  `,
  styles: [`
    .cell {
      font-family: sans-serif;
      display: table-cell;
      vertical-align: top;
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
      width: 100%;
      height: 100px;
      display: table;
    }

    .node-display:first-child {
      padding-left: 20px;
    }

  `]
})
export class LightNodeDisplayComponent {
  public get latestData(): RawData {
    return this.data[0];
  }

  @Input()
  public data: RawData[] = [];

  @Input()
  public pinNumber: number;
}
