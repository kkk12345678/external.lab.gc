import {Component} from '@angular/core';
import {TagService} from "../../featured/tag.service";
import {HttpClient, HttpHandler} from "@angular/common/http";


@Component({
  //constructor(tagService: TagService) {},

  selector: 'app-parent',
  templateUrl: './parent.component.html',
  styleUrls: ['./parent.component.css']
})

export class ParentComponent {
  items = ["Item1", "Item2", "Item3", "Item4", "Item5"];

}
