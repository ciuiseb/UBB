import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';

import { LocationMapModalComponent } from './location-map-modal.component';

import { MultiMapModalComponent } from './multi-map-modal.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule
  ],

  declarations: [
    LocationMapModalComponent,
    MultiMapModalComponent
  ],
  exports: [
    LocationMapModalComponent,
    MultiMapModalComponent
  ]
})
export class LocationMapsModule { }
