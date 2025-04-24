import { setupWorker } from 'msw';
import { handlers } from 'mock/handlers.js';

export const worker = setupWorker(...handlers);
