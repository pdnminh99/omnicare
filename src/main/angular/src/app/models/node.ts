import Timestamp = firebase.firestore.Timestamp;
import * as firebase from 'firebase';

export enum ComponentType {
  FIRE = 'FIRE',
  TEMP_HUMID = 'TEMP_HUMID',
  LIGHT = 'LIGHT'
}

// export interface Node {
//
//   componentId: string;
//
//   pinNumber: number;
//
//   lastRefresh: number;
//
//   type: ComponentType;
//
// }

// export enum FireState {
//   SAFE = 'SAFE',
//   SMOKE = 'SMOKE',
//   FIRE = 'FIRE',
//   OFF = 'OFF'
// }

export interface RawData {

  type: ComponentType;

  data: string;

  createdAt: Timestamp;
  // // tslint:disable-next-line:variable-name
  // protected _type: ComponentType;
  //
  // public set type(value: string) {
  //   this._type = value as ComponentType;
  // }
  //
  // public get type(): string {
  //   return this._type as string;
  // }
  //
  // public get typeValue(): ComponentType {
  //   return this._type;
  // }
  //
  // constructor(protected data: string, private createdAt: number) {
  // }
}

// export class FireData extends RawData {
//
//   public get state(): FireState {
//     return this.data as FireState;
//   }
//
// }
//
// export class TempHumidComponent extends RawData {
//
//   public get temperature(): number {
//     return Number(this.data.split('_')[0]);
//   }
//
//   public get humidity(): number {
//     return Number(this.data.split('_')[1]);
//   }
//
// }

export interface Node {
  data: RawData[];

  isActive: boolean;

  lastRefresh: Timestamp;

  dataCount: number;

  pinNumber: number;
}

// export interface FireComponent extends Node {
//   state: FireState;
// }
//
// export interface TempHumidComponent extends Node{
//   temperature: number;
//
//   humidity: number;
// }
//
// export interface LightComponent extends Node {
//   lightOn: boolean;
// }
