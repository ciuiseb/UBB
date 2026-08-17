import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Book } from "../model/book";
import { BookService } from "../service/book.service";
import { Network } from "@capacitor/network";

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
})
export class DetailsComponent  implements OnInit {
  public book: Book | null = null;
  private readonly id: string | null;
  public networkStatus: boolean = true;

  constructor(
    private activeRoute: ActivatedRoute,
    private bookService: BookService
  ) {
    this.id = this.activeRoute.snapshot.paramMap.get('id');
    Network.addListener('networkStatusChange', status => {
      this.networkStatus = status.connected;
    });
  }

  public async ngOnInit() {
    this.networkStatus = (await Network.getStatus()).connected;
    if (this.id == null) return;
    const bookId = Number(this.id);

    if (this.networkStatus) {
      (await this.bookService.findOne(bookId)).subscribe((retrievedBook) => {
        this.book = retrievedBook;
      });
    }
  }
}
