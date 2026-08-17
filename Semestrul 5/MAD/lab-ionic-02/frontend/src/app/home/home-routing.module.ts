import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePage } from './home.page';
import { DetailsComponent } from "../details/details.component";
import { UpdateComponent } from "../update/update.component";
import { AddComponent } from "../add/add.component";

const routes: Routes = [
  {
    path: '',
    component: HomePage,
  },
  {
    path: 'new',
    component: AddComponent,
  },
  {
    path: ':id',
    component: DetailsComponent,
  },
  {
    path: ':id/:action',
    component: UpdateComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomePageRoutingModule {}
