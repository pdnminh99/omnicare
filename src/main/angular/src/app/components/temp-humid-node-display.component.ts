import {Component, Input} from '@angular/core';
import {TempHumidComponent} from '../models/node';


@Component({
  selector: 'app-temp-humid-display',
  template: `
    <div style="width: 100%; border: 1px black solid; border-radius: 20px; display: table">

      <div style="display: table-cell">
        <h3>Pin</h3>
        <span>{{ node?.pinNumber }}</span>
      </div>

      <div style="display: table-cell">
        <h3>Type</h3>
        <span>{{ node?.type?.toString() }}</span>
      </div>

      <div style="display: table-cell">
        <h3>Humidity</h3>
        <span>{{ node?.humidity }}%</span>
      </div>

      <div style="display: table-cell">
        <h3>Temperature</h3>
        <span>{{ node?.temperature }}oC</span>
      </div>

    </div>
  `
})
export class TempHumidNodeDisplayComponent {
  @Input()
  public node: TempHumidComponent;
}
