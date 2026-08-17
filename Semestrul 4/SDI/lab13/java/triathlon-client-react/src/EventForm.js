import React from 'react';

class EventForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            date: '',
            location: '',
            description: ''
        };
    }

    componentDidUpdate(prevProps) {
        if (this.props.editingEvent &&
            (!prevProps.editingEvent || prevProps.editingEvent.id !== this.props.editingEvent.id)) {
            this.setState({
                name: this.props.editingEvent.name || '',
                date: this.props.editingEvent.date || '',
                location: this.props.editingEvent.location || '',
                description: this.props.editingEvent.description || ''
            });
        }

        if (!this.props.editingEvent && prevProps.editingEvent) {
            this.setState({
                name: '',
                date: '',
                location: '',
                description: ''
            });
        }
    }

    handleInputChange = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    }

    handleSubmit = (event) => {
        event.preventDefault();

        const eventData = {
            name: this.state.name,
            date: this.state.date,
            location: this.state.location,
            description: this.state.description
        };

        if (this.props.editingEvent) {
            this.props.updateFunc(this.props.editingEvent.id, eventData);
        } else {
            this.props.createFunc(eventData);
        }

        this.setState({ name: '', date: '', location: '', description: '' });
    }

    render() {
        return (
            <div>
                <h3>{this.props.editingEvent ? 'Edit Event' : 'Add Event'}</h3>
                <form onSubmit={this.handleSubmit}>
                    <input name="name" value={this.state.name} onChange={this.handleInputChange} placeholder="Name" />
                    <input name="date" type="date" value={this.state.date} onChange={this.handleInputChange} />
                    <input name="location" value={this.state.location} onChange={this.handleInputChange} placeholder="Location" />
                    <input name="description" value={this.state.description} onChange={this.handleInputChange} placeholder="Description" />
                    <button type="submit">{this.props.editingEvent ? 'Update' : 'Add'}</button>
                </form>
            </div>
        );
    }
}

export default EventForm;