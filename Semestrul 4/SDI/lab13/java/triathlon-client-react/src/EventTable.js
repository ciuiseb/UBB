import React from 'react';
import { Edit, Trash2, Calendar, MapPin, FileText } from 'lucide-react';

class EventRow extends React.Component {
    handleDelete = (event) => {
        console.log('delete button pentru ' + this.props.event.id);
        if (window.confirm(`Are you sure you want to delete "${this.props.event.name}"?`)) {
            this.props.deleteFunc(this.props.event.id);
        }
    }

    handleEdit = (event) => {
        console.log('edit button pentru ' + this.props.event.id);
        this.props.editFunc(this.props.event);
    }

    render() {
        return (
            <tr>
                <td>{this.props.event.name}</td>
                <td>
                    <Calendar size={16} className="icon" />
                    {this.props.event.date}
                </td>
                <td>
                    <MapPin size={16} className="icon" />
                    {this.props.event.location}
                </td>
                <td>
                    {this.props.event.description && (
                        <>
                            <FileText size={16} className="icon" />
                            {this.props.event.description}
                        </>
                    )}
                </td>
                <td>
                    <button onClick={this.handleEdit}>Edit</button>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        );
    }
}

class EventTable extends React.Component {
    render() {
        let rows = [];
        let functieStergere = this.props.deleteFunc;
        let functieEditare = this.props.editFunc;

        this.props.events.forEach(function(event) {
            rows.push(
                <EventRow
                    event={event}
                    key={event.id}
                    deleteFunc={functieStergere}
                    editFunc={functieEditare}
                />
            );
        });

        if (this.props.loading) {
            return (
                <div className="loading">
                    <p>Loading events...</p>
                </div>
            );
        }

        if (this.props.events.length === 0) {
            return (
                <div className="no-events">
                    <p>No triathlon events found. Create your first event!</p>
                </div>
            );
        }

        return (
            <div className="EventTable">
                <h2>Triathlon Events</h2>
                <table className="center">
                    <thead>
                    <tr>
                        <th>Event Name</th>
                        <th>Date</th>
                        <th>Location</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </div>
        );
    }
}

export default EventTable;