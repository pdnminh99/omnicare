import {Component, Input} from '@angular/core';
import {LightComponent} from '../models/node';


@Component({
  selector: 'app-light-display',
  template: `
    <div>
      <h1 style="text-align: center; margin: 5px 0; font-size: large; font-weight: bolder;">Pin {{ node?.pinNumber }}</h1>

      <div class="node-display">

        <div class="cell">
          <h5>Type</h5>
          <div>{{ node?.type?.toString() }}</div>
        </div>

        <div class="cell">
          <h5>Light Status</h5>
          <div>{{ node?.lightOn ? 'ON' : 'OFF' }}</div>
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
  @Input()
  public node: LightComponent;
}
