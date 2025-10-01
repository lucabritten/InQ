import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { userService } from '../services/userService';
import type { User } from '../types';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

export default function UserDetails() {
  const { id } = useParams();
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    if (id) {
      loadUser(Number(id));
    }
  }, [id]);

  const loadUser = async (userId: number) => {
    try {
      setLoading(true);
      setError('');
      const data = await userService.getOne(userId);
      setUser(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load user');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loading />;
  if (error) return <ErrorMessage message={error} />;
  if (!user) return <div>User not found</div>;

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">User Details</h1>
        <Link
          to="/users"
          className="bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400 transition"
        >
          Back to Users
        </Link>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="mb-4">
          <h2 className="text-2xl font-semibold mb-2">{user.name}</h2>
        </div>

        <div className="space-y-3">
          <div>
            <span className="font-medium text-gray-700">Email:</span>
            <span className="ml-2 text-gray-600">{user.emailAddress}</span>
          </div>

          <div>
            <span className="font-medium text-gray-700">Age:</span>
            <span className="ml-2 text-gray-600">{user.age}</span>
          </div>

          {user.createdAt && (
            <div>
              <span className="font-medium text-gray-700">Created:</span>
              <span className="ml-2 text-gray-600">
                {new Date(user.createdAt).toLocaleString()}
              </span>
            </div>
          )}

          {user.ticketIds && user.ticketIds.length > 0 && (
            <div>
              <span className="font-medium text-gray-700">Tickets:</span>
              <span className="ml-2 text-gray-600">
                {user.ticketIds.length} ticket(s)
              </span>
              <div className="mt-2">
                {user.ticketIds.map((ticketId) => (
                  <Link
                    key={ticketId}
                    to={`/tickets/${ticketId}`}
                    className="inline-block bg-blue-100 text-blue-800 px-3 py-1 rounded mr-2 mb-2 hover:bg-blue-200 transition"
                  >
                    Ticket #{ticketId}
                  </Link>
                ))}
              </div>
            </div>
          )}
        </div>

        <div className="mt-6 flex space-x-4">
          <Link
            to={`/users/edit/${user.id}`}
            className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 transition"
          >
            Edit User
          </Link>
          <Link
            to={`/tickets/new?userId=${user.id}`}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
          >
            Create Ticket for User
          </Link>
        </div>
      </div>
    </div>
  );
}
