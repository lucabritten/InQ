import { Link, Outlet } from 'react-router-dom';

export default function Layout() {
  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-blue-600 text-white shadow-lg">
        <div className="container mx-auto px-4">
          <div className="flex items-center justify-between h-16">
            <Link to="/" className="text-2xl font-bold">
              🎟️ InQ - Event Ticketing
            </Link>
            <div className="flex space-x-4">
              <Link
                to="/events"
                className="px-3 py-2 rounded-md hover:bg-blue-700 transition"
              >
                Events
              </Link>
              <Link
                to="/users"
                className="px-3 py-2 rounded-md hover:bg-blue-700 transition"
              >
                Users
              </Link>
              <Link
                to="/tickets"
                className="px-3 py-2 rounded-md hover:bg-blue-700 transition"
              >
                Tickets
              </Link>
              <Link
                to="/scanner"
                className="px-3 py-2 rounded-md hover:bg-blue-700 transition"
              >
                QR Scanner
              </Link>
            </div>
          </div>
        </div>
      </nav>
      <main className="container mx-auto px-4 py-8">
        <Outlet />
      </main>
    </div>
  );
}
