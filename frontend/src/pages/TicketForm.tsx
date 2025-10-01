import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { ticketService } from '../services/ticketService';
import { eventService } from '../services/eventService';
import { userService } from '../services/userService';
import type { Ticket, Event, User } from '../types';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

export default function TicketForm() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>('');
  const [events, setEvents] = useState<Event[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [formData, setFormData] = useState<Ticket>({
    eventId: Number(searchParams.get('eventId')) || 0,
    userId: Number(searchParams.get('userId')) || 0,
    status: 'VALID',
  });

  useEffect(() => {
    loadEventsAndUsers();
  }, []);

  const loadEventsAndUsers = async () => {
    try {
      setLoading(true);
      const [eventsData, usersData] = await Promise.all([
        eventService.getAll(),
        userService.getAll(),
      ]);
      setEvents(eventsData);
      setUsers(usersData);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setLoading(true);
      setError('');
      // Create DTO with only required fields
      const ticketDTO = {
        eventId: formData.eventId,
        userId: formData.userId,
      };
      await ticketService.create(ticketDTO);
      navigate('/tickets');
    } catch (err: any) {
      const errorData = err.response?.data;
      if (errorData?.errors) {
        setError(errorData.message || 'Validation failed');
      } else {
        setError(errorData?.message || 'Failed to create ticket');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: Number(e.target.value),
    });
  };

  if (loading && events.length === 0) return <Loading />;

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Create Ticket</h1>

      {error && <ErrorMessage message={error} />}

      <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow-md p-6">
        <div className="mb-4">
          <label className="block text-gray-700 font-medium mb-2">
            Event *
          </label>
          <select
            name="eventId"
            value={formData.eventId}
            onChange={handleChange}
            required
            className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Select an event</option>
            {events.map((event) => (
              <option key={event.id} value={event.id}>
                {event.name} - {new Date(event.date).toLocaleDateString()}
              </option>
            ))}
          </select>
        </div>

        <div className="mb-6">
          <label className="block text-gray-700 font-medium mb-2">
            User *
          </label>
          <select
            name="userId"
            value={formData.userId}
            onChange={handleChange}
            required
            className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Select a user</option>
            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.name} ({user.emailAddress})
              </option>
            ))}
          </select>
        </div>

        <div className="flex space-x-4">
          <button
            type="submit"
            disabled={loading}
            className="flex-1 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition disabled:bg-gray-400"
          >
            {loading ? 'Creating...' : 'Create Ticket'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/tickets')}
            className="flex-1 bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400 transition"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
