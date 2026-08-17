import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Book } from "../model/book";
import { Observable } from "rxjs";
import { Storage } from "@ionic/storage-angular";
import {MapMarker} from "../location/multi-map-modal.component";

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private readonly serverUrl: string;

  constructor(private http: HttpClient, private storage: Storage) {
    this.serverUrl = "http://localhost:3000/api/books";
  }

  public async findAll(page: number = 0, size: number = 10, search: string = '', year: number | null = null): Promise<Observable<any>> {
    const token = await this.storage.get('token');
    const headers = new HttpHeaders().set("Authorization", "Bearer " + token);

    let url = `${this.serverUrl}?page=${page}&size=${size}`;

    if (search) {
      url += `&search=${encodeURIComponent(search)}`;
    }

    if (year) {
      url += `&year=${year}`;
    }

    return this.http.get<any>(url, { headers });
  }

  public async findOne(id: number): Promise<Observable<Book>> {
    const token = await this.storage.get('token');
    const headers = new HttpHeaders().set("Authorization", "Bearer " + token);
    return this.http.get<Book>(this.serverUrl + '/' + id, { headers: headers });
  }

  public async save(book: Book) {
    const token = await this.storage.get('token');
    const headers = new HttpHeaders().set("Authorization", "Bearer " + token);
    return this.http.post(this.serverUrl, book, { headers: headers });
  }

  public async update(id: number, book: Book) {
    const token = await this.storage.get('token');
    const headers = new HttpHeaders().set("Authorization", "Bearer " + token);
    return this.http.put(this.serverUrl + '/' + id, book, { headers: headers });
  }

  public async del(id: number) {
    const token = await this.storage.get('token');
    const headers = new HttpHeaders().set("Authorization", "Bearer " + token);
    return this.http.delete(this.serverUrl + '/' + id, { headers: headers });
  }

  public async findAllLocations(): Promise<Observable<MapMarker[]>> {
    const token = await this.storage.get('token');
    const headers = new HttpHeaders().set("Authorization", "Bearer " + token);

    const url = `${this.serverUrl}/all-locations`;
    return this.http.get<MapMarker[]>(url, { headers });
  }
}
