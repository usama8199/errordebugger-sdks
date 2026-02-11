#!/usr/bin/env php
<?php
/**
 * Integration test app for PHP SDK
 */

require_once __DIR__ . '/../../vendor/autoload.php';

use ErrorDebugger\ErrorDebugger;

if ($argc < 3) {
    fwrite(STDERR, "Usage: test_app.php <dsn> <token>\n");
    exit(1);
}

$dsn = $argv[1];
$token = $argv[2];

// Initialize SDK
ErrorDebugger::init([
    'dsn' => $dsn,
    'token' => $token,
    'service_name' => 'php-integration-test',
    'debug' => true
]);

// Trigger a test error
$exception = new Exception('Integration test error from PHP SDK');
ErrorDebugger::captureException($exception);

// Allow time for export
sleep(2);
ErrorDebugger::shutdown();
echo "PHP SDK test completed\n";
