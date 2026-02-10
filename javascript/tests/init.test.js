const { init, captureException } = require('../src/index');

// Mock dependencies
jest.mock('@opentelemetry/sdk-node');
jest.mock('@opentelemetry/exporter-trace-otlp-http');
jest.mock('@opentelemetry/resources');
jest.mock('@opentelemetry/auto-instrumentations-node');
jest.mock('@opentelemetry/api', () => ({
    trace: {
        getTracer: jest.fn().mockReturnValue({
            startSpan: jest.fn().mockReturnValue({
                recordException: jest.fn(),
                setStatus: jest.fn(),
                setAttribute: jest.fn(),
                end: jest.fn()
            })
        })
    }
}));

describe('ErrorDebugger JS SDK', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('init should throw if dsn or token missing', () => {
        expect(() => init({})).toThrow('dsn and token are required');
    });

    test('init should initialize NodeSDK', () => {
        const sdk = init({
            dsn: 'http://localhost:8000',
            token: 'test-token'
        });

        // We could assert on mocked constructors here
        // For now just checking it returns something (the sdk instance)
        expect(sdk).toBeTruthy();
    });

    test('captureException should record exception', () => {
        const error = new Error('Test error');
        captureException(error, { user: '123' });

        // Assertions would go here against the mocked trace API
        // This is a basic smoke test ensuring no crashes
    });
});
