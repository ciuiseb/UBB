import { Component, OnDestroy, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { Book } from "../model/book";
import { BookService } from "../service/book.service";
import { Subscription } from "rxjs";
import { Network } from "@capacitor/network";
import { Operation } from "../model/entities-with-operations";
import { Storage } from "@ionic/storage-angular";
import {AlertController, ModalController} from "@ionic/angular";
import { Camera, CameraResultType, CameraSource } from '@capacitor/camera';
import { Filesystem, Directory } from '@capacitor/filesystem';
import { Geolocation } from '@capacitor/geolocation';
import {LocationMapModalComponent} from "../location/location-map-modal.component";

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss'],
})
export class UpdateComponent implements OnInit, OnDestroy {
  private queue: Operation[] = [];
  public book: Book;

  public photo: string | undefined = undefined;

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
    private alertController: AlertController,
    private modalCtrl: ModalController
  ) {
    this.book= {
      id: 0,
      name: '',
      author: '',
      publishing_date: new Date(),
      stars: false,
      userId: "",
      photo: ""
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
        this.photo = retrievedBook.photo;
      });
    }
  }

  public ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  async takePhoto() {
    const image = await Camera.getPhoto({
      quality: 50,
      allowEditing: false,
      resultType: CameraResultType.Base64,
      source: CameraSource.Camera
    });

    const base64WithPrefix = 'data:image/jpeg;base64,' + image.base64String;

    this.book.photo = base64WithPrefix;
    this.photo = base64WithPrefix;

    if (image.base64String) {
      await this.saveImageToDevice(base64WithPrefix, `book_${this.book.id || Date.now()}.jpeg`);
    }
  }

  async saveImageToDevice(base64Data: string, fileName: string) {
    try {
      const rawBase64 = base64Data.replace(/^data:image\/\w+;base64,/, '');

      await Filesystem.writeFile({
        path: fileName,
        data: rawBase64,
        directory: Directory.Data,
      });
      console.log('File saved successfully to device: ' + fileName);
    } catch (e) {
      console.error('Unable to save file', e);
    }
  }

  public async updateBook() {
    if (!this.networkStatus) {
      const alert = await this.alertController.create({
        header: 'No Network Connection',
        message: 'You are currently offline. The updates will be saved to the cache and added later when you reconnect.',
        buttons: ['OK']
      });
      await alert.present();

      this.book.photo = this.photo || '';

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
  public async openMapModal() {
    let initialLat = this.book.latitude;
    let initialLng = this.book.longitude;

    if (!initialLat || !initialLng) {

      try {
        const position = await Geolocation.getCurrentPosition();
        initialLat = position.coords.latitude;
        initialLng = position.coords.longitude;
      } catch (e) {
        console.warn('Could not get current location.', e);
        if (!initialLat) initialLat = 44.4268;
        if (!initialLng) initialLng = 26.1025;
      }
    }

    const modal = await this.modalCtrl.create({
      component: LocationMapModalComponent,
      componentProps: {
        initialLat: initialLat,
        initialLng: initialLng
      }
    });

    await modal.present();

    const { data } = await modal.onWillDismiss();

    if (data && data.latitude !== undefined && data.longitude !== undefined) {
      this.book.latitude = data.latitude;
      this.book.longitude = data.longitude;
    }
  }
}
