import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { QRCodeSVG } from 'qrcode.react';
import { ticketService } from '../services/ticketService';
import type { Ticket } from '../types';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

export default function TicketDetails() {
  const { id } = useParams();
  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    if (id) {
      loadTicket(Number(id));
    }
  }, [id]);

  const loadTicket = async (ticketId: number) => {
    try {
      setLoading(true);
      setError('');
      const data = await ticketService.getOne(ticketId);
      setTicket(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load ticket');
    } finally {
      setLoading(false);
    }
  };

  const handleDownloadPDF = async () => {
    if (!id) return;
    try {
      const pdfUrl = await ticketService.getPDF(Number(id));
      const link = document.createElement('a');
      link.href = pdfUrl;
      link.download = `ticket-${id}.pdf`;
      link.click();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to download PDF');
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'VALID':
        return 'bg-green-100 text-green-800';
      case 'USED':
        return 'bg-gray-100 text-gray-800';
      case 'INVALID':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  if (loading) return <Loading />;
  if (error) return <ErrorMessage message={error} />;
  if (!ticket) return <div>Ticket not found</div>;

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Ticket Details</h1>
        <Link
          to="/tickets"
          className="bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400 transition"
        >
          Back to Tickets
        </Link>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="mb-4">
          <h2 className="text-2xl font-semibold mb-2">Ticket #{ticket.id}</h2>
          <span
            className={`px-3 py-1 inline-flex text-sm leading-5 font-semibold rounded-full ${getStatusColor(
              ticket.status
            )}`}
          >
            {ticket.status}
          </span>
        </div>

        <div className="space-y-3 mb-6">
          <div>
            <span className="font-medium text-gray-700">Event:</span>
            <Link
              to={`/events/${ticket.eventId}`}
              className="ml-2 text-blue-600 hover:text-blue-900"
            >
              View Event #{ticket.eventId}
            </Link>
          </div>

          <div>
            <span className="font-medium text-gray-700">User:</span>
            <Link
              to={`/users/${ticket.userId}`}
              className="ml-2 text-blue-600 hover:text-blue-900"
            >
              View User #{ticket.userId}
            </Link>
          </div>
        </div>

        {ticket.qrCode && (
          <div className="mt-6 mb-6 text-center">
            <h3 className="text-lg font-semibold mb-4">QR Code</h3>
            <div className="flex justify-center">
              <div className="bg-white p-4 rounded-lg border-2 border-gray-200">
                <QRCodeSVG
                  value={ticket.qrCode}
                  size={256}
                  level="H"
                  includeMargin={true}
                />
              </div>
            </div>
          </div>
        )}

        <div className="mt-6 flex space-x-4">
          <button
            onClick={handleDownloadPDF}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
          >
            Download PDF
          </button>
          {ticket.status === 'VALID' && (
            <Link
              to={`/scanner?ticketId=${ticket.id}`}
              className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
            >
              Scan & Validate
            </Link>
          )}
        </div>
      </div>
    </div>
  );
}
