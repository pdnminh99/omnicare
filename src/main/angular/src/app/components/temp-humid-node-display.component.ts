import {Component, Input} from '@angular/core';
import {TempHumidComponent} from '../models/node';


@Component({
  selector: 'app-temp-humid-display',
  template: `
    <div class="card">
      <h1 style="text-align: center; margin: 5px 0; font-size: large; font-weight: bolder;">Pin {{ node?.pinNumber }}</h1>

      <div class="node-display">

        <div class="cell">
          <h5>Type</h5>
          <div>{{ node?.type?.toString() }}</div>
        </div>

        <div class="cell">
          <h5>Humidity</h5>
          <div>{{ node?.humidity }}%</div>
        </div>

        <div class="cell">
          <h5>Temperature</h5>
          <div>{{ node?.temperature }}\u00B0C</div>
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
export class TempHumidNodeDisplayComponent {
  @Input()
  public node: TempHumidComponent;
}
