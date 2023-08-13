import { NgModule } from '@angular/core';
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import { ParentComponent } from './parent/parent.component';
import { ChildComponent } from './child/child.component';
import {CommonModule} from "@angular/common";
import { FormComponent } from './form/form.component';
import {ReactiveFormsModule} from "@angular/forms";



@NgModule({
  declarations: [
    ParentComponent,
    ChildComponent,
    FormComponent
  ],
  imports: [
    MatButtonModule,
    MatMenuModule,
    MatToolbarModule,
    MatIconModule,
    MatCardModule,
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [
    ParentComponent,
    ChildComponent,
    ChildComponent,
    FormComponent
  ]
})
export class SharedModule { }
