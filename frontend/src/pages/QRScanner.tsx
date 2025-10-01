import { useState, useRef, useEffect } from 'react';
import { BrowserMultiFormatReader } from '@zxing/library';
import { ticketService } from '../services/ticketService';
import ErrorMessage from '../components/ErrorMessage';

export default function QRScanner() {
  const [scanning, setScanning] = useState(false);
  const [result, setResult] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');
  const videoRef = useRef<HTMLVideoElement>(null);
  const codeReaderRef = useRef<BrowserMultiFormatReader | null>(null);

  useEffect(() => {
    return () => {
      stopScanning();
    };
  }, []);

  const startScanning = async () => {
    try {
      setError('');
      setSuccess('');
      setResult('');
      setScanning(true);

      const codeReader = new BrowserMultiFormatReader();
      codeReaderRef.current = codeReader;

      const videoInputDevices = await codeReader.listVideoInputDevices();
      if (videoInputDevices.length === 0) {
        throw new Error('No camera found');
      }

      const selectedDeviceId = videoInputDevices[0].deviceId;

      codeReader.decodeFromVideoDevice(
        selectedDeviceId,
        videoRef.current!,
        (result, err) => {
          if (result) {
            const text = result.getText();
            setResult(text);
            handleScan(text);
            stopScanning();
          }
          if (err && !(err instanceof Error && err.message.includes('NotFoundException'))) {
            console.error(err);
          }
        }
      );
    } catch (err: any) {
      setError(err.message || 'Failed to start camera');
      setScanning(false);
    }
  };

  const stopScanning = () => {
    if (codeReaderRef.current) {
      codeReaderRef.current.reset();
      codeReaderRef.current = null;
    }
    setScanning(false);
  };

  const handleScan = async (qrCode: string) => {
    try {
      setError('');

      // The frontend QR code is expected to be a JSON string
      // e.g., {"eventId":1,"userId":2,"ticketId":3}
      let parsed: { eventId?: number; userId?: number; ticketId?: number };
      try {
        parsed = JSON.parse(qrCode);
      } catch {
        setError('Invalid JSON format in QR code');
        return;
      }

      const { eventId, userId, ticketId } = parsed;
      if (
        typeof eventId !== 'number' || isNaN(eventId) ||
        typeof userId !== 'number' || isNaN(userId) ||
        typeof ticketId !== 'number' || isNaN(ticketId)
      ) {
        setError('QR code must contain valid eventId, userId, and ticketId as numbers');
        return;
      }

      // Validate the ticket by sending only the ticketId to the backend
      const updatedTicket = await ticketService.useTicket(ticketId);
      setSuccess(
        `Ticket #${ticketId} (User: ${userId}, Event: ${eventId}) successfully validated! Status: ${updatedTicket.status}`
      );
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to validate ticket');
    }
  };

  const handleManualInput = async (e: React.FormEvent) => {
    e.preventDefault();
    if (result) {
      handleScan(result);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">QR Code Scanner</h1>

      {error && <ErrorMessage message={error} />}

      {success && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative mb-4">
          <strong className="font-bold">Success! </strong>
          <span className="block sm:inline">{success}</span>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="mb-6">
          <h2 className="text-xl font-semibold mb-4">Scan QR Code</h2>
          
          {!scanning ? (
            <button
              onClick={startScanning}
              className="w-full bg-blue-600 text-white px-4 py-3 rounded hover:bg-blue-700 transition"
            >
              Start Camera
            </button>
          ) : (
            <button
              onClick={stopScanning}
              className="w-full bg-red-600 text-white px-4 py-3 rounded hover:bg-red-700 transition"
            >
              Stop Scanning
            </button>
          )}
        </div>

        {scanning && (
          <div className="mb-6">
            <video
              ref={videoRef}
              className="w-full rounded border-2 border-gray-300"
              style={{ maxHeight: '400px' }}
            />
          </div>
        )}

        <div className="border-t pt-6 mt-6">
          <h2 className="text-xl font-semibold mb-4">Manual QR Code Entry</h2>
          <form onSubmit={handleManualInput} className="flex space-x-2">
            <input
              type="text"
              value={result}
              onChange={(e) => setResult(e.target.value)}
              placeholder="Enter QR code manually (userId,eventId,ticketId)"
              className="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              type="submit"
              className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
            >
              Validate
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
