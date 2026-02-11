const { init, captureException } = require('./src/index');

console.log('Testing ErrorDebugger JS SDK...');

try {
    init({
        dsn: 'http://localhost:8000',
        token: 'test-token-smoke',
        debug: true
    });

    console.log('✅ Init success');

    try {
        throw new Error('Smoke Test Error');
    } catch (e) {
        captureException(e, { smoke: true });
        console.log('✅ Capture Success');
    }

} catch (e) {
    console.error('❌ Failed:', e);
    process.exit(1);
}
