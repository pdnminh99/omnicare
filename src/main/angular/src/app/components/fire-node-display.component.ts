import { Component, Input } from '@angular/core';
import { FireComponent } from '../models/node';

@Component({
  selector: 'app-fire-node-display',
  template: `
    <div
      class="row border rounded p-3"
      [ngClass]="{
        'bg-success': node?.state == 'SAFE',
        'bg-danger': node?.state == 'FIRE',
        'bg-warning': node?.state == 'SMOKE',
        'white-text': node?.state == 'SAFE' || node?.state == 'FIRE'
      }"
    >
      <!-- <h1
        style="text-align: center; margin: 5px 0; font-size: large; font-weight: bolder;"
      >
        Port {{ node?.pinNumber }}
      </h1> -->
      <div class="col-12 col-sm-3 d-flex justify-content-center">
        <img src="../../assets/fire.png" width="80px" height="80px" />
      </div>

      <div
        class="node-display col-12 col-sm-9 d-flex justify-content-center align-items-center"
      >
        <!-- <div class="cell">
          <h5>Type</h5>
          <div>{{ node?.type?.toString() }}</div>
        </div> -->

        <div class="state-text">{{ node?.state || 'Unknown value' }}</div>
      </div>
    </div>
  `,
  styles: [
    `
      .cell h5 {
        text-align: center;
        font-weight: normal;
      }

      .state-text {
        font-size: 30px;
        font-weight: bolder;
      }

      .white-text {
        color: white;
      }
    `,
  ],
})
export class FireNodeDisplayComponent {
  @Input()
  public node: FireComponent;
}
