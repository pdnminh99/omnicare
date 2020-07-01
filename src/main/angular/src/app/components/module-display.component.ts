import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ComponentType, Node} from '../models/node';
import {AngularFirestore} from '@angular/fire/firestore';
import {falseIfMissing} from 'protractor/built/util';

@Component({
  selector: 'app-module-display',
  template: `
    <div class="module-wrapper container">
      <div class="badge  badge-dark mt-5 mb-2">Node: {{ MAC }}</div>
      <app-component-display [node]="firstComponent"></app-component-display>
      <div class="my-4"></div>
      <app-component-display [node]="secondComponent"></app-component-display>
    </div>
  `,
  styles: [
      `
      .badge {
        font-size: 20px;
        font-weight: normal;
      }
    `,
  ],
})
export class ModuleDisplayComponent implements OnInit {

  public MAC = '3C:71:BF:3A:47:7D';

  public firstComponent: Node;

  public secondComponent: Node;

  constructor(private firestore: AngularFirestore) {
  }

  ngOnInit(): void {
    this.firestore
      .collection('components')
      .doc<Node>(`0_${this.MAC}`)
      .valueChanges()
      .subscribe(value => {
        this.firstComponent = value;
      });

    this.firestore
      .collection('components')
      .doc<Node>(`1_${this.MAC}`)
      .valueChanges()
      .subscribe(value => {
        this.secondComponent = value;
      });
  }

}

@Component({
  selector: 'app-component-display',
  template: `
    <div>
      <div
        class="node-not-active p-3 border rounded d-flex justify-content-center align-items-center"
        *ngIf="!isActive"
      >
        <img src="../../assets/sleep.png"/>
      </div>

      <div *ngIf="isActive">
        <app-fire-node-display
          *ngIf="componentType == 'FIRE'"
          [data]="node?.data"
        ></app-fire-node-display>
        <app-temp-humid-display
          *ngIf="componentType == 'TEMP_HUMID'"
          [raw]="node?.data"
        ></app-temp-humid-display>
        <app-light-display
          *ngIf="componentType == 'LIGHT'"
          [data]="node?.data"
          [pinNumber]="node?.pinNumber"
        ></app-light-display>
      </div>
    </div>
  `,
  styles: [
      `
      .node-not-active {
        text-align: center;
        font-family: sans-serif;
        font-size: large;
        font-weight: bolder;
        margin: 0 -15px 0 -15px;
      }

      .node-not-active img {
        width: 60px;
      }
    `,
  ],
})
export class NodeDisplayComponent {
  @Input()
  public node?: Node;

  public get componentType(): string {
    return this.node?.data[0].type as string;
  }

  public get isActive(): boolean {
    return this.node?.isActive;
  }
}
