import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormagendaComponent } from './formagenda.component';

describe('FormagendaComponent', () => {
  let component: FormagendaComponent;
  let fixture: ComponentFixture<FormagendaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormagendaComponent]
    });
    fixture = TestBed.createComponent(FormagendaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
