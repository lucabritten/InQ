import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { eventService } from '../services/eventService';
import type { Event } from '../types';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

export default function EventDetails() {
  const { id } = useParams();
  const [event, setEvent] = useState<Event | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    if (id) {
      loadEvent(Number(id));
    }
  }, [id]);

  const loadEvent = async (eventId: number) => {
    try {
      setLoading(true);
      setError('');
      const data = await eventService.getOne(eventId);
      setEvent(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load event');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loading />;
  if (error) return <ErrorMessage message={error} />;
  if (!event) return <div>Event not found</div>;

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Event Details</h1>
        <Link
          to="/events"
          className="bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400 transition"
        >
          Back to Events
        </Link>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="mb-4">
          <h2 className="text-2xl font-semibold mb-2">{event.name}</h2>
        </div>

        <div className="space-y-3">
          <div>
            <span className="font-medium text-gray-700">Location:</span>
            <span className="ml-2 text-gray-600">{event.location}</span>
          </div>

          <div>
            <span className="font-medium text-gray-700">Date:</span>
            <span className="ml-2 text-gray-600">
              {new Date(event.date).toLocaleString()}
            </span>
          </div>

          {event.ticketIds && event.ticketIds.length > 0 && (
            <div>
              <span className="font-medium text-gray-700">Tickets:</span>
              <span className="ml-2 text-gray-600">
                {event.ticketIds.length} ticket(s) sold
              </span>
            </div>
          )}
        </div>

        <div className="mt-6 flex space-x-4">
          <Link
            to={`/events/edit/${event.id}`}
            className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 transition"
          >
            Edit Event
          </Link>
          <Link
            to={`/tickets/new?eventId=${event.id}`}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
          >
            Create Ticket for Event
          </Link>
        </div>
      </div>
    </div>
  );
}
