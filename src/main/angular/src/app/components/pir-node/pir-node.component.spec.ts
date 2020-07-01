import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PirNodeComponent } from './pir-node.component';

describe('PirNodeComponent', () => {
  let component: PirNodeComponent;
  let fixture: ComponentFixture<PirNodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PirNodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PirNodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
