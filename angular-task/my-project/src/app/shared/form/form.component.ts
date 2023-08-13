import { Component } from '@angular/core';
import {FormGroup, FormControl} from "@angular/forms";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent {
  nameForm = new FormGroup({
    name: new FormControl()
  })

  onSubmit() {
    console.log(this.nameForm.value);
  }
}
