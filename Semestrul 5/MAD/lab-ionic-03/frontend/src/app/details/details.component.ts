import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Book} from "../model/book";
import {BookService} from "../service/book.service";
import {Network} from "@capacitor/network";
import {trigger, state, style, animate, transition} from '@angular/animations';
// import { Filesystem, Directory } from '@capacitor/filesystem';
import {AlertController} from '@ionic/angular';
import * as L from 'leaflet';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
  animations: [
    trigger('fade', [
      state('void', style({
        opacity: 0,
        transform: 'translateY(10px)'
      })),


      transition(':enter', [
        animate('1500ms ease-in', style({
          opacity: 1,
          transform: 'translateY(0)'
        }))
      ]),
    ])
  ]
})
export class DetailsComponent implements OnInit {
  public book: Book | null = null;
  private readonly id: string | null;
  public networkStatus: boolean = true;
  private map: L.Map | undefined;

  constructor(
    private activeRoute: ActivatedRoute,
    private bookService: BookService,
    private alertController: AlertController
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
      setTimeout(() => this.initializeDisplayMap(), 500);
    }
  }

  async downloadPhoto() {
    if (!this.book || !this.book.photo) {
      const alert = await this.alertController.create({
        header: 'Error',
        message: 'No photo data available to download.',
        buttons: ['OK']
      });
      await alert.present();
      return;
    }

    try {
      const photoData = this.book.photo;
      const fileName = `photo_${this.book.name.replace(/\s/g, '_')}_${Date.now()}.jpeg`;

      const a = document.createElement('a');

      a.download = fileName;
      a.href = photoData;

      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);

      const successAlert = await this.alertController.create({
        header: 'Success',
        message: `Photo successfully sent to your browser's Downloads folder as ${fileName}.`,
        buttons: ['OK']
      });
      await successAlert.present();

    } catch (e) {
      console.error('Error saving file:', e);
      this.alertController.create({
        header: 'Error',
        message: 'An error occurred while saving the photo.',
        buttons: ['OK']
      }).then(a => a.present());
    }
  }
  private initializeDisplayMap() {
    if (!this.book || !this.book.latitude || !this.book.longitude) {
      return;
    }
    const coords: L.LatLngTuple = [this.book.latitude, this.book.longitude];

    if (this.map) {
      this.map.remove();
    }
    this.map = L.map('display-map-container', {
      center: coords,
      zoom: 15,
      zoomControl: false
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    L.marker(coords).addTo(this.map)
      .bindPopup(`<b>${this.book.name}</b>`).openPopup();
  }
}
