

export enum ComponentType {
  FIRE = 'FIRE',
  TEMP_HUMID = 'TEMP_HUMID',
  LIGHT = 'LIGHT'
}

export interface Node {

  componentId: string;

  pinNumber: number;

  lastRefresh: number;

  type: ComponentType;

}

export enum FireState {
  SAFE = 'SAFE',
  SMOKE = 'SMOKE',
  FIRE = 'FIRE',
  OFF = 'OFF'
}

export interface FireComponent extends Node {
  state: FireState;
}

export interface TempHumidComponent extends Node{
  temperature: number;

  humidity: number;
}

export interface LightComponent extends Node {
  lightOn: boolean;
}
