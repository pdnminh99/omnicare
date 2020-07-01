import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FireNodeDisplayComponent} from './components/fire-node-display.component';
import {
  ModuleDisplayComponent, NodeDisplayComponent,
} from './components/module-display.component';
import {AngularFireModule} from '@angular/fire';
import {environment} from '../environments/environment';
import {TempHumidNodeDisplayComponent} from './components/temp-humid-node-display.component';
import {LightNodeDisplayComponent} from './components/light-node-display.component';
import {NavBarComponent} from './components/nav-bar/nav-bar.component';
import {AngularFireMessagingModule} from '@angular/fire/messaging';
import {ChartsModule} from 'ng2-charts';
import { PirNodeComponent } from './components/pir-node/pir-node.component';

@NgModule({
  declarations: [
    AppComponent,
    FireNodeDisplayComponent,
    ModuleDisplayComponent,
    NodeDisplayComponent,
    TempHumidNodeDisplayComponent,
    LightNodeDisplayComponent,
    NavBarComponent,
    PirNodeComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ChartsModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFireMessagingModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {
}
