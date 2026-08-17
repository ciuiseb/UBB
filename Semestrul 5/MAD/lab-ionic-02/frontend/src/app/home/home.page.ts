import { Component, OnInit } from '@angular/core';
import { Book } from "../model/book";
import { BookService } from "../service/book.service";
import { Router } from "@angular/router";
import { Storage } from "@ionic/storage-angular";
import { Network } from "@capacitor/network";
import { Operation } from "../model/entities-with-operations";
import { LoadingController, ToastController, ViewWillEnter } from "@ionic/angular";
import { WebsocketService } from "../service/websocket.service";

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage implements OnInit, ViewWillEnter {
  public queue: Operation[] = [];
  public displayedBooks: Book[] = [];
  public networkStatus: boolean = true;

  public currentPage = 0;
  public pageSize = 5;
  public isLastPage = false;

  public currentSearchQuery = '';
  public currentYearFilter: number | null = null;

  private loading: HTMLIonLoadingElement | null = null;

  constructor(
    private bookService: BookService,
    public router: Router,
    public storage: Storage,
    private loadingController: LoadingController,
    private toastController: ToastController,
    private websocketService: WebsocketService,
  ) {}

  async ngOnInit() {
    await this.storage.create();

    Network.addListener('networkStatusChange', async status => {
      this.networkStatus = status.connected;
      if (this.networkStatus) {
        await this.processOfflineQueue();
        this.loadBooks(this.currentPage);
      }
    });

    this.websocketService.getMessages().subscribe(async (msg) => {
      if (msg.type === 'ADD' && this.currentPage === 0) {
        this.displayedBooks.unshift(msg.payload);
        if (this.displayedBooks.length > this.pageSize) this.displayedBooks.pop();
      }
      else if (msg.type === 'UPDATE') {
        const index = this.displayedBooks.findIndex(b => b.id === msg.payload.id);
        if (index !== -1) this.displayedBooks[index] = msg.payload;
      }
      else if (msg.type === 'DELETE') {
        const id = typeof msg.payload === 'object' ? msg.payload.id : msg.payload;
        this.displayedBooks = this.displayedBooks.filter(b => b.id !== id);
      }
    });
  }

  ionViewWillEnter() {
    this.currentPage = 0;
    this.loadBooks(0);
  }

  async loadBooks(page: number) {
    this.loading = await this.loadingController.create({ message: 'Loading...', duration: 3000 });
    await this.loading.present();

    const status = await Network.getStatus();
    this.networkStatus = status.connected;

    if (this.networkStatus) {
      try {
        (await this.bookService.findAll(page, this.pageSize, this.currentSearchQuery, this.currentYearFilter))
          .subscribe({
            next: async (response: any) => {
              const books = response.content || [];

              this.displayedBooks = books;
              this.currentPage = page;

              this.isLastPage = response.isLast !== undefined ? response.isLast : true;

              await this.storage.set('elements-cache', this.displayedBooks);

              await this.dismissLoading();
            },
            error: async (err) => {
              console.error("Server Failed:", err);
              await this.loadFromCache("Server unreachable. Showing offline data.");
              await this.dismissLoading();
            }
          });
      } catch (e) {
        await this.loadFromCache("Error initializing.");
        await this.dismissLoading();
      }
    } else {
      await this.loadFromCache("You are offline.");
      await this.dismissLoading();
    }
  }

  async loadFromCache(msg?: string) {
    const cached = await this.storage.get('elements-cache');
    this.displayedBooks = cached || [];
    this.isLastPage = true;

    if (msg) {
      const toast = await this.toastController.create({
        message: msg,
        duration: 2000,
        position: 'bottom',
        color: 'warning'
      });
      await toast.present();
    }
  }

  async dismissLoading() {
    if (this.loading) {
      await this.loading.dismiss();
      this.loading = null;
    }
  }

  public onYearChange($event: any) {
    const val = $event.detail.value;
    this.currentYearFilter = (val && val > 1900) ? val : null;

    this.currentPage = 0;
    this.loadBooks(0);
  }

  public onSearchChange($event: any) {
    this.currentSearchQuery = $event.detail.value;
    this.currentPage = 0;
    this.loadBooks(0);
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.loadBooks(this.currentPage - 1);
    }
  }

  nextPage() {
    if (!this.isLastPage) {
      this.loadBooks(this.currentPage + 1);
    }
  }

  private async processOfflineQueue() {
    this.queue = await this.storage.get('operations-cache') || [];
    if (this.queue.length === 0) return;

    const toast = await this.toastController.create({ message: 'Syncing offline changes...', duration: 2000 });
    await toast.present();

    for (const op of this.queue) {
      if (!op.entity) continue;

      try {
        if (op.type === 'ADD') {
          (await this.bookService.save(op.entity)).subscribe();
        } else if (op.type === 'UPD' && op.entity.id) {
          (await this.bookService.update(op.entity.id, op.entity)).subscribe();
        } else if (op.type === 'DEL' && op.entity.id) {
          (await this.bookService.del(op.entity.id)).subscribe();
        }
      } catch (err) {
        console.error("Sync failed for op", op);
      }
    }

    await this.router.navigate(['/items']);
    this.queue = [];
    await this.storage.set('operations-cache', []);

    this.loadBooks(0);
  }

  public async refreshList(event: any) {
    this.currentPage = 0;
    await this.loadBooks(0);
    event.target.complete();
  }

  public async handleAdd(){
    await this.router.navigate(['/items/new']);
  }

  public async handleLogout() {
    await this.storage.remove('token');
    await this.storage.remove('elements-cache');
    await this.router.navigate(['/login']);
  }
}
