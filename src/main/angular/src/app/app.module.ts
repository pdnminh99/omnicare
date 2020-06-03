import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FireNodeDisplayComponent} from './components/fire-node-display.component';
import {NodeDisplayComponent, ModuleDisplayComponent} from './components/module-display.component';
import {AngularFirestoreModule} from '@angular/fire/firestore';
import {AngularFireModule} from '@angular/fire';
import {environment} from '../environments/environment';
import {TempHumidNodeDisplayComponent} from './components/temp-humid-node-display.component';

@NgModule({
  declarations: [
    AppComponent,
    FireNodeDisplayComponent,
    ModuleDisplayComponent,
    NodeDisplayComponent,
    TempHumidNodeDisplayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFirestoreModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
