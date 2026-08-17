import {
  IonContent, IonHeader, IonPage, IonTitle, IonToolbar, IonList, IonItem, 
  IonLabel, IonInput, IonButton, IonSpinner, IonCard, IonCardContent, IonIcon
} from '@ionic/react';
import { useState, useEffect } from 'react';
import axios from 'axios';

const Home: React.FC = () => {
  const [zone, setZone] = useState<string>(localStorage.getItem('zone') || '');
  const [zoneInput, setZoneInput] = useState('');
  const [items, setItems] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [countInput, setCountInput] = useState<number | string>('');

  useEffect(() => {
    if (!zone) return;

    const savedItems = localStorage.getItem('inventory');
    if (savedItems) {
      setItems(JSON.parse(savedItems));
    } else {
      connectToWebSocket();
    }
  }, [zone]);

  const connectToWebSocket = () => {
    setLoading(true);
    const ws = new WebSocket('ws://localhost:3000'); 

    ws.onopen = () => {
      console.log('Connected to WS');
    };

    ws.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        setItems(data);
        localStorage.setItem('inventory', JSON.stringify(data));
        setLoading(false);
        ws.close();
      } catch (e) {
        console.error("Error parsing WS data", e);
      }
    };
    
    ws.onerror = (e) => {
        console.log("WS Error: Is the server running?", e);
        setLoading(false);
    }
  };

  const handleSetZone = () => {
    if (zoneInput.trim().length > 0) {
      localStorage.setItem('zone', zoneInput);
      setZone(zoneInput);
    }
  };

  const saveCount = async (code: number) => {
    const newCount = Number(countInput);
    if (isNaN(newCount) || newCount < 0) {
        alert("Please enter a valid non-negative number");
        return;
    }

    const updatedItems = items.map(it => 
      it.code === code ? { ...it, counted: newCount } : it
    );
    setItems(updatedItems);
    localStorage.setItem('inventory', JSON.stringify(updatedItems));
    setEditId(null);

    try {
      await axios.post('http://localhost:3000/audit', {
        code,
        counted: newCount,
        zone
      });
      console.log("Audit synced with server!");
    } catch (e) {
      console.error("Server offline? Saved locally only.", e);
      alert("Saved locally, but server seems unreachable.");
    }
  };

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar color="primary">
          <IonTitle>Audit App: {zone || "Setup"}</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent fullscreen className="ion-padding">
        
        {/* VIEW 1: Zone Setup */}
        {!zone && (
          <div style={{ display: 'flex', height: '100%', alignItems: 'center', justifyContent: 'center' }}>
            <IonCard style={{ width: '100%' }}>
                <IonHeader>
                    <IonToolbar>
                        <IonTitle>Welcome</IonTitle>
                    </IonToolbar>
                </IonHeader>
                <IonCardContent>
                <IonLabel>Enter Zone Name to Begin:</IonLabel>
                <IonInput 
                    value={zoneInput} 
                    onIonInput={(e) => setZoneInput(e.detail.value!)} 
                    className="ion-margin-vertical"
                    style={{ border: '1px solid #ddd', borderRadius: '4px', padding: '5px' }}
                />
                <IonButton expand="block" onClick={handleSetZone} disabled={!zoneInput}>Set Zone</IonButton>
                </IonCardContent>
            </IonCard>
          </div>
        )}

        {}
        {zone && loading && (
          <div style={{ textAlign: 'center', marginTop: 100 }}>
            <IonSpinner name="crescent" style={{ transform: 'scale(1.5)' }} />
            <p>Fetching inventory from server...</p>
          </div>
        )}

        {}
        {zone && !loading && (
          <IonList>
            {items.map(item => (
              <IonItem key={item.code} lines="full">
                <div style={{ width: '100%', padding: '10px 0' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    
                    {}
                    <div>
                        <h2 style={{ fontWeight: 'bold', fontSize: '1.1em' }}>{item.name}</h2>
                        <p style={{ fontSize: '0.9em', color: '#666' }}>System Qty: {item.quantity}</p>
                    </div>

                    {}
                    <div onClick={() => {

                        if (editId !== item.code) {
                            setEditId(item.code);
                            setCountInput(item.counted !== undefined ? item.counted : '');
                        }
                    }}>
                        {editId === item.code ? (

                            <div style={{ display: 'flex', alignItems: 'center', gap: '5px' }}>
                                <input 
                                    autoFocus
                                    type="number" 
                                    value={countInput}
                                    onChange={e => setCountInput(e.target.value)}
                                    style={{ width: '60px', padding: '5px', fontSize: '16px', border: '2px solid blue' }}
                                />
                                <IonButton size="small" onClick={(e) => {
                                    e.stopPropagation();
                                    saveCount(item.code);
                                }}>OK</IonButton>
                            </div>
                        ) : (

                            <div style={{ textAlign: 'center', minWidth: '60px', padding: '5px', border: '1px dashed #ccc', borderRadius: '5px' }}>
                                <div style={{ fontSize: '0.8em', color: '#888' }}>Counted</div>
                                <div style={{ 
                                    fontSize: '1.4em', 
                                    fontWeight: 'bold', 
                                    color: item.counted !== undefined ? '#3880ff' : '#ccc' 
                                }}>
                                    {item.counted !== undefined ? item.counted : '-'}
                                </div>
                            </div>
                        )}
                    </div>

                  </div>
                </div>
              </IonItem>
            ))}
          </IonList>
        )}

      </IonContent>
    </IonPage>
  );
};

export default Home;