import { useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import Book from '../model/Book';
import BookDto from "../model/BookDto";

export const useBookWebSocket = (onNewBook: (book: Book) => void) => {
  const clientRef = useRef<Client | null>(null);

  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      onConnect: () => {
        console.log('WebSocket connected');
        client.subscribe('/topic/newBooks', (message) => {
          const bookDto: BookDto = JSON.parse(message.body);
          const newBook: Book = new Book(bookDto);
          onNewBook(newBook);
        });
      },
      onWebSocketError: (err) => {
        console.error('WebSocket error', err);
      }
    });

    client.activate();
    clientRef.current = client;

    return () => {
      client.deactivate();
    };
  }, []);

  return null;
};