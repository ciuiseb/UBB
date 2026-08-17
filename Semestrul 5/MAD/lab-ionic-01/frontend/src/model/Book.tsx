import BookDto from "./BookDto";

export class Book {
    name: string;
    author: string;
    theme: string;
    has_nobel_prize: boolean;
    date_published: Date;

    constructor(dto: BookDto) {
        this.name = dto.name;
        this.author = dto.author;
        this.theme = dto.theme;
        this.has_nobel_prize = (dto.has_nobel_prize === "true");
        this.date_published = new Date(dto.date_published);
    }

    static toDto(book: Book): BookDto {
        return {
            name: book.name,
            author: book.author,
            theme: book.theme,
            has_nobel_prize: String(book.has_nobel_prize),
            date_published: book.date_published.toISOString().split('T')[0]
        };
    }
}

export default Book;