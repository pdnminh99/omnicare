import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';
import { Subscription } from 'rxjs';
import { ComponentType, Node } from '../models/node';

@Component({
  selector: 'app-module-display',
  template: `
    <div class="module-wrapper">
      <h2 class="MAC-display">MAC address: {{ MAC }}</h2>
      <app-component-display [node]="firstComponent"></app-component-display>
      <div class="my-4"></div>
      <app-component-display [node]="secondComponent"></app-component-display>
    </div>
  `,
  styles: [
    `
      .MAC-display {
        margin-bottom: 20px;
      }
    `,
  ],
})
export class ModuleDisplayComponent implements OnInit, OnDestroy {
  public MAC = '9sNvRgOD7mMsoSQh7Y2Y';

  public firstComponent: Node;

  private firstNodeSubscription: Subscription;

  public secondComponent: Node;

  private secondNodeSubscription: Subscription;

  constructor(private firestore: AngularFirestore) {}

  ngOnInit(): void {
    this.firstNodeSubscription = this.firestore
      .collection<Node>('components')
      .doc(`1_${this.MAC}`)
      .valueChanges()
      .subscribe((value) => {
        this.firstComponent = value as Node;
      });
    this.secondNodeSubscription = this.firestore
      .collection<Node>('components')
      .doc(`2_${this.MAC}`)
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
        class="node-not-active card d-flex justify-content-center align-items-center"
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
        width: 100%;
        height: 100px;
        text-align: center;
        font-family: sans-serif;
        font-size: large;
        font-weight: bolder;
        margin: 5px 0;
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
