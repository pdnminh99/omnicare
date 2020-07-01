import { Component, OnInit, Input } from '@angular/core';
import { RawData } from 'src/app/models/node';

@Component({
  selector: 'app-pir-node',
  templateUrl: './pir-node.component.html',
  styleUrls: ['./pir-node.component.scss'],
})
export class PirNodeComponent implements OnInit {
  @Input() public data: RawData[] = [];
  @Input() public pinNumber: number;

  constructor() {}

  ngOnInit(): void {}

  public get latestData(): RawData {
    return this.data[0];
  }

  public get validData(): number[] {
    return this.data
      .filter((d) => d.type == 'LIGHT' && d.data == 'TRUE')
      .map((d) => d.createdAt.toMillis());
  }
}
