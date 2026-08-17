import { Component, OnInit, Input, AfterViewInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import * as L from 'leaflet';
import { Geolocation } from '@capacitor/geolocation';

@Component({
  selector: 'app-location-map-modal',
  templateUrl: './location-map-modal.component.html',
  // styleUrls: ['./location-map-modal.component.scss'],
})
export class LocationMapModalComponent implements OnInit, AfterViewInit {

  @Input() initialLat: number | undefined;
  @Input() initialLng: number | undefined;

  private map: L.Map | undefined;
  private marker: L.Marker | undefined;

  public selectedLat: number | undefined;
  public selectedLng: number | undefined;

  constructor(private modalCtrl: ModalController) {}

  ngOnInit() {
    this.selectedLat = this.initialLat;
    this.selectedLng = this.initialLng;
  }

  ngAfterViewInit() {
    setTimeout(() => this.initializeMap(), 100);
  }

  private async initializeMap() {
    const defaultCenter: L.LatLngTuple = [44.4268, 26.1025];
    let center: L.LatLngTuple;

    if (this.selectedLat && this.selectedLng) {
      center = [this.selectedLat, this.selectedLng];
    } else {
      try {
        const position = await Geolocation.getCurrentPosition();
        center = [position.coords.latitude, position.coords.longitude];
      } catch (e) {
        center = defaultCenter;
        console.warn('Geolocation failed, falling back to default center.', e);
      }
    }

    this.map = L.map('map-container', {
      center: center,
      zoom: 15
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    if (this.selectedLat && this.selectedLng) {
      this.addMarker(this.selectedLat, this.selectedLng);
    }
    this.map.on('click', (e: L.LeafletMouseEvent) => this.onMapClick(e));
    this.map.invalidateSize();
  }

  private addMarker(lat: number, lng: number) {
    const coords: L.LatLngTuple = [lat, lng];
    if (this.marker) {
      this.marker.setLatLng(coords);
    } else {
      this.marker = L.marker(coords).addTo(this.map!);
    }
    this.map!.setView(coords, this.map!.getZoom());
  }

  private onMapClick(e: L.LeafletMouseEvent) {
    this.selectedLat = e.latlng.lat;
    this.selectedLng = e.latlng.lng;
    this.addMarker(this.selectedLat, this.selectedLng);
  }

  public dismiss() {
    return this.modalCtrl.dismiss(null, 'cancel');
  }

  public saveLocation() {
    if (!this.selectedLat || !this.selectedLng) {
      return;
    }
    return this.modalCtrl.dismiss({
      latitude: this.selectedLat,
      longitude: this.selectedLng
    }, 'confirm');
  }
}
