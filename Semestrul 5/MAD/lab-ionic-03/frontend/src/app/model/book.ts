export class Book {
  id: number = 0;
  name: string = '';
  author: string = '';
  publishing_date!: Date;
  stars: boolean = false;
  userId: string = '';
  photo: string = '';
  latitude?: number;
  longitude?: number;

  constructor();

  constructor(
    id: number,
    name: string,
    author: string,
    publishing_date: Date,
    stars: boolean,
    userId: string,
    photo: string,
    latitude: number,
    longitude: number
  );

  constructor(
    id?: number,
    name?: string,
    author?: string,
    publishing_date?: Date,
    stars?: boolean,
    userId?: string,
    photo?: string,
    latitude?: number,
    longitude?: number
  ) {
    if (id !== undefined) this.id = id;
    if (name !== undefined) this.name = name;
    if (author !== undefined) this.author = author;
    if (publishing_date !== undefined) this.publishing_date = publishing_date;
    if (stars !== undefined) this.stars = stars;
    if (userId !== undefined) this.userId = userId;
    if (photo !== undefined) this.photo = photo;
    if (latitude !== undefined) this.latitude = latitude;
    if (longitude !== undefined) this.longitude = longitude;
  }
}
