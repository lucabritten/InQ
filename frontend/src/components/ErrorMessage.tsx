interface ErrorMessageProps {
  message: string;
  errors?: string[];
}

export default function ErrorMessage({ message, errors }: ErrorMessageProps) {
  return (
    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
      <strong className="font-bold">Error: </strong>
      <span className="block sm:inline">{message}</span>
      {errors && errors.length > 0 && (
        <ul className="mt-2 list-disc list-inside">
          {errors.map((error, index) => (
            <li key={index}>{error}</li>
          ))}
        </ul>
      )}
    </div>
  );
}
