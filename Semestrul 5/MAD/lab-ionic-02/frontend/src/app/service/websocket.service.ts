import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private client: Client;
  private messageSubject = new Subject<any>();

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://localhost:3000/ws',
      reconnectDelay: 5000,
      debug: (str) => {
        console.log(str);
      }
    });

    this.client.onConnect = (frame) => {
      console.log('✅ Connected to WebSocket');

      this.client.subscribe('/topic/newBooks', (message: Message) => {
        if (message.body) {
          this.messageSubject.next({ type: 'ADD', payload: JSON.parse(message.body) });
        }
      });

      this.client.subscribe('/topic/updatedBooks', (message: Message) => {
        if (message.body) {
          this.messageSubject.next({ type: 'UPDATE', payload: JSON.parse(message.body) });
        }
      });

      this.client.subscribe('/topic/deletedBooks', (message: Message) => {
        if (message.body) {
          this.messageSubject.next({ type: 'DELETE', payload: JSON.parse(message.body) });
        }
      });
    };

    this.client.activate();
  }

  getMessages() {
    return this.messageSubject.asObservable();
  }
}
