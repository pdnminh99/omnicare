import {Component, Input} from '@angular/core';
import {FireComponent} from '../models/node';

@Component({
  selector: 'app-fire-node-display',
  template: `
    <div style="width: 100%; border: 1px black solid; border-radius: 20px; display: table">

      <div style="display: table-cell" *ngIf="node.state !== undefined && node.state !== null">
        <h3>State</h3>
        <span>{{ node?.state }}</span>
      </div>

      <div style="display: table-cell">
        <h3>Pin</h3>
        <span>{{ node?.pinNumber }}</span>
      </div>

      <div style="display: table-cell">
        <h3>Type</h3>
        <span>{{ node?.type?.toString() }}</span>
      </div>

    </div>
  `
})
export class FireNodeDisplayComponent {
  @Input()
  public node: FireComponent;
}
