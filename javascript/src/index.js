const { NodeSDK } = require('@opentelemetry/sdk-node');
const { OTLPTraceExporter } = require('@opentelemetry/exporter-trace-otlp-http');
const { Resource } = require('@opentelemetry/resources');
const { SemanticResourceAttributes } = require('@opentelemetry/semantic-conventions');
const { getNodeAutoInstrumentations } = require('@opentelemetry/auto-instrumentations-node');
const { trace } = require('@opentelemetry/api');

let sdk;
let _initialized = false;

/**
 * Initialize the ErrorDebugger SDK.
 * @param {Object} options Configuration options
 * @param {string} options.dsn ErrorDebugger backend URL (e.g. http://localhost:8000)
 * @param {string} options.token Project API Token
 * @param {string} [options.serviceName] Service name (default: nodejs-app)
 * @param {string} [options.environment] Deployment environment (default: production)
 * @param {boolean} [options.debug] Enable debug logging (default: false)
 */
function init(options) {
    if (_initialized) {
        console.warn('ErrorDebugger SDK already initialized');
        return sdk;
    }

    const {
        dsn,
        token,
        serviceName = 'nodejs-app',
        environment = 'production',
        debug = false
    } = options;

    if (!dsn || !token) {
        throw new Error('ErrorDebugger: dsn and token are required');
    }

    if (debug) {
        console.log(`Initializing ErrorDebugger for ${serviceName} in ${environment}`);
    }

    const resource = new Resource({
        [SemanticResourceAttributes.SERVICE_NAME]: serviceName,
        [SemanticResourceAttributes.DEPLOYMENT_ENVIRONMENT]: environment,
        'project.token': token,
        'telemetry.sdk.language': 'nodejs',
        'telemetry.sdk.name': 'errordebugger-js',
        'telemetry.sdk.version': '0.1.0'
    });

    const traceExporter = new OTLPTraceExporter({
        url: `${dsn.replace(/\/$/, '')}/v1/otlp/traces`,
        headers: {
            'x-project-token': token
        }
    });

    sdk = new NodeSDK({
        resource: resource,
        traceExporter: traceExporter,
        instrumentations: [getNodeAutoInstrumentations({
            // Disable some noisy instrumentations if needed, but keep generic defaults for now
        })]
    });

    try {
        sdk.start();
        _initialized = true;
        if (debug) console.log('ErrorDebugger SDK started successfully');

        // Graceful shutdown
        process.on('SIGTERM', () => {
            sdk.shutdown()
                .then(() => console.log('Tracing terminated'))
                .catch((error) => console.log('Error terminating tracing', error))
                .finally(() => process.exit(0));
        });

    } catch (error) {
        console.error('Error starting ErrorDebugger SDK:', error);
    }

    return sdk;
}

/**
 * Manually capture an exception.
 * @param {Error} exception The error object
 * @param {Object} [context] Additional context attributes
 */
function captureException(exception, context = {}) {
    try {
        const tracer = trace.getTracer('errordebugger-manual');
        const span = tracer.startSpan('manual_exception');

        span.recordException(exception);
        span.setStatus({ code: 2, message: exception.message }); // 2 = ERROR

        // Add context attributes
        Object.entries(context).forEach(([key, value]) => {
            span.setAttribute(key, value);
        });

        span.end();
    } catch (e) {
        console.error('Failed to capture exception:', e);
    }
}

module.exports = {
    init,
    captureException
};
