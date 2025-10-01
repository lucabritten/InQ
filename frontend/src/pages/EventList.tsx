import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { eventService } from '../services/eventService';
import type { Event } from '../types';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

export default function EventList() {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    loadEvents();
  }, []);

  const loadEvents = async () => {
    try {
      setLoading(true);
      setError('');
      const data = await eventService.getAll();
      setEvents(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load events');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Are you sure you want to delete this event?')) {
      return;
    }
    try {
      await eventService.delete(id);
      loadEvents();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to delete event');
    }
  };

  if (loading) return <Loading />;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Events</h1>
        <Link
          to="/events/new"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          Create Event
        </Link>
      </div>

      {error && <ErrorMessage message={error} />}

      {events.length === 0 ? (
        <div className="text-center py-12 text-gray-500">
          No events found. Create your first event!
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {events.map((event) => (
            <div key={event.id} className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-xl font-semibold mb-2">{event.name}</h2>
              <p className="text-gray-600 mb-1">
                <span className="font-medium">Location:</span> {event.location}
              </p>
              <p className="text-gray-600 mb-4">
                <span className="font-medium">Date:</span>{' '}
                {new Date(event.date).toLocaleString()}
              </p>
              {event.ticketIds && event.ticketIds.length > 0 && (
                <p className="text-sm text-gray-500 mb-4">
                  {event.ticketIds.length} ticket(s)
                </p>
              )}
              <div className="flex space-x-2">
                <Link
                  to={`/events/${event.id}`}
                  className="flex-1 bg-blue-600 text-white px-3 py-1 rounded text-center hover:bg-blue-700 transition"
                >
                  View
                </Link>
                <Link
                  to={`/events/edit/${event.id}`}
                  className="flex-1 bg-yellow-500 text-white px-3 py-1 rounded text-center hover:bg-yellow-600 transition"
                >
                  Edit
                </Link>
                <button
                  onClick={() => handleDelete(event.id!)}
                  className="flex-1 bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700 transition"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
