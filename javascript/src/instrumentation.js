function getInstrumentations() {
    const instrumentations = [];

    // Try HTTP/HTTPS
    try {
        const { HttpInstrumentation } = require('@opentelemetry/instrumentation-http');
        instrumentations.push(new HttpInstrumentation());
    } catch (e) { }

    // Try Express
    try {
        const { ExpressInstrumentation } = require('@opentelemetry/instrumentation-express');
        instrumentations.push(new ExpressInstrumentation());
    } catch (e) { }

    // Try Fastify
    try {
        const { FastifyInstrumentation } = require('@opentelemetry/instrumentation-fastify');
        instrumentations.push(new FastifyInstrumentation());
    } catch (e) { }

    return instrumentations;
}

module.exports = { getInstrumentations };
