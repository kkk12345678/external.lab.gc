import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class TagService {
  private httpHeaders = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
    })
  };

  constructor(
    private http: HttpClient
  ) {}

  public async sendGetRequest(url:string): Promise<any>{
    return this.http.get(url, this.httpHeaders).pipe(
      tap({
        error: error => {
          console.log(error.message);
        }}));
  }
}
