import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import ErrorMessage from '../ErrorMessage';

describe('ErrorMessage', () => {
  it('renders error message correctly', () => {
    render(<ErrorMessage message="Test error message" />);
    expect(screen.getByText(/Test error message/i)).toBeInTheDocument();
  });

  it('renders multiple error messages when provided', () => {
    const errors = ['Error 1', 'Error 2'];
    render(<ErrorMessage message="Validation failed" errors={errors} />);
    expect(screen.getByText(/Validation failed/i)).toBeInTheDocument();
    expect(screen.getByText('Error 1')).toBeInTheDocument();
    expect(screen.getByText('Error 2')).toBeInTheDocument();
  });
});
