import { Component, Input } from '@angular/core';
import { TempHumidComponent } from '../models/node';

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
        <img src="../../assets/temperature.png" width="80px" height="80px" />
      </div>
      <div class="node-display col-12 col-sm-9">
        <div class="cell">
          <h5>Temperature</h5>
          <div>{{ node?.temperature | number: '1.0-2' }}°C</div>
        </div>
        <div class="cell">
          <h5>Humidity</h5>
          <div>{{ node?.humidity }}%</div>
        </div>
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
  @Input()
  public node: TempHumidComponent;
}
