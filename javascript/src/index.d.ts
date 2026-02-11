export interface InitOptions {
    /** ErrorDebugger backend URL (e.g. http://localhost:8000) */
    dsn: string;
    /** Project API Token */
    token: string;
    /** Service name (default: nodejs-app) */
    serviceName?: string;
    /** Deployment environment (default: production) */
    environment?: string;
    /** Enable debug logging (default: false) */
    debug?: boolean;
}

/**
 * Initialize the ErrorDebugger SDK.
 */
export function init(options: InitOptions): any;

/**
 * Manually capture an exception.
 * @param exception The error object
 * @param context Additional context attributes
 */
export function captureException(exception: Error, context?: Record<string, any>): void;
