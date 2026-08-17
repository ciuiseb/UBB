import { Component, OnInit } from '@angular/core';
import { Book } from "../model/book";
import { BookService } from "../service/book.service";
import { Network } from "@capacitor/network";
import { Storage } from "@ionic/storage-angular";
import { AlertController } from "@ionic/angular";
import { Router } from "@angular/router";
import { Operation } from "../model/entities-with-operations";

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss'],
})
export class AddComponent implements OnInit {
  public book: Book;
  public networkStatus: boolean = true;

  constructor(
    private bookService: BookService,
    public router: Router,
    private storage: Storage,
    private alertController: AlertController
  ) {
    this.book = new Book();
  }

  public async ngOnInit(): Promise<void> {
    await this.storage.create();
    const status = await Network.getStatus();
    this.networkStatus = status.connected;

    Network.addListener('networkStatusChange', status => {
      this.networkStatus = status.connected;
    });
  }

  public async addBook() {
    const status = await Network.getStatus();

    if (!status.connected) {
      await this.saveToLocalQueue("You are offline. Saved locally.");
    } else {
      (await this.bookService.save(this.book)).subscribe({
        next: async () => {
          await this.router.navigate(['/items']);
        },
        error: async (err) => {
          console.error("Server failed", err);
          await this.saveToLocalQueue("Server unreachable. Saved locally.");
        }
      });
    }
  }

  private async saveToLocalQueue(message: string) {
    try {
      const queue: Operation[] = await this.storage.get('operations-cache') || [];
      queue.push({ type: 'ADD', entity: this.book });
      await this.storage.set('operations-cache', queue);
      try {
        const alert = await this.alertController.create({
          header: 'Saved Offline',
          message: message,
          buttons: ['OK']
        });
        await alert.present();
      } catch (e) {
        window.alert(message);
      } finally {
      }

      await this.router.navigate(['/items']);
    } catch (e) {
      console.error("Error saving to queue", e);
    }
  }
}
