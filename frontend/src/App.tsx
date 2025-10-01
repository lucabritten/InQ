import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Home from './pages/Home';
import EventList from './pages/EventList';
import EventForm from './pages/EventForm';
import EventDetails from './pages/EventDetails';
import UserList from './pages/UserList';
import UserForm from './pages/UserForm';
import UserDetails from './pages/UserDetails';
import TicketList from './pages/TicketList';
import TicketForm from './pages/TicketForm';
import TicketDetails from './pages/TicketDetails';
import QRScanner from './pages/QRScanner';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          
          {/* Event routes */}
          <Route path="events" element={<EventList />} />
          <Route path="events/new" element={<EventForm />} />
          <Route path="events/edit/:id" element={<EventForm />} />
          <Route path="events/:id" element={<EventDetails />} />
          
          {/* User routes */}
          <Route path="users" element={<UserList />} />
          <Route path="users/new" element={<UserForm />} />
          <Route path="users/edit/:id" element={<UserForm />} />
          <Route path="users/:id" element={<UserDetails />} />
          
          {/* Ticket routes */}
          <Route path="tickets" element={<TicketList />} />
          <Route path="tickets/new" element={<TicketForm />} />
          <Route path="tickets/:id" element={<TicketDetails />} />
          
          {/* QR Scanner */}
          <Route path="scanner" element={<QRScanner />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

