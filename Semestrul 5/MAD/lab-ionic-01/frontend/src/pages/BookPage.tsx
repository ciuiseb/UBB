import React, {useState, useEffect} from 'react';
import {
    IonFab, IonFabButton, IonIcon, IonContent, IonPage, IonHeader,
    IonToolbar, IonTitle, IonList, IonItem, IonLabel, IonButton,
    IonSpinner, IonModal, IonInput, IonSelect, IonSelectOption,
    IonToggle, IonDatetime
} from '@ionic/react';
import {add, close} from 'ionicons/icons';
import {useBookWebSocket} from '../services/BookWebSocketService';
import Book from '../model/Book';
import BookDto from '../model/BookDto';

const API_BASE = 'http://localhost:8080/api';

const BookPage: React.FC = () => {
    const [books, setBooks] = useState<Book[]>([]);
    const [themes, setThemes] = useState<string[]>([]);
    const [selectedTheme, setSelectedTheme] = useState('All');
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);


    const [newBook, setNewBook] = useState<Book>({
        name: '',
        author: '',
        theme: '',
        has_nobel_prize: false,
        date_published: new Date()
    });

    const fetchBooks = async () => {
        console.log("Fetching books...");
        try {
            const response = await fetch(`${API_BASE}/books`);
            console.log("Response status:", response.status);
            const data: BookDto[] = await response.json();
            console.log("Books DTOs received:", data);

            const bookModels = data.map(dto => new Book(dto));

            setBooks(bookModels);
        } catch (err) {
            console.error("Error fetching books:", err);
        }
    };

    useEffect(() => {
        fetchBooks().finally(() => setLoading(false));
    }, []);

    useBookWebSocket(() => {
        fetchBooks();
    });

    useEffect(() => {
        console.log("Fetching themes...");
        fetch(`${API_BASE}/themes`)
            .then(res => {
                console.log("Response status:", res.status);
                return res.json();
            })
            .then(data => {
                console.log("Themes received:", data);
                setThemes(['All', ...data]);
            })
            .catch(err => console.error("Error fetching themes:", err));
    }, []);

    const filteredBooks = selectedTheme === 'All'
        ? books
        : books.filter(b => b.theme === selectedTheme);

    const handleAddBook = () => {
        setNewBook({
            name: '',
            author: '',
            theme: '',
            has_nobel_prize: false,
            date_published: new Date()
        });
        setShowModal(true);
    };

    const handleSaveBook = async () => {
        if (!newBook.name || !newBook.author || !newBook.theme) {
            alert('Please fill in all required fields.');
            return;
        }

        const bookDto = Book.toDto(newBook);

        try {
            const response = await fetch(`${API_BASE}/books`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(bookDto),
            });

            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);

            // const createdBook = await response.json(); // This is a DTO
            setShowModal(false);
        } catch (error) {
            console.error("Error adding book:", error);
        }
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Books</IonTitle>
                </IonToolbar>
            </IonHeader>

            <IonContent className="ion-padding">
                {loading ? (
                    <div style={{display: 'flex', justifyContent: 'center', marginTop: '2rem'}}>
                        <IonSpinner name="crescent"/>
                    </div>
                ) : (
                    <>
                        <div style={{marginBottom: '1rem'}}>
                            {themes.map(theme => (
                                <IonButton
                                    key={theme}
                                    fill={theme === selectedTheme ? 'solid' : 'outline'}
                                    onClick={() => setSelectedTheme(theme)}
                                    style={{marginRight: '0.5rem', marginBottom: '0.5rem'}}
                                >
                                    {theme}
                                </IonButton>
                            ))}
                        </div>

                        <IonList>
                            {filteredBooks.map((book, index) => (
                                <IonItem key={index}>
                                    <IonLabel>
                                        <h2>{book.name}</h2>
                                        <p>{book.author}</p>
                                        <p>Published: {book.date_published.toLocaleDateString()}</p>
                                        <small>{book.theme}</small>
                                    </IonLabel>
                                    {book.has_nobel_prize && (
                                        <IonIcon slot="end" color="warning" icon={add} />
                                    )}
                                </IonItem>
                            ))}
                        </IonList>

                        <IonFab vertical="bottom" horizontal="end" slot="fixed">
                            <IonFabButton color="primary" onClick={handleAddBook}>
                                <IonIcon icon={add}/>
                            </IonFabButton>
                        </IonFab>

                        <IonModal isOpen={showModal} onDidDismiss={() => setShowModal(false)}>
                            <IonHeader>
                                <IonToolbar>
                                    <IonTitle>Add New Book</IonTitle>
                                    <IonButton slot="end" fill="clear" onClick={() => setShowModal(false)}>
                                        <IonIcon icon={close}/>
                                    </IonButton>
                                </IonToolbar>
                            </IonHeader>

                            <IonContent className="ion-padding">
                                <IonItem>
                                    <IonLabel position="stacked">Name</IonLabel>
                                    <IonInput
                                        value={newBook.name}
                                        onIonChange={e => setNewBook({...newBook, name: e.detail.value!})}
                                        placeholder="Enter book name"
                                    />
                                </IonItem>

                                <IonItem>
                                    <IonLabel position="stacked">Author</IonLabel>
                                    <IonInput
                                        value={newBook.author}
                                        onIonChange={e => setNewBook({...newBook, author: e.detail.value!})}
                                        placeholder="Enter author"
                                    />
                                </IonItem>

                                <IonItem>
                                    <IonLabel position="stacked">Theme</IonLabel>
                                    <IonSelect
                                        value={newBook.theme}
                                        placeholder="Select theme"
                                        onIonChange={e => setNewBook({...newBook, theme: e.detail.value!})}
                                    >
                                        {themes
                                            .filter(t => t !== 'All')
                                            .map(theme => (
                                                <IonSelectOption key={theme} value={theme}>
                                                    {theme}
                                                </IonSelectOption>
                                            ))}
                                    </IonSelect>
                                </IonItem>

                                <IonItem>
                                    <IonLabel>Has Nobel Prize?</IonLabel>
                                    <IonToggle
                                        checked={newBook.has_nobel_prize}
                                        onIonChange={e => setNewBook({...newBook, has_nobel_prize: e.detail.checked})}
                                    />
                                </IonItem>

                                <IonItem>
                                    <IonLabel position="stacked">Date Published</IonLabel>
                                    <IonDatetime
                                        presentation="date"
                                        value={newBook.date_published.toISOString()}
                                        onIonChange={e => {
                                            if (e.detail.value) {
                                                setNewBook({...newBook, date_published: new Date(e.detail.value as string)})
                                            }
                                        }}
                                    />
                                </IonItem>

                                <IonButton
                                    expand="block"
                                    color="success"
                                    onClick={handleSaveBook}
                                    style={{marginTop: '1.5rem'}}
                                >
                                    Save Book
                                </IonButton>
                            </IonContent>
                        </IonModal>
                    </>
                )}
            </IonContent>
        </IonPage>
    );
};

export default BookPage;