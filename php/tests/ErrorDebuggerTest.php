<?php
namespace ErrorDebugger\Tests;

use PHPUnit\Framework\TestCase;
use ErrorDebugger\Config;
use ErrorDebugger\ErrorDebugger;

class ErrorDebuggerTest extends TestCase
{
    public function testConfigValidation()
    {
        $this->expectException(\InvalidArgumentException::class);
        new Config('', 'token');
        
        $this->expectException(\InvalidArgumentException::class);
        new Config('dsn', '');
    }

    public function testInit()
    {
        $config = new Config('http://localhost:8000', 'test-token');
        
        // Just ensure no exception is thrown
        ErrorDebugger::init($config);
        $this->assertTrue(true);
    }
    
    public function testCapture()
    {
        $config = new Config('http://localhost:8000', 'test-token');
        ErrorDebugger::init($config);
        
        try {
            throw new \Exception("Test exception");
        } catch (\Throwable $e) {
            ErrorDebugger::captureException($e);
        }
        $this->assertTrue(true);
    }
}
