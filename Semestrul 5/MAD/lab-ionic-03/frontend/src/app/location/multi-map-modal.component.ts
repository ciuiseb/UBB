import { Component, OnInit, Input, AfterViewInit } from '@angular/core';
import { ModalController } from '@ionic/angular';
import * as L from 'leaflet';

export interface MapMarker {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
}

@Component({
  selector: 'app-multi-map-modal',
  templateUrl: './multi-map-modal.component.html',
})
export class MultiMapModalComponent implements OnInit, AfterViewInit {
  @Input() markers: MapMarker[] = [];

  private map: L.Map | undefined;

  private readonly FALLBACK_CENTER: L.LatLngTuple = [44.4268, 26.1025];

  constructor(private modalCtrl: ModalController) {}

  ngOnInit() {}

  ngAfterViewInit() {
    setTimeout(() => this.initializeMap(), 100);
  }

  private initializeMap() {
    if (this.map) {
      this.map.remove();
    }

    const initialCenter: L.LatLngTuple = this.markers.length > 0
      ? [this.markers[0].latitude, this.markers[0].longitude]
      : this.FALLBACK_CENTER;

    this.map = L.map('multi-map-container', {
      center: initialCenter,
      zoom: 10,
      zoomControl: true
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    this.addMarkersToMap();
    this.fitMapToMarkers();

    this.map.invalidateSize();
  }

  private addMarkersToMap() {
    this.markers.forEach(marker => {
      L.marker([marker.latitude, marker.longitude])
        .addTo(this.map!)
        .bindPopup(`<b>${marker.name}</b><br>ID: ${marker.id}`)
        .openPopup();
    });
  }

  private fitMapToMarkers() {
    if (this.markers.length < 1) return;

    const latLngs: L.LatLng[] = this.markers.map(m => L.latLng(m.latitude, m.longitude));
    const bounds = L.latLngBounds(latLngs);

    if (this.markers.length > 1 || bounds.isValid()) {
      this.map!.fitBounds(bounds, {
        padding: [50, 50],
        maxZoom: 16
      });
    }
  }

  public dismiss() {
    return this.modalCtrl.dismiss(null, 'cancel');
  }
}
