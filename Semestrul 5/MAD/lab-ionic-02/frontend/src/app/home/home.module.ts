import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { HomePage } from './home.page';

import { HomePageRoutingModule } from './home-routing.module';
import { DetailsComponent } from "../details/details.component";
import { UpdateComponent } from "../update/update.component";
import { AddComponent } from "../add/add.component";
import { LoginComponent } from "../login/login.component";


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    HomePageRoutingModule
  ],
  declarations: [HomePage, DetailsComponent, UpdateComponent, AddComponent, LoginComponent]
})
export class HomePageModule {}
