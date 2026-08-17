import { Component, OnDestroy, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { Book } from "../model/book";
import { BookService } from "../service/book.service";
import { Subscription } from "rxjs";
import { Network } from "@capacitor/network";
import { Operation } from "../model/entities-with-operations";
import { Storage } from "@ionic/storage-angular";
import {AlertController} from "@ionic/angular";

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss'],
})
export class UpdateComponent implements OnInit, OnDestroy {
  private queue: Operation[] = [];
  public book: Book;
  public date: Date = new Date();
  public isDeleteAction: boolean = false;
  private subscription: Subscription | null = null;
  private readonly id: string | null;
  public networkStatus: boolean = false;

  public constructor(
    private activeRoute: ActivatedRoute,
    private bookService: BookService,
    public router: Router,
    private storage: Storage,
    private alertController: AlertController
  ) {
    this.book= {
      id: 0,
      name: '',
      author: '',
      publishing_date: new Date(),
      stars: false,
      userId: "vI1GMkzBza5i82zj"
    };
    this.id = this.activeRoute.snapshot.paramMap.get('id');
    this.isDeleteAction = this.activeRoute.snapshot.paramMap.get('action') === 'delete';

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

  public ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  public async updateBook() {
    if (!this.networkStatus) {
      const alert = await this.alertController.create({
        header: 'No Network Connection',
        message: 'You are currently offline. The updates will be saved to the cache and added later when you reconnect.',
        buttons: ['OK']
      });
      await alert.present();

      this.queue = await this.storage.get('operations-cache');
      this.queue.push({type: 'UPD', entity: this.book});
      await this.storage.set('operations-cache', this.queue);
    } else {
      this.subscription = (await this.bookService.update(this.book.id!!, this.book)).subscribe(() => {});
    }
    await this.router.navigate(['/items']);
  }

  public async deleteBook() {
    if (!this.networkStatus) {
      const alert = await this.alertController.create({
        header: 'No Network Connection',
        message: 'You are currently offline. The deletion will be saved to the cache and executed later when you reconnect.',
        buttons: ['OK']
      });
      await alert.present();

      this.queue = await this.storage.get('operations-cache');
      this.queue.push({type: 'DEL', entity: this.book});
      await this.storage.set('operations-cache', this.queue);
    }
    else {
      this.subscription = (await this.bookService.del(this.book.id!!)).subscribe(() => {});
    }
    await this.router.navigate(['/items']);
  }
}
