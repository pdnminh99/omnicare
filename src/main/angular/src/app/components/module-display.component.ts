import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';
import { Subscription } from 'rxjs';
import { ComponentType, Node } from '../models/node';

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
export class ModuleDisplayComponent implements OnInit, OnDestroy {
  public MAC = '3C:71:BF:3A:47:7D';

  public firstComponent: Node;

  private firstNodeSubscription: Subscription;

  public secondComponent: Node;

  private secondNodeSubscription: Subscription;

  constructor(private firestore: AngularFirestore) {}

  ngOnInit(): void {
    this.firstNodeSubscription = this.firestore
      .collection<Node>('components')
      .doc(`0_${this.MAC}`)
      .valueChanges()
      .subscribe((value) => {
        this.firstComponent = value as Node;
      });
    this.secondNodeSubscription = this.firestore
      .collection<Node>('components')
      .doc(`1_${this.MAC}`)
      .valueChanges()
      .subscribe((value) => {
        this.secondComponent = value as Node;
      });
  }

  ngOnDestroy(): void {
    this.firstNodeSubscription?.unsubscribe();
    this.secondNodeSubscription?.unsubscribe();
  }
}

@Component({
  selector: 'app-component-display',
  template: `
    <div>
      <div
        class="node-not-active p-3 border rounded d-flex justify-content-center align-items-center"
        *ngIf="!isNodeActive"
      >
        <img src="../../assets/sleep.png" />
      </div>

      <div *ngIf="isNodeActive">
        <app-fire-node-display
          *ngIf="isFireNode"
          [node]="node"
        ></app-fire-node-display>
        <app-temp-humid-display
          *ngIf="isTempHumidNode"
          [node]="node"
        ></app-temp-humid-display>
        <app-light-display
          *ngIf="isLightNode"
          [node]="node"
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

  public get isFireNode(): boolean {
    return this.node?.type === ComponentType.FIRE;
  }

  public get isTempHumidNode(): boolean {
    return this.node?.type === ComponentType.TEMP_HUMID;
  }

  public get isLightNode(): boolean {
    return this.node?.type === ComponentType.LIGHT;
  }

  public get isNodeActive(): boolean {
    return this.node !== undefined;
  }
}
