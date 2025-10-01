import { Link } from 'react-router-dom';

export default function Home() {
  return (
    <div className="text-center">
      <h1 className="text-4xl font-bold mb-4">Welcome to InQ</h1>
      <p className="text-xl text-gray-600 mb-8">
        Event Ticketing System - Manage events, users, and tickets with ease
      </p>

      <div className="grid gap-6 md:grid-cols-3 max-w-4xl mx-auto">
        <Link
          to="/events"
          className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
        >
          <div className="text-4xl mb-4">📅</div>
          <h2 className="text-xl font-semibold mb-2">Events</h2>
          <p className="text-gray-600">
            Create and manage events, set dates and locations
          </p>
        </Link>

        <Link
          to="/users"
          className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
        >
          <div className="text-4xl mb-4">👥</div>
          <h2 className="text-xl font-semibold mb-2">Users</h2>
          <p className="text-gray-600">
            Manage users and view their ticket history
          </p>
        </Link>

        <Link
          to="/tickets"
          className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
        >
          <div className="text-4xl mb-4">🎟️</div>
          <h2 className="text-xl font-semibold mb-2">Tickets</h2>
          <p className="text-gray-600">
            Generate tickets with QR codes and manage sales
          </p>
        </Link>
      </div>

      <div className="mt-12 max-w-2xl mx-auto">
        <div className="bg-blue-50 rounded-lg shadow-md p-6">
          <h2 className="text-2xl font-semibold mb-4">QR Code Validation</h2>
          <p className="text-gray-700 mb-4">
            Use the QR Scanner to validate tickets at your event entrance
          </p>
          <Link
            to="/scanner"
            className="inline-block bg-blue-600 text-white px-6 py-3 rounded hover:bg-blue-700 transition"
          >
            Open QR Scanner
          </Link>
        </div>
      </div>
    </div>
  );
}
