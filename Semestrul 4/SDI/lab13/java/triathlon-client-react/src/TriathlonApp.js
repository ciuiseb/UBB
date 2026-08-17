import React from 'react';
import './TriathlonApp.css';
import EventTable from './EventTable';
import EventForm from './EventForm';
import { restCalls } from './utils/rest-calls';

class TriathlonApp extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      events: [],
      editingEvent: null,
      loading: false
    };
  }

  componentDidMount() {
    this.loadEvents();
  }

  loadEvents = async () => {
    this.setState({ loading: true });
    try {
      const events = await restCalls.getAllEvents();
      console.log('Loaded events:', events);
      this.setState({ events: events });
    } catch (error) {
      console.error('Error loading events:', error);
      alert('Failed to load events');
    } finally {
      this.setState({ loading: false });
    }
  }

  handleCreateEvent = async (eventData) => {
    try {
      const newEvent = await restCalls.createEvent(eventData);
      console.log('Created event:', newEvent);
      this.loadEvents(); // Reload the list
    } catch (error) {
      console.error('Error creating event:', error);
      alert('Failed to create event');
    }
  }

  handleUpdateEvent = async (id, eventData) => {
    try {
      const updatedEvent = await restCalls.updateEvent(id, eventData);
      console.log('Updated event:', updatedEvent);
      this.setState({ editingEvent: null });
      this.loadEvents(); // Reload the list
    } catch (error) {
      console.error('Error updating event:', error);
      alert('Failed to update event');
    }
  }

  handleDeleteEvent = async (id) => {
    try {
      await restCalls.deleteEvent(id);
      console.log('Deleted event:', id);
      this.loadEvents(); // Reload the list
    } catch (error) {
      console.error('Error deleting event:', error);
      alert('Failed to delete event');
    }
  }

  handleEditEvent = (event) => {
    console.log('Editing event:', event);
    this.setState({ editingEvent: event });
  }

  handleCancelEdit = () => {
    console.log('Cancel edit');
    this.setState({ editingEvent: null });
  }

  render() {
    return (
        <div className="App">
          <h1>Triathlon TriathlonApp</h1>
          <EventTable
              events={this.state.events}
              loading={this.state.loading}
              deleteFunc={this.handleDeleteEvent}
              editFunc={this.handleEditEvent}
          />

          <EventForm
              editingEvent={this.state.editingEvent}
              createFunc={this.handleCreateEvent}
              updateFunc={this.handleUpdateEvent}
              cancelFunc={this.handleCancelEdit}
          />


        </div>
    );
  }
}

export default TriathlonApp;