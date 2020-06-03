import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div id="wrapper">
      <h1>Omnicare Application</h1>
      <app-module-display></app-module-display>
    </div>
  `,
  styles: [`
    #wrapper {
      width: 1000px;
      margin: 0 auto;
    }

    #wrapper h1 {
      text-align: center;
      font-family: sans-serif;
    }
  `]
})
export class AppComponent {

}
